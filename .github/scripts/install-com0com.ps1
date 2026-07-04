$ErrorActionPreference = "Stop"

$Com0ComUrl = "https://files.akeo.ie/blog/com0com.7z"
$ExpectedCom0ComArchiveSha256 = "90305C1D690985BFFA6BBBC9E4729A7D132765F11CFFAFFC30D3FC7EE0B30772"
$ModemPort = "COM50"
$DtePort = "COM51"

function Add-GitHubEnvironmentValue {
  param(
    [Parameter(Mandatory = $true)][string] $Name,
    [Parameter(Mandatory = $true)][string] $Value
  )

  "$Name=$Value" | Out-File -FilePath $env:GITHUB_ENV -Append
}

function Write-Com0ComSummary {
  param([Parameter(Mandatory = $true)][string] $Message)

  @"
GitHub-hosted Windows could not provision the com0com virtual COM driver in this run.

$Message

Use a self-hosted Windows runner with com0com preinstalled for required Windows serial integration coverage.
"@ | Out-File -FilePath $env:GITHUB_STEP_SUMMARY -Append
}

function Disable-Com0Com {
  param([Parameter(Mandatory = $true)][string] $Reason)

  Write-Host "::error title=com0com unavailable::$Reason"
  Add-GitHubEnvironmentValue -Name "MODEMSIM_WINDOWS_COM0COM_AVAILABLE" -Value "false"
  Write-Com0ComSummary -Message $Reason
  exit 1
}

function Show-ProcessLog {
  param(
    [Parameter(Mandatory = $true)][string] $Label,
    [Parameter(Mandatory = $true)][string] $Path
  )

  if ((Test-Path $Path) -and ((Get-Item $Path).Length -gt 0)) {
    Write-Host "${Label}:"
    Get-Content -Path $Path | ForEach-Object { Write-Host $_ }
  }
}

function Invoke-TimedProcess {
  param(
    [Parameter(Mandatory = $true)][string] $FilePath,
    [string[]] $ArgumentList = @(),
    [string] $WorkingDirectory,
    [Parameter(Mandatory = $true)][int] $TimeoutSeconds,
    [Parameter(Mandatory = $true)][string] $Description
  )

  $logPrefix = ($Description -replace '[^A-Za-z0-9_-]', '_')
  $stdout = Join-Path $env:RUNNER_TEMP "$logPrefix.out.log"
  $stderr = Join-Path $env:RUNNER_TEMP "$logPrefix.err.log"
  $argumentText = $ArgumentList -join " "
  Write-Host "Running $Description`: $FilePath $argumentText"

  $startProcessArguments = @{
    FilePath = $FilePath
    ArgumentList = $ArgumentList
    NoNewWindow = $true
    PassThru = $true
    RedirectStandardOutput = $stdout
    RedirectStandardError = $stderr
  }
  if ($WorkingDirectory) {
    $startProcessArguments.WorkingDirectory = $WorkingDirectory
    Write-Host "Working directory: $WorkingDirectory"
  }

  $process = Start-Process @startProcessArguments

  if (-not $process.WaitForExit($TimeoutSeconds * 1000)) {
    Stop-Process -Id $process.Id -Force
    Get-Process -Name "setupc" -ErrorAction SilentlyContinue | Stop-Process -Force
    Show-ProcessLog -Label "$Description stdout" -Path $stdout
    Show-ProcessLog -Label "$Description stderr" -Path $stderr
    Write-Host "::warning title=com0com unavailable::$Description timed out after $TimeoutSeconds seconds."
    return [pscustomobject]@{
      TimedOut = $true
      ExitCode = $null
    }
  }

  Show-ProcessLog -Label "$Description stdout" -Path $stdout
  Show-ProcessLog -Label "$Description stderr" -Path $stderr
  Write-Host "$Description exit code: $($process.ExitCode)"
  return [pscustomobject]@{
    TimedOut = $false
    ExitCode = $process.ExitCode
  }
}

function Assert-TimedProcessSucceeded {
  param(
    [Parameter(Mandatory = $true)] $Result,
    [Parameter(Mandatory = $true)][int] $TimeoutSeconds,
    [Parameter(Mandatory = $true)][string] $Description
  )

  if ($Result.TimedOut) {
    Disable-Com0Com -Reason "$Description timed out after $TimeoutSeconds seconds."
  }
  if ($Result.ExitCode -ne 0) {
    Disable-Com0Com -Reason "$Description failed with exit code $($Result.ExitCode)."
  }
}

function Invoke-Setupc {
  param(
    [Parameter(Mandatory = $true)][System.IO.FileInfo] $Setupc,
    [Parameter(Mandatory = $true)][string[]] $SetupArgs,
    [int] $TimeoutSeconds = 90,
    [switch] $AllowTimeout
  )

  $description = "setupc $($SetupArgs -join ' ')"
  $result = Invoke-TimedProcess `
    -FilePath $Setupc.FullName `
    -ArgumentList $SetupArgs `
    -WorkingDirectory $Setupc.DirectoryName `
    -TimeoutSeconds $TimeoutSeconds `
    -Description $description
  if ($AllowTimeout -and $result.TimedOut) {
    Write-Host "$description timed out, but this command can finish device creation before exiting. Continuing to verification."
    return
  }
  Assert-TimedProcessSucceeded -Result $result -TimeoutSeconds $TimeoutSeconds -Description $description
}

function Invoke-OptionalProcess {
  param(
    [Parameter(Mandatory = $true)][string] $FilePath,
    [string[]] $ArgumentList = @(),
    [Parameter(Mandatory = $true)][int] $TimeoutSeconds,
    [Parameter(Mandatory = $true)][string] $Description
  )

  $result = Invoke-TimedProcess `
    -FilePath $FilePath `
    -ArgumentList $ArgumentList `
    -TimeoutSeconds $TimeoutSeconds `
    -Description $Description
  if ($result.TimedOut) {
    Write-Host "::warning title=com0com diagnostics::$Description timed out after $TimeoutSeconds seconds."
  } elseif ($result.ExitCode -ne 0) {
    Write-Host "::warning title=com0com diagnostics::$Description failed with exit code $($result.ExitCode)."
  }
}

function Assert-Sha256 {
  param(
    [Parameter(Mandatory = $true)][string] $Path,
    [Parameter(Mandatory = $true)][string] $ExpectedSha256,
    [Parameter(Mandatory = $true)][string] $Description
  )

  $actualSha256 = (Get-FileHash -Path $Path -Algorithm SHA256).Hash.ToUpperInvariant()
  $normalizedExpectedSha256 = $ExpectedSha256.ToUpperInvariant()
  if ($actualSha256 -ne $normalizedExpectedSha256) {
    throw "$Description SHA-256 mismatch. Expected $normalizedExpectedSha256 but got $actualSha256."
  }
  Write-Host "Verified $Description SHA-256: $actualSha256"
}

function Find-SevenZip {
  $command = Get-Command "7z.exe" -ErrorAction SilentlyContinue
  if ($command) {
    return $command.Source
  }

  $candidatePaths = @(
    (Join-Path $env:ProgramFiles "7-Zip\7z.exe"),
    (Join-Path ${env:ProgramFiles(x86)} "7-Zip\7z.exe")
  )
  foreach ($candidatePath in $candidatePaths) {
    if ($candidatePath -and (Test-Path $candidatePath)) {
      return $candidatePath
    }
  }

  throw "Could not find 7z.exe to extract the com0com archive."
}

$work = Join-Path $env:RUNNER_TEMP "com0com"
New-Item -ItemType Directory -Force -Path $work | Out-Null

$archive = Join-Path $work "com0com.7z"
Write-Host "Downloading com0com from $Com0ComUrl"
& curl.exe -L --fail --retry 3 -o $archive $Com0ComUrl
if ($LASTEXITCODE -ne 0) {
  throw "curl failed while downloading com0com with exit code $LASTEXITCODE."
}
Write-Host "Downloaded com0com archive bytes: $((Get-Item $archive).Length)"
Assert-Sha256 -Path $archive -ExpectedSha256 $ExpectedCom0ComArchiveSha256 -Description "com0com archive"

$sevenZip = Find-SevenZip
$extractResult = Invoke-TimedProcess `
  -FilePath $sevenZip `
  -ArgumentList @("x", "-y", "-o$work", $archive) `
  -TimeoutSeconds 60 `
  -Description "extract com0com archive"
Assert-TimedProcessSucceeded -Result $extractResult -TimeoutSeconds 60 -Description "extract com0com archive"

$driverDirectory = Join-Path $work "x64"
$setupcPath = Join-Path $driverDirectory "setupc.exe"
if (-not (Test-Path $setupcPath)) {
  throw "Could not find x64 setupc.exe in extracted com0com archive."
}
$setupc = Get-Item $setupcPath
$catalogPath = Join-Path $driverDirectory "com0com.cat"
if (Test-Path $catalogPath) {
  $catalogSignature = Get-AuthenticodeSignature -FilePath $catalogPath
  Write-Host "com0com catalog signature status: $($catalogSignature.Status)"
}
Write-Host "Using setupc: $($setupc.FullName)"

Invoke-Setupc `
  -Setupc $setupc `
  -SetupArgs @("install", "PortName=$ModemPort", "PortName=$DtePort") `
  -TimeoutSeconds 240 `
  -AllowTimeout
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCA0", "PortName=$ModemPort")
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCB0", "PortName=$DtePort")
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCA0", "EmuBR=yes")
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCB0", "EmuOverrun=yes")
Invoke-Setupc -Setupc $setupc -SetupArgs @("list")
Invoke-OptionalProcess `
  -FilePath "pnputil.exe" `
  -ArgumentList @("/scan-devices") `
  -TimeoutSeconds 60 `
  -Description "pnputil scan-devices"

for ($attempt = 0; $attempt -lt 120; $attempt++) {
  $ports = [System.IO.Ports.SerialPort]::GetPortNames()
  if (($ports -contains $ModemPort) -and ($ports -contains $DtePort)) {
    break
  }
  Start-Sleep -Seconds 1
}

$ports = [System.IO.Ports.SerialPort]::GetPortNames()
if (-not (($ports -contains $ModemPort) -and ($ports -contains $DtePort))) {
  Write-Host "com0com PnP device state:"
  Get-PnpDevice |
    Where-Object { $_.FriendlyName -like "*com0com*" -or $_.InstanceId -like "*com0com*" } |
    Format-Table -AutoSize |
    Out-String |
    ForEach-Object { Write-Host $_ }
  Disable-Com0Com -Reason "Ports did not appear. Available ports: $($ports -join ', ')"
}

Add-GitHubEnvironmentValue -Name "MODEMSIM_WINDOWS_COM0COM_AVAILABLE" -Value "true"
Add-GitHubEnvironmentValue -Name "MODEMSIM_SERIAL_MODEM_PORT" -Value $ModemPort
Add-GitHubEnvironmentValue -Name "MODEMSIM_SERIAL_DTE_PORT" -Value $DtePort

@"
com0com virtual serial pair is available on GitHub-hosted Windows.

Modem port: $ModemPort
DTE port: $DtePort
"@ | Out-File -FilePath $env:GITHUB_STEP_SUMMARY -Append

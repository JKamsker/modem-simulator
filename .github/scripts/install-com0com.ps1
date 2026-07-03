$ErrorActionPreference = "Stop"

$Com0ComUrl = "https://sourceforge.net/projects/com0com/files/com0com/3.0.0.0/com0com-3.0.0.0-i386-and-x64-signed.zip/download"
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

  Write-Host "::warning title=com0com unavailable::$Reason"
  Add-GitHubEnvironmentValue -Name "MODEMSIM_WINDOWS_COM0COM_AVAILABLE" -Value "false"
  Write-Com0ComSummary -Message $Reason
  exit 0
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
    [Parameter(Mandatory = $true)][string[]] $SetupArgs
  )

  $timeoutSeconds = 90
  $description = "setupc $($SetupArgs -join ' ')"
  $result = Invoke-TimedProcess `
    -FilePath $Setupc.FullName `
    -ArgumentList $SetupArgs `
    -WorkingDirectory $Setupc.DirectoryName `
    -TimeoutSeconds $timeoutSeconds `
    -Description $description
  Assert-TimedProcessSucceeded -Result $result -TimeoutSeconds $timeoutSeconds -Description $description
}

function Find-Setupc {
  param([Parameter(Mandatory = $true)][string] $WorkDirectory)

  $searchRoots = @($WorkDirectory, $env:ProgramFiles, ${env:ProgramFiles(x86)}) |
    Where-Object { $_ -and (Test-Path $_) }
  Get-ChildItem -Path $searchRoots -Filter "setupc.exe" -Recurse -ErrorAction SilentlyContinue |
    Select-Object -First 1
}

$work = Join-Path $env:RUNNER_TEMP "com0com"
New-Item -ItemType Directory -Force -Path $work | Out-Null

$zip = Join-Path $work "com0com.zip"
Write-Host "Downloading com0com from $Com0ComUrl"
& curl.exe -L --fail --retry 3 -o $zip $Com0ComUrl
if ($LASTEXITCODE -ne 0) {
  throw "curl failed while downloading com0com with exit code $LASTEXITCODE."
}
Write-Host "Downloaded com0com package bytes: $((Get-Item $zip).Length)"

& tar.exe -xf $zip -C $work
if ($LASTEXITCODE -ne 0) {
  throw "tar failed while extracting com0com with exit code $LASTEXITCODE."
}

$installer = Get-ChildItem -Path $work -Filter "*x64_signed.exe" -Recurse | Select-Object -First 1
if (-not $installer) {
  throw "Could not find com0com x64 signed installer in downloaded package."
}
Write-Host "Using com0com installer: $($installer.FullName)"

$env:CNC_INSTALL_START_MENU_SHORTCUTS = "NO"
$env:CNC_INSTALL_CNCA0_CNCB0_PORTS = "NO"
$env:CNC_INSTALL_COMX_COMX_PORTS = "NO"
$installerTimeoutSeconds = 120
$installerResult = Invoke-TimedProcess `
  -FilePath $installer.FullName `
  -ArgumentList @("/S") `
  -TimeoutSeconds $installerTimeoutSeconds `
  -Description "com0com installer"

$setupc = Find-Setupc -WorkDirectory $work
if (-not $setupc) {
  if ($installerResult.TimedOut) {
    Disable-Com0Com -Reason "com0com installer timed out after $installerTimeoutSeconds seconds and setupc.exe was not found."
  }
  if ($installerResult.ExitCode -ne 0) {
    Disable-Com0Com -Reason "com0com installer failed with exit code $($installerResult.ExitCode) and setupc.exe was not found."
  }
  Disable-Com0Com -Reason "setupc.exe was not found after com0com installation."
}
if ($installerResult.TimedOut) {
  Write-Host "Installer timed out, but setupc.exe exists. Trying bounded setupc provisioning fallback."
} elseif ($installerResult.ExitCode -ne 0) {
  Write-Host "Installer returned exit code $($installerResult.ExitCode), but setupc.exe exists. Trying bounded setupc provisioning fallback."
}
Write-Host "Using setupc: $($setupc.FullName)"

Invoke-Setupc -Setupc $setupc -SetupArgs @("install", "-", "-")
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCA0", "PortName=$ModemPort")
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCB0", "PortName=$DtePort")
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCA0", "EmuBR=yes")
Invoke-Setupc -Setupc $setupc -SetupArgs @("change", "CNCB0", "EmuOverrun=yes")
Invoke-Setupc -Setupc $setupc -SetupArgs @("list")

for ($attempt = 0; $attempt -lt 30; $attempt++) {
  $ports = [System.IO.Ports.SerialPort]::GetPortNames()
  if (($ports -contains $ModemPort) -and ($ports -contains $DtePort)) {
    break
  }
  Start-Sleep -Seconds 1
}

$ports = [System.IO.Ports.SerialPort]::GetPortNames()
if (-not (($ports -contains $ModemPort) -and ($ports -contains $DtePort))) {
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

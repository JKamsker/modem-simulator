@echo off
setlocal

set "APP_HOME=%~dp0.."
if defined JAVA_HOME (
  set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVA_EXE=java"
)

"%JAVA_EXE%" --enable-native-access=ALL-UNNAMED -Dmodemsim.home="%APP_HOME%" %MODEMSIM_JAVA_OPTS% -cp "%APP_HOME%\lib\*" com.jkamsker.modemsim.app.ModemSimCli %*
exit /b %ERRORLEVEL%

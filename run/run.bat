@echo off
setlocal

:: This file location
set "PROJECT_ROOT=%~dp0..\"
set "PROJECT_ROOT=%PROJECT_ROOT:\=\%
set "JAR_FILE=%PROJECT_ROOT%target\BankAccountManagement.jar"

:: Running the application
echo Running the application...
java -jar "%JAR_FILE%"

endlocal
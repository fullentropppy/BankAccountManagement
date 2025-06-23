@echo off
setlocal

set SRC_DIR=D:\IT\Repositories\BankAccountManagement\src
set OUT_DIR=D:\IT\Repositories\BankAccountManagement\out\production\BankAccountManagement_BAT
set MAIN_CLASS=main.ru.dgritsenko.ru.dgritsenko.app.Application

if not exist "%BIN_DIR%" mkdir "%OUT_DIR%"

echo Compilation...
javac -d "%OUT_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\ru\dgritsenko\bam\main\Application.java"

if errorlevel 1 (
    echo Compilation error...
    pause
    exit /b 1
)

java -cp "%OUT_DIR%" %MAIN_CLASS%

endlocal
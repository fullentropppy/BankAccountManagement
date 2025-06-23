@echo off
setlocal

set SRC_DIR=D:\IT\Repositories\BankAccountManagement\src
set OUT_DIR=D:\IT\Repositories\BankAccountManagement\out\production\BankAccountManagement_BAT
set TEST_CLASS=ru.dgritsenko.bam.test.Test

if not exist "%BIN_DIR%" mkdir "%OUT_DIR%"

echo Compilation...
javac -d "%OUT_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\ru\dgritsenko\bam\test\Test.java"

if errorlevel 1 (
    echo Compilation error...
    pause
    exit /b 1
)

java -cp "%OUT_DIR%" %TEST_CLASS%

endlocal
@echo off
setlocal

set SRC_DIR=D:\IT\Repositories\BankAccountManagement\src
set OUT_DIR=D:\IT\Repositories\BankAccountManagement\out\production\BankAccountManagement_BAT
set MAIN_CLASS=main.ru.dgritsenko.ru.dgritsenko.app.Application
set JAR_FILE=D:\IT\Repositories\BankAccountManagement\run\bam.jar
set MANIFEST_FILE=%OUT_DIR%\MANIFEST.MF

if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

echo Compilation...
javac -d "%OUT_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\ru\dgritsenko\bam\main\Application.java"

if errorlevel 1 (
    echo Compilation error...
    pause
    exit /b 1
)

echo Creating manifest file...
echo Main-Class: %MAIN_CLASS% > "%MANIFEST_FILE%"

echo Creating JAR file...
jar cvfm "%JAR_FILE%" "%MANIFEST_FILE%" -C "%OUT_DIR%" .

if errorlevel 1 (
    echo JAR creation error...
    pause
    exit /b 1
)

endlocal
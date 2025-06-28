@echo off
setlocal

:: This file location
set "PROJECT_ROOT=%~dp0..\"
set "PROJECT_ROOT=%PROJECT_ROOT:\=\%
echo Project root: %PROJECT_ROOT%

:: Setting the paths of PROJECT_ROOT
set "SRC_DIR=%PROJECT_ROOT%src\main\java"
set "CLASS_DIR=%PROJECT_ROOT%target\classes"
set "JAR_FILE=%PROJECT_ROOT%target\BankAccountManagement.jar"
set "MAIN_CLASS=ru.dgritsenko.app.Application"

:: Creating a folder for classes if it doesn't exist
if not exist "%CLASS_DIR%" mkdir "%CLASS_DIR%"

:: Class-files compilation
echo Compilation...
javac -d "%CLASS_DIR%" -sourcepath "%SRC_DIR%" "%SRC_DIR%\ru\dgritsenko\app\Application.java"
if errorlevel 1 (
    echo Compilation error!
    pause
    exit /b 1
)

:: JAR-file creating
echo JAR-creating...
jar cvfe "%JAR_FILE%" "%MAIN_CLASS%" -C "%CLASS_DIR%" .
if errorlevel 1 (
    echo JAR-creating error!
    pause
    exit /b 1
)

:: Running the application
echo Running the application...
java -jar "%JAR_FILE%"

endlocal
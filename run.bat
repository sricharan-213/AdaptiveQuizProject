@echo off
echo ========================================
echo Running Adaptive Quiz System
echo ========================================
echo.

REM Set your JavaFX path here - MUST MATCH compile.bat
set JAVAFX_PATH=C:\javafx-sdk-17.0.2\lib

REM Check if compiled classes exist
if not exist "out\main\App.class" (
    echo ERROR: Application not compiled!
    echo Please run compile.bat first
    pause
    exit /b 1
)

echo Starting application...
echo.

java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp out main.App

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo Application failed to start!
    echo ========================================
    pause
)


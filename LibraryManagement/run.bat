@echo off
echo ========================================================
echo Starting Question 2: Spring Boot Library Management API
echo ========================================================
cd /d "%~dp0"
call .\mvnw.cmd spring-boot:run
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Trying with system maven...
    mvn spring-boot:run
)
pause

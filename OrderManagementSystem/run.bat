@echo off
echo ========================================================
echo Compiling and Running Order Management System (JDBC)
echo ========================================================
if not exist bin mkdir bin
javac -cp ".;lib\*" -d bin OrderManagementSystem.java
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)
java -cp "bin;lib\*" OrderManagementSystem
pause

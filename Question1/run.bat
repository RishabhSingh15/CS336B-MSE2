@echo off
echo ===================================================
echo Compiling and Running Question 1 (Order Management)
echo ===================================================
if not exist bin mkdir bin
javac -cp ".;lib\*" -d bin OrderManagementSystem.java
if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)
java -cp "bin;lib\*" question1.OrderManagementSystem
pause

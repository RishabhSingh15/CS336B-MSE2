Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "Compiling and Running Order Management System (JDBC)" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
javac -cp ".;lib\*" -d bin OrderManagementSystem.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful! Starting application..." -ForegroundColor Green
    java -cp "bin;lib\*" OrderManagementSystem
} else {
    Write-Host "Compilation failed with error code $LASTEXITCODE" -ForegroundColor Red
}

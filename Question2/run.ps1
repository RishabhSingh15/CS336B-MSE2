Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "Starting Question 2: Spring Boot Library Management API" -ForegroundColor Cyan
Write-Host "========================================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot
if (Test-Path ".\mvnw.cmd") {
    .\mvnw.cmd spring-boot:run
} else {
    mvn spring-boot:run
}

# Quick Start Database Script
# This script ensures the database is running (without confirmation prompts)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  QUICK START - AutoHub Database" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/3] Checking Docker..." -ForegroundColor Yellow
try {
    docker ps | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: Docker is not running." -ForegroundColor Red
        Write-Host "Please start Docker Desktop and try again." -ForegroundColor Yellow
        exit 1
    }
    Write-Host "✓ Docker is running" -ForegroundColor Green
} catch {
    Write-Host "Error: Docker is not installed or not in PATH." -ForegroundColor Red
    exit 1
}

Write-Host "`n[2/3] Checking database container..." -ForegroundColor Yellow
$containerRunning = docker ps --filter "name=autohub-db" --format "{{.Names}}"

if ($containerRunning -eq "autohub-db") {
    Write-Host "✓ Database is already running" -ForegroundColor Green
} else {
    Write-Host "Database not running. Starting..." -ForegroundColor Yellow
    docker-compose up -d

    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Database started" -ForegroundColor Green
        Write-Host "`n[3/3] Waiting for PostgreSQL to be ready..." -ForegroundColor Yellow
        Start-Sleep -Seconds 8
        Write-Host "✓ Database ready" -ForegroundColor Green
    } else {
        Write-Host "Error: Failed to start database" -ForegroundColor Red
        exit 1
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  ✅ DATABASE IS READY!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "You can now run your Spring Boot application." -ForegroundColor Green
Write-Host ""
Write-Host "Useful commands:" -ForegroundColor Cyan
Write-Host "  View logs:  docker-compose logs -f db" -ForegroundColor White
Write-Host "  Stop:       docker-compose down" -ForegroundColor White
Write-Host "  Reset:      .\reset-db.ps1" -ForegroundColor White
Write-Host ""


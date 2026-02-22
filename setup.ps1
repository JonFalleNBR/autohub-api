# AutoHub API - Database Setup Script
# This script resets and starts the PostgreSQL database

Write-Host ""
Write-Host "========================================"
Write-Host "  AUTOHUB API - DATABASE SETUP"
Write-Host "========================================"
Write-Host ""
Write-Host "WARNING: This will DELETE all existing data!" -ForegroundColor Red
Write-Host ""

$confirmation = Read-Host "Continue? (yes/no)"
if ($confirmation -ne "yes") {
    Write-Host "Cancelled."
    exit 0
}

Write-Host ""
Write-Host "[1/4] Checking Docker..." -ForegroundColor Yellow

$dockerRunning = $false
try {
    docker ps 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        $dockerRunning = $true
    }
}
catch {
    $dockerRunning = $false
}

if (-not $dockerRunning) {
    Write-Host "[ERROR] Docker is not running!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please start Docker Desktop and try again."
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[OK] Docker is running" -ForegroundColor Green

Write-Host ""
Write-Host "[2/4] Stopping containers..." -ForegroundColor Yellow
docker-compose down 2>&1 | Out-Null
Write-Host "[OK] Containers stopped" -ForegroundColor Green

Write-Host ""
Write-Host "[3/4] Removing volumes..." -ForegroundColor Yellow
docker-compose down -v 2>&1 | Out-Null
docker volume rm autohub-api_postgres_data -f 2>&1 | Out-Null
Write-Host "[OK] Volumes removed" -ForegroundColor Green

Write-Host ""
Write-Host "[4/4] Starting database..." -ForegroundColor Yellow
docker-compose up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Failed to start database!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Check logs: docker-compose logs db"
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[OK] Database started" -ForegroundColor Green
Write-Host ""
Write-Host "Waiting for PostgreSQL to be ready (15 seconds)..." -ForegroundColor Yellow

for ($i = 15; $i -gt 0; $i--) {
    Write-Host "  $i..." -NoNewline
    Start-Sleep -Seconds 1
}

Write-Host ""
Write-Host ""
Write-Host "========================================"
Write-Host "  SETUP COMPLETE!" -ForegroundColor Green
Write-Host "========================================"
Write-Host ""
Write-Host "Database is ready!" -ForegroundColor Green
Write-Host ""
Write-Host "Connection Details:"
Write-Host "  Host:     localhost:5432"
Write-Host "  Database: autohub"
Write-Host "  User:     autohub"
Write-Host "  Password: autohub123"
Write-Host ""
Write-Host "Next Step:"
Write-Host "  Run your Spring Boot application"
Write-Host ""
Write-Host "Ready!" -ForegroundColor Green
Write-Host ""

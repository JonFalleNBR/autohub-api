# Quick Setup Script - Simple version without special characters
# This script resets the database completely

Write-Host ""
Write-Host "========================================"
Write-Host "  AUTOHUB API - SETUP"
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
try {
    docker ps | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: Docker is not running!" -ForegroundColor Red
        Write-Host "Please start Docker Desktop and try again."
        Read-Host "Press Enter to exit"
        exit 1
    }
    Write-Host "OK: Docker is running" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Docker is not installed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[2/4] Stopping containers..." -ForegroundColor Yellow
docker-compose down 2>$null
Write-Host "OK: Containers stopped" -ForegroundColor Green

Write-Host ""
Write-Host "[3/4] Removing volumes..." -ForegroundColor Yellow
docker-compose down -v 2>$null
docker volume rm autohub-api_postgres_data -f 2>$null
Write-Host "OK: Volumes removed" -ForegroundColor Green

Write-Host ""
Write-Host "[4/4] Starting database..." -ForegroundColor Yellow
docker-compose up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to start database!" -ForegroundColor Red
    Write-Host "Check logs: docker-compose logs db"
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "OK: Database started" -ForegroundColor Green
Write-Host ""
Write-Host "Waiting for PostgreSQL (15 seconds)..." -ForegroundColor Yellow

for ($i = 15; $i -gt 0; $i--) {
    Write-Host "  $i..." -NoNewline
    Start-Sleep -Seconds 1
}

Write-Host ""
Write-Host ""
Write-Host "========================================"
Write-Host "  SETUP COMPLETE!"
Write-Host "========================================"
Write-Host ""
Write-Host "Database is ready!" -ForegroundColor Green
Write-Host ""
Write-Host "Connection Details:" -ForegroundColor Cyan
Write-Host "  Host:     localhost:5432"
Write-Host "  Database: autohub"
Write-Host "  User:     autohub"
Write-Host "  Password: autohub123"
Write-Host ""
Write-Host "Next Step:" -ForegroundColor Cyan
Write-Host "  Run your Spring Boot application"
Write-Host "  Flyway will create these tables:"
Write-Host "    - tenants"
Write-Host "    - clientes"
Write-Host "    - cars"
Write-Host "    - service_history"
Write-Host ""
Write-Host "Ready to use!" -ForegroundColor Green
Write-Host ""


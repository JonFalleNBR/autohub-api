# Reset Database Script
# This script stops, removes, and recreates the PostgreSQL database

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RESET DATABASE - AutoHub API" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "WARNING: This will delete ALL existing data!" -ForegroundColor Red
Write-Host ""

$confirmation = Read-Host "Are you sure you want to continue? (yes/no)"
if ($confirmation -ne "yes") {
    Write-Host "`nOperation cancelled." -ForegroundColor Yellow
    exit 0
}

Write-Host "`n[1/4] Checking Docker..." -ForegroundColor Yellow
try {
    docker ps | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: Docker is not running. Please start Docker Desktop first." -ForegroundColor Red
        exit 1
    }
    Write-Host "✓ Docker is running" -ForegroundColor Green
} catch {
    Write-Host "Error: Docker is not installed or not in PATH." -ForegroundColor Red
    exit 1
}

Write-Host "`n[2/4] Stopping containers..." -ForegroundColor Yellow
docker-compose down
Write-Host "✓ Containers stopped" -ForegroundColor Green

Write-Host "`n[3/4] Removing volumes (deleting all data)..." -ForegroundColor Yellow
docker-compose down -v
docker volume rm autohub-api_postgres_data -f 2>$null
Write-Host "✓ Volumes removed" -ForegroundColor Green

Write-Host "`n[4/4] Starting fresh database..." -ForegroundColor Yellow
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Database container started" -ForegroundColor Green
    Write-Host "`nWaiting 8 seconds for PostgreSQL to initialize..." -ForegroundColor Yellow

    for ($i = 8; $i -gt 0; $i--) {
        Write-Host "  $i..." -NoNewline
        Start-Sleep -Seconds 1
    }
    Write-Host ""

    Write-Host "`n========================================" -ForegroundColor Cyan
    Write-Host "  ✅ DATABASE RESET COMPLETE!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Database is ready for Flyway migrations." -ForegroundColor Green
    Write-Host "You can now run your Spring Boot application." -ForegroundColor Green
    Write-Host ""
    Write-Host "Connection details:" -ForegroundColor Cyan
    Write-Host "  Host:     localhost:5432" -ForegroundColor White
    Write-Host "  Database: autohub" -ForegroundColor White
    Write-Host "  User:     autohub" -ForegroundColor White
    Write-Host "  Password: autohub123" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host "`nError: Failed to start database." -ForegroundColor Red
    Write-Host "Try running: docker-compose logs db" -ForegroundColor Yellow
    exit 1
}


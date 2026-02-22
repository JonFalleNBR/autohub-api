# Start Database Script
# This script starts the PostgreSQL database using Docker Compose

Write-Host "Starting PostgreSQL database..." -ForegroundColor Green

# Check if Docker is running
try {
    docker ps | Out-Null
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Error: Docker is not running. Please start Docker Desktop first." -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "Error: Docker is not installed or not in PATH." -ForegroundColor Red
    exit 1
}

# Start the database
Write-Host "Running docker-compose up -d..." -ForegroundColor Yellow
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "`nDatabase started successfully!" -ForegroundColor Green
    Write-Host "Waiting 5 seconds for PostgreSQL to be ready..." -ForegroundColor Yellow
    Start-Sleep -Seconds 5

    Write-Host "`nDatabase is ready. You can now run your Spring Boot application." -ForegroundColor Green
    Write-Host "`nTo check database status, run: docker-compose ps" -ForegroundColor Cyan
    Write-Host "To view logs, run: docker-compose logs -f" -ForegroundColor Cyan
    Write-Host "To stop database, run: docker-compose down" -ForegroundColor Cyan
} else {
    Write-Host "`nError: Failed to start database." -ForegroundColor Red
    Write-Host "Try running: docker-compose down -v" -ForegroundColor Yellow
    Write-Host "Then run this script again." -ForegroundColor Yellow
    exit 1
}


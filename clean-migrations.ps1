# Script para limpar migrations duplicadas

Write-Host "Limpando migrations duplicadas..." -ForegroundColor Yellow

$migrationsPath = "C:\autohub-project\autohub-api\src\main\resources\db\migration"

# Remover V2__create_cars.sql (antiga - sem cliente_id)
$oldV2 = Join-Path $migrationsPath "V2__create_cars.sql"
if (Test-Path $oldV2) {
    $content = Get-Content $oldV2 -Raw
    if ($content -notmatch "cliente_id") {
        Remove-Item $oldV2 -Force
        Write-Host "✓ Removido: V2__create_cars.sql (antiga)" -ForegroundColor Green
    }
}

# Remover V3__create_service_history.sql (duplicata - manter V4)
$v3service = Join-Path $migrationsPath "V3__create_service_history.sql"
if (Test-Path $v3service) {
    Remove-Item $v3service -Force
    Write-Host "✓ Removido: V3__create_service_history.sql (duplicata)" -ForegroundColor Green
}

Write-Host "`n✅ Migrations limpas!" -ForegroundColor Green
Write-Host "`nEstrutura final:" -ForegroundColor Cyan
Write-Host "  V1__create_tenants.sql" -ForegroundColor White
Write-Host "  V2__create_clientes.sql" -ForegroundColor White
Write-Host "  V3__create_cars.sql" -ForegroundColor White
Write-Host "  V4__create_service_history.sql" -ForegroundColor White


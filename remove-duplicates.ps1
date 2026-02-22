# Script para remover duplicatas

Write-Host "Removendo duplicatas..." -ForegroundColor Yellow

# Remover pasta db/migration duplicada
$dbPath = "C:\autohub-project\autohub-api\src\main\java\com\autohub_api\db"
if (Test-Path $dbPath) {
    Remove-Item -Recurse -Force $dbPath
    Write-Host "✓ Removido: com.autohub_api/db/" -ForegroundColor Green
}

# Remover pasta model/entity duplicada
$modelPath = "C:\autohub-project\autohub-api\src\main\java\com\autohub_api\model"
if (Test-Path $modelPath) {
    Remove-Item -Recurse -Force $modelPath
    Write-Host "✓ Removido: com.autohub_api/model/" -ForegroundColor Green
}

Write-Host "`n✅ Duplicatas removidas com sucesso!" -ForegroundColor Green
Write-Host "`nEstrutura correta mantida:" -ForegroundColor Cyan
Write-Host "  ✓ model/Entity/" -ForegroundColor White
Write-Host "  ✓ resources/db/migration/" -ForegroundColor White


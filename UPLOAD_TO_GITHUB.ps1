# Script para subir MateKids a GitHub con workflow automático
# Uso: .\upload_to_github.ps1 -GitHubUser "tu_usuario"

param(
    [Parameter(Mandatory=$true)]
    [string]$GitHubUser,
    [string]$RepoName = "MateKids"
)

Write-Host "╔════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║  MateKids - GitHub Upload & Build CI  ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════╝" -ForegroundColor Cyan

# Verificar git
Write-Host "`n📋 Verificando requisitos..." -ForegroundColor Yellow
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Git no está instalado" -ForegroundColor Red
    exit 1
}

Write-Host "✓ Git encontrado" -ForegroundColor Green

# Ir al directorio del proyecto
$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectDir
Write-Host "✓ Directorio: $projectDir" -ForegroundColor Green

# Verificar estado de Git
Write-Host "`n📦 Estado del repositorio..." -ForegroundColor Yellow
git status

# Preguntar si continuar
Write-Host "`n¿Deseas continuar? (S/N)" -ForegroundColor Cyan
$continue = Read-Host

if ($continue -ne "S" -and $continue -ne "s") {
    Write-Host "❌ Operación cancelada" -ForegroundColor Red
    exit 1
}

# Añadir cambios
Write-Host "`n📝 Preparando cambios..." -ForegroundColor Yellow
git add -A
Write-Host "✓ Archivos agregados" -ForegroundColor Green

# Crear commit - Versión simplificada para PowerShell
Write-Host "`n💾 Creando commit..." -ForegroundColor Yellow
git commit -m "feat: MateKids v1.0.0 - Release inicial con CI/CD automático"
Write-Host "✓ Commit creado" -ForegroundColor Green

# Configurar remote
Write-Host "`n🌐 Configurando remoto GitHub..." -ForegroundColor Yellow
$remoteUrl = "https://github.com/$GitHubUser/$RepoName.git"

# Verificar si remote ya existe
$remoteExists = git remote get-url origin 2>$null
if ($null -ne $remoteExists) {
    Write-Host "⚠️  Remote 'origin' ya existe" -ForegroundColor Yellow
    git remote remove origin
    Write-Host "✓ Remote anterior removido" -ForegroundColor Green
}

git remote add origin $remoteUrl
Write-Host "✓ Remote configurado: $remoteUrl" -ForegroundColor Green

# Renombrar rama a main
Write-Host "`n🔄 Configurando rama principal..." -ForegroundColor Yellow
$currentBranch = git rev-parse --abbrev-ref HEAD
if ($currentBranch -ne "main") {
    git branch -M main
    Write-Host "✓ Rama renombrada a main" -ForegroundColor Green
} else {
    Write-Host "✓ Ya estamos en rama main" -ForegroundColor Green
}

# Subir al repositorio
Write-Host "`n📤 Subiendo al repositorio..." -ForegroundColor Yellow
Write-Host "   Esto puede tomar un momento..." -ForegroundColor Gray

try {
    git push -u origin main
    Write-Host "✓ Código subido exitosamente" -ForegroundColor Green
} catch {
    Write-Host "❌ Error al subir: $_" -ForegroundColor Red
    Write-Host "`n💡 Verifica que:" -ForegroundColor Yellow
    Write-Host "   1. El repositorio existe en GitHub"
    Write-Host "   2. Tienes permisos de push"
    Write-Host "   3. Tu usuario y contraseña son correctos"
    exit 1
}

# Resumen final
Write-Host "`n╔════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║  ✅ ¡LISTO PARA USAR CI/CD!           ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════╝" -ForegroundColor Green

Write-Host "`n📊 Información:" -ForegroundColor Cyan
Write-Host "   Repositorio: $remoteUrl" -ForegroundColor White
Write-Host "   Rama: main" -ForegroundColor White
Write-Host "   Usuario: $GitHubUser" -ForegroundColor White

Write-Host "`n🚀 GitHub Actions está configurado:" -ForegroundColor Cyan
Write-Host "   ✓ Se ejecuta automáticamente en cada push" -ForegroundColor Green
Write-Host "   ✓ Compila el proyecto" -ForegroundColor Green
Write-Host "   ✓ Ejecuta tests" -ForegroundColor Green
Write-Host "   ✓ Genera APK debug y release" -ForegroundColor Green
Write-Host "   ✓ Crea Release automático en GitHub" -ForegroundColor Green

Write-Host "`n📦 Acceder a los APKs:" -ForegroundColor Cyan
Write-Host "   1. Ve a: https://github.com/$GitHubUser/$RepoName" -ForegroundColor White
Write-Host "   2. Click en 'Releases'" -ForegroundColor White
Write-Host "   3. Descarga los APKs" -ForegroundColor White

Write-Host "`n💡 Próximos pasos:" -ForegroundColor Cyan
Write-Host "   - Verifica que el workflow se ejecute en GitHub Actions" -ForegroundColor White
Write-Host "   - Descarga los APKs generados" -ForegroundColor White
Write-Host "   - Instala el APK debug en tu dispositivo para probar" -ForegroundColor White

Write-Host "`n" -ForegroundColor White

# ============================================
# Script para subir MateKids a GitHub
# Ejecutar: .\UPLOAD_TO_GITHUB.ps1
# ============================================

Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
Write-Host "   MateKids - GitHub Upload" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Pedir credenciales
$GITHUB_USER = Read-Host "Ingresa tu usuario de GitHub"
$GITHUB_EMAIL = Read-Host "Ingresa tu email de GitHub"

if ([string]::IsNullOrEmpty($GITHUB_USER)) {
    Write-Host "Error: Usuario vacio" -ForegroundColor Red
    exit 1
}

try {
    # Paso 1: Configurar Git
    Write-Host "[1/6] Configurando Git..." -ForegroundColor Yellow
    & git config user.name "$GITHUB_USER"
    & git config user.email "$GITHUB_EMAIL"

    # Paso 2: Inicializar repositorio
    Write-Host "[2/6] Inicializando repositorio..." -ForegroundColor Yellow
    & git init

    # Paso 3: Agregar archivos
    Write-Host "[3/6] Agregando archivos..." -ForegroundColor Yellow
    & git add .

    # Paso 4: Primer commit
    Write-Host "[4/6] Creando primer commit..." -ForegroundColor Yellow
    & git commit -m "Initial commit: MateKids v1.0.0 - Aplicacion educativa de matematicas"

    # Paso 5: Agregar remoto
    Write-Host "[5/6] Conectando con GitHub..." -ForegroundColor Yellow
    $REPO_URL = "https://github.com/$GITHUB_USER/MateKids.git"
    & git remote add origin $REPO_URL

    # Paso 6: Cambiar a rama main
    & git branch -M main

    # Paso 7: Subir a GitHub
    Write-Host "[6/6] Subiendo a GitHub (esto puede tardar)..." -ForegroundColor Yellow
    & git push -u origin main

    Write-Host ""
    Write-Host "============================================" -ForegroundColor Green
    Write-Host "SUCCESS! Codigo subido a GitHub" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "URL del repositorio:" -ForegroundColor Cyan
    Write-Host $REPO_URL -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Proximo paso:" -ForegroundColor Cyan
    Write-Host "1. Ve a https://github.com/$GITHUB_USER/MateKids/actions" -ForegroundColor White
    Write-Host "2. Espera a que termine la compilacion (verde checkmark)" -ForegroundColor White
    Write-Host "3. Descarga los APKs desde Artifacts" -ForegroundColor White
    Write-Host ""

} catch {
    Write-Host ""
    Write-Host "ERROR! Algo salio mal:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    Write-Host ""
    Write-Host "Soluciones:" -ForegroundColor Yellow
    Write-Host "1. Verifica tu usuario de GitHub" -ForegroundColor White
    Write-Host "2. Usa un token personal en lugar de contrasena" -ForegroundColor White
    Write-Host "3. Configura SSH si lo prefieres" -ForegroundColor White
    Write-Host ""
}

Read-Host "Presiona Enter para salir"

@echo off
REM ============================================
REM Script para subir MateKids a GitHub
REM ============================================

setlocal enabledelayedexpansion

echo.
echo ================================
echo   MateKids - GitHub Upload
echo ================================
echo.

REM Pedir usuario de GitHub
set /p GITHUB_USER="Ingresa tu usuario de GitHub: "
set /p GITHUB_EMAIL="Ingresa tu email de GitHub: "

if "%GITHUB_USER%"=="" (
    echo Error: Usuario vacío
    pause
    exit /b 1
)

REM Configurar Git
echo.
echo [1/6] Configurando Git...
git config user.name "%GITHUB_USER%"
git config user.email "%GITHUB_EMAIL%"

REM Inicializar repositorio
echo [2/6] Inicializando repositorio...
git init

REM Agregar todos los archivos
echo [3/6] Agregando archivos...
git add .

REM Primer commit
echo [4/6] Creando primer commit...
git commit -m "Initial commit: MateKids v1.0.0 - Aplicacion educativa de matematicas"

REM Agregar remoto
echo [5/6] Conectando con GitHub...
set REPO_URL=https://github.com/%GITHUB_USER%/MateKids.git
git remote add origin %REPO_URL%

REM Renombrar rama a main
git branch -M main

REM Subir a GitHub
echo [6/6] Subiendo a GitHub (esto puede tardar)...
git push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo ============================================
    echo SUCCESS! Codigo subido a GitHub
    echo ============================================
    echo.
    echo URL del repositorio:
    echo %REPO_URL%
    echo.
    echo Proximo paso:
    echo 1. Ve a https://github.com/%GITHUB_USER%/MateKids/actions
    echo 2. Espera a que termine la compilacion (verde checkmark)
    echo 3. Descarga los APKs desde Artifacts
    echo.
) else (
    echo.
    echo ERROR! Algo salio mal
    echo Verifica tu usuario y token de GitHub
    echo.
    pause
    exit /b 1
)

pause

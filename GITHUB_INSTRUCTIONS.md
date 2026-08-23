# 🚀 Instrucciones: Subir a GitHub con Workflow de Build

Este documento explica cómo subir el proyecto MateKids a GitHub y generar APKs automáticamente.

## 📋 Requisitos

- Git instalado
- Cuenta de GitHub
- [GitHub CLI](https://cli.github.com/) (opcional, pero recomendado)

---

## 🔧 Paso 1: Preparar Repositorio Local

```bash
cd E:\MateKids

# Verificar estado de Git
git status

# Añadir todos los cambios
git add -A

# Crear commit inicial
git commit -m "feat: MateKids v1.0.0 - Release inicial

- Arquitectura MVVM + Hilt DI
- 8 Screens completas
- 6 ViewModels
- 7 Máquinas + 8 Insignias
- Tests unitarios incluidos"
```

---

## 🌐 Paso 2: Crear Repositorio en GitHub

### Opción A: Usando GitHub Web
1. Ir a https://github.com/new
2. Nombre: `MateKids`
3. Descripción: `Aplicación Android educativa de matemáticas para niños 8-12 años`
4. Tipo: Public (o Private según prefieras)
5. **NO** inicializar con README (ya tenemos uno)
6. Click en "Create repository"

### Opción B: Usando GitHub CLI
```bash
gh repo create MateKids --public --source=. --remote=origin --push
```

---

## 📤 Paso 3: Añadir Remote y Subir

```bash
# Reemplaza TU_USUARIO con tu usuario de GitHub
git remote add origin https://github.com/TU_USUARIO/MateKids.git

# Renombrar rama a main si es necesario
git branch -M main

# Subir el código
git push -u origin main
```

---

## ✅ Paso 4: Verificar Workflow

1. Ir a tu repositorio: `https://github.com/TU_USUARIO/MateKids`
2. Click en la pestaña **"Actions"**
3. Verás que el workflow "Android Build APK" está ejecutándose
4. Espera a que termine (2-5 minutos)

---

## 📦 Paso 5: Obtener los APKs

### Opción A: Desde Releases (Automático)
1. Ir a tu repositorio
2. Click en **"Releases"** (lado derecho)
3. Verás la release `v1.0.0-build-X`
4. Descargar:
   - `app-debug.apk` (para pruebas)
   - `app-release-unsigned.apk` (para distribución)

### Opción B: Desde Artifacts (GitHub Actions)
1. Ir a **"Actions"**
2. Selecciona el workflow completado
3. Baja hasta "Artifacts"
4. Descargar:
   - `MateKids-debug` (archivo zip con APK debug)
   - `MateKids-release` (archivo zip con APK release)

---

## 🔄 Próximos Pasos

### Cada que hagas cambios:
```bash
# Realizar cambios en el código

# Commit
git add .
git commit -m "descripción de cambios"

# Push (automáticamente dispara el workflow)
git push
```

### El workflow hará automáticamente:
- ✅ Compilar el proyecto
- ✅ Ejecutar tests
- ✅ Lint del código
- ✅ Generar APK debug
- ✅ Generar APK release
- ✅ Crear Release en GitHub (si es push a main)
- ✅ Subir artifacts

---

## 📱 Instalar APK en tu Dispositivo

### Desde una computadora:
```bash
# Asegúrate que el dispositivo está conectado
adb devices

# Instalar APK debug
adb install app/build/outputs/apk/debug/app-debug.apk

# O instalar directamente del release descargado
adb install /ruta/a/app-debug.apk
```

### Directamente en el dispositivo:
1. Descargar el APK desde GitHub Releases
2. Transferir a tu dispositivo Android
3. Abrir el archivo y instalar
4. Permitir instalación de fuentes desconocidas si lo pide

---

## 🔐 Notas de Seguridad

⚠️ **APK Release sin firmar**: El APK release generado por el workflow no está firmado. Para distribución en Google Play, necesitas:

1. Crear un keystore:
```bash
keytool -genkey -v -keystore matekids-key.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias matekids
```

2. Firmar el APK:
```bash
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore matekids-key.keystore \
  app/build/outputs/apk/release/app-release-unsigned.apk matekids
```

3. Zipealign (optimizar):
```bash
zipalign -v 4 app/build/outputs/apk/release/app-release-unsigned.apk app-release.apk
```

---

## 📊 Status del Proyecto

- **Versión**: 1.0.0
- **Estado**: Release Candidate ✅
- **Fases**: 8/8 completadas
- **Líneas de código**: 7,000+
- **Tests**: 6+ unitarios
- **API Min/Target**: 24/34

---

## 🆘 Solución de Problemas

### El workflow falla en "Build with Gradle"
- Verifica que `build.gradle.kts` esté correctamente formateado
- Revisa que todas las dependencias estén disponibles
- Consulta el log en GitHub Actions para más detalles

### El workflow no se ejecuta automáticamente
- Verifica que el workflow esté en `.github/workflows/android_build.yml`
- Haz un push pequeño para disparar el workflow
- Revisa la pestaña "Actions" → "All workflows"

### No puedo ver el Release
- El release se crea solo en push a `main`
- Los PRs no generan releases automáticamente
- Verifica que el push fue a la rama `main`

---

## 📚 Más Información

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Android Build Guide](https://developer.android.com/build)
- [Google Play Console](https://play.google.com/console)

---

**¡Listo! Tu proyecto está en GitHub con CI/CD automático** 🎉

Cada push a `main` generará automáticamente:
- ✅ APK Debug
- ✅ APK Release
- ✅ Release en GitHub
- ✅ Test Reports

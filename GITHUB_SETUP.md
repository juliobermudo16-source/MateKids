# Instrucciones para Subir MateKids a GitHub

## 1. Crear Repositorio en GitHub

### Paso 1: Ir a GitHub.com
1. Abre https://github.com
2. Inicia sesión en tu cuenta
3. Toca el icono **+** arriba a la derecha
4. Selecciona **New repository**

### Paso 2: Configurar el Repositorio
```
Repository name:        MateKids
Description:            Aplicación Android educativa de matemáticas
Visibility:             Public
Add .gitignore:         Android
Add a license:          MIT
```

### Paso 3: Crear Repositorio
Toca **Create repository**

---

## 2. Preparar el Código Localmente

### Paso 1: Abrir Terminal/PowerShell

```bash
cd E:\MateKids
```

### Paso 2: Inicializar Git

```bash
git init
git config user.name "Tu Nombre"
git config user.email "tu.email@example.com"
```

### Paso 3: Agregar Archivos

```bash
git add .
```

### Paso 4: Primer Commit

```bash
git commit -m "Initial commit: MateKids v1.0.0 - Aplicación educativa de matemáticas"
```

### Paso 5: Agregar Remoto

```bash
git remote add origin https://github.com/tuusuario/MateKids.git
```

Reemplaza `tuusuario` con tu nombre de usuario de GitHub.

### Paso 6: Enviar a GitHub

```bash
git branch -M main
git push -u origin main
```

---

## 3. Verificar en GitHub

1. Abre https://github.com/tuusuario/MateKids
2. Verifica que los archivos estén subidos
3. Revisa la carpeta `.github/workflows/`
4. Los workflows deben aparecer en "Actions"

---

## 4. GitHub Actions (Compilación Automática)

### Qué sucede después de hacer Push

1. GitHub detecta cambios en `main`
2. Ejecuta el workflow `android_build.yml`
3. Compila el proyecto
4. Ejecuta tests
5. Genera APK debug + release
6. Sube los APKs como "Artifacts"

### Ver Compilación

1. Abre tu repositorio en GitHub
2. Toca la pestaña **Actions**
3. Verás cada compilación listada
4. Verde ✅ = Compilación exitosa
5. Rojo ❌ = Error (verifica logs)

### Descargar APK

1. Entra a la compilación exitosa
2. Baja en "Artifacts"
3. Descarga `MateKids-debug` o `MateKids-release`
4. Descomprime el ZIP
5. Encontrarás: `app-debug.apk` o `app-release.apk`

---

## 5. Estructura Final en GitHub

```
MateKids/
├── .github/
│   └── workflows/
│       └── android_build.yml        ← Workflow automático
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/matekids/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── docs/
│   ├── MEMORIA_DESCRIPTIVA.md
│   ├── MANUAL_USUARIO.md
│   ├── MANUAL_TECNICO.md
│   └── BUILD_REPORT.md
├── database/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── README.md
├── INDEX.md
├── ESTADO_PROYECTO.md
├── FINAL_REPORT.md
├── .gitignore
└── [otros archivos]
```

---

## 6. Comandos Git Útiles

### Ver estado
```bash
git status
```

### Ver historial
```bash
git log
```

### Hacer cambios adicionales
```bash
git add .
git commit -m "Descripción del cambio"
git push
```

### Crear rama de desarrollo
```bash
git checkout -b develop
git push -u origin develop
```

---

## 7. Troubleshooting

### Error: "fatal: not a git repository"
**Solución**: Ejecuta `git init` en la carpeta

### Error: "authentication failed"
**Solución**: 
- Usa token personal en lugar de contraseña
- O configura SSH: https://docs.github.com/en/authentication/connecting-to-github-with-ssh

### Workflow no se ejecuta
**Solución**:
- Verifica que `.github/workflows/android_build.yml` esté bien
- Espera 1 minuto después de push
- Revisa la pestaña "Actions"

### APK no se genera
**Solución**:
- Verifica que compile localmente: `./gradlew assembleDebug`
- Revisa logs del workflow en GitHub Actions
- Busca errores de compilación

---

## 8. URLs Importantes

| Recurso | URL |
|---------|-----|
| Tu Repositorio | https://github.com/tuusuario/MateKids |
| GitHub Actions | https://github.com/tuusuario/MateKids/actions |
| Releases | https://github.com/tuusuario/MateKids/releases |
| Issues | https://github.com/tuusuario/MateKids/issues |

---

## 9. Próximos Pasos

### Después de subir a GitHub:

1. ✅ Verifica compilación exitosa
2. ✅ Descarga APK de Artifacts
3. ✅ Instala en dispositivo/emulador
4. ✅ Prueba todas las funcionalidades
5. ✅ Convierte documentos a PDF (Pandas/Word)
6. ✅ Crea una Release (Tag v1.0.0)

### Crear Release en GitHub

```bash
git tag -a v1.0.0 -m "MateKids v1.0.0 - Release inicial"
git push origin v1.0.0
```

Luego en GitHub:
1. Pestaña "Releases"
2. "Create release from tag"
3. Sube los APK y PDFs

---

## 10. Checklista Final

- [ ] Repositorio creado en GitHub
- [ ] Código subido (git push)
- [ ] Workflow ejecutado exitosamente
- [ ] APK generado automáticamente
- [ ] APK descargado y probado
- [ ] Documentos convertidos a PDF
- [ ] Release v1.0.0 creada
- [ ] APKs + PDFs en Release
- [ ] README está completo
- [ ] Documentación visible en GitHub

---

**¡Listo!** MateKids está en GitHub y compilándose automáticamente. 🚀

Para cualquier problema, revisa:
- GitHub Actions (ver logs de compilación)
- `.gitignore` (verifica qué se ignora)
- `build.gradle.kts` (versiones de librerías)

---

**Última actualización**: 2026-08-23

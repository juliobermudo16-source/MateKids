# 🚀 RUN THIS FIRST - Subir MateKids a GitHub

## Opción 1: Script Automático (RECOMENDADO) ⭐

### Para Windows (CMD)

1. **Abre PowerShell o CMD** en `E:\MateKids\`
   ```
   cd E:\MateKids
   ```

2. **Ejecuta el script**
   ```
   UPLOAD_TO_GITHUB.bat
   ```

3. **Ingresa tus datos**
   - Usuario de GitHub: `tuusuario`
   - Email de GitHub: `tu.email@ejemplo.com`

4. **Espera a que termine**
   - El script sube TODO automáticamente

---

### Para Windows (PowerShell)

1. **Abre PowerShell** en `E:\MateKids\`
   ```powershell
   cd E:\MateKids
   ```

2. **Ejecuta el script**
   ```powershell
   .\UPLOAD_TO_GITHUB.ps1
   ```

3. **Ingresa tus datos** cuando lo pida

4. **¡Listo!** GitHub tiene tu código 🎉

---

## Opción 2: Comandos Manuales (Si el script falla)

```bash
cd E:\MateKids

# 1. Configurar Git
git config user.name "Tu Nombre"
git config user.email "tu.email@ejemplo.com"

# 2. Inicializar
git init

# 3. Agregar archivos
git add .

# 4. Primer commit
git commit -m "Initial commit: MateKids v1.0.0"

# 5. Conectar con GitHub (reemplaza tuusuario)
git remote add origin https://github.com/tuusuario/MateKids.git

# 6. Cambiar a main
git branch -M main

# 7. Subir
git push -u origin main
```

---

## ¿Qué sucede después?

### 1️⃣ GitHub genera APK automáticamente

- GitHub Actions se ejecuta automáticamente
- Compila el proyecto
- Genera APK debug + release
- Toma ~5-10 minutos

### 2️⃣ Descarga los APKs

- Ve a: `https://github.com/tuusuario/MateKids/actions`
- Busca la compilación en verde ✅
- Descarga desde **Artifacts**

### 3️⃣ Convierte a PDF (opcional pero recomendado)

```bash
# Necesitas Pandoc: https://pandoc.org/installing.html

pandoc docs/MEMORIA_DESCRIPTIVA.md -o docs/pdf/MEMORIA_DESCRIPTIVA.pdf
pandoc docs/MANUAL_USUARIO.md -o docs/pdf/MANUAL_USUARIO.pdf
pandoc docs/MANUAL_TECNICO.md -o docs/pdf/MANUAL_TECNICO.pdf
```

---

## ❓ Problemas Comunes

### "git command not found"
→ Git no está instalado
→ Descarga desde: https://git-scm.com/

### "fatal: not a git repository"
→ No ejecutaste `git init`
→ Sigue los pasos del script

### "authentication failed"
→ Usuario/contraseña incorrectos
→ Usa token personal: https://github.com/settings/tokens

### El script falla
→ Usa la Opción 2 (comandos manuales)
→ O avísame para ayudarte

---

## ✅ Checklist Rápido

- [ ] Usuario de GitHub creado
- [ ] Repositorio "MateKids" creado en GitHub
- [ ] Git instalado en tu PC
- [ ] Estás en carpeta `E:\MateKids`
- [ ] Ejecutaste el script o comandos
- [ ] GitHub Actions mostró ✅ (verde)
- [ ] Descargaste los APKs

---

## 🎯 Resumido en 3 pasos

1. **Ejecuta**: `UPLOAD_TO_GITHUB.bat` (o .ps1)
2. **Ingresa**: Tu usuario y email de GitHub
3. **Espera**: GitHub genere los APKs (~5-10 min)

**¡Eso es todo!** 🎉

---

## 📱 Después de obtener APKs

1. Descarga `app-debug.apk` de Artifacts
2. Instala en dispositivo/emulador: `adb install app-debug.apk`
3. Prueba todas las funcionalidades
4. ¡Disfruta MateKids!

---

**Tiempo total**: 15-20 minutos  
**Dificultad**: Muy fácil  
**Resultado**: Tu código en GitHub + APK automático ✨

---

### 🆘 ¿Necesitas ayuda?

Si algo falla:
1. Lee el error completo
2. Busca en GITHUB_SETUP.md
3. Intenta los comandos manuales
4. Contacta soporte con el error exacto

---

**¡ADELANTE! A subir MateKids a GitHub! 🚀**

# MateKids - Índice de Navegación

**Bienvenido a MateKids** 🚀 - Aplicación Android educativa de matemáticas para niños de 8-12 años.

Este índice te ayuda a encontrar rápidamente los archivos y documentos más importantes del proyecto.

---

## 📋 Documentación Principal

### Para empezar
- **[README.md](README.md)** - Guía general del proyecto, cómo compilar y usar
- **[ESTADO_PROYECTO.md](ESTADO_PROYECTO.md)** - Estado actual, progreso y próximos pasos
- **[RESUMEN_SESION.md](RESUMEN_SESION.md)** - Resumen detallado de lo que se ha completado
- **[BUILD_REPORT.md](docs/BUILD_REPORT.md)** - Reporte técnico de compilación

### Análisis y diseño
- **[Plan Completo](.claude/plans/zippy-petting-clock.md)** - Plan detallado de diseño y arquitectura

---

## 🏗️ Arquitectura del Código

### Capa Data (Persistencia)
```
app/src/main/kotlin/com/matekids/data/

├── local/
│   ├── MateKidsDatabase.kt              ← Base de datos Room principal
│   ├── dao/
│   │   ├── OperationDao.kt              ← Acceso a operaciones
│   │   ├── ProblemDao.kt                ← Acceso a problemas
│   │   ├── UserDao.kt                   ← Acceso a perfil
│   │   └── AchievementDao.kt            ← Acceso a logros
│   └── entity/
│       ├── OperationEntity.kt           ← Modelo BD: operaciones
│       ├── ProblemEntity.kt             ← Modelo BD: problemas
│       ├── UserProfileEntity.kt         ← Modelo BD: usuario
│       └── AchievementEntity.kt         ← Modelo BD: logros
│
└── repository/
    ├── OperationRepository.kt           ← Repositorio de operaciones
    ├── ProblemRepository.kt             ← Repositorio de problemas
    ├── UserRepository.kt                ← Repositorio de usuario
    └── AchievementRepository.kt         ← Repositorio de logros
```

### Capa Domain (Lógica de Negocio)
```
app/src/main/kotlin/com/matekids/domain/

├── model/
│   ├── Operation.kt                     ← Modelo: operación matemática
│   ├── Problem.kt                       ← Modelo: problema
│   ├── UserProfile.kt                   ← Modelo: perfil usuario
│   └── Achievement.kt                   ← Modelo: logro/insignia
│
└── usecase/
    ├── ResolveOperationUseCase.kt       ← Validar operación
    ├── ResolveProblemUseCase.kt         ← Validar problema
    ├── CalculateProgressUseCase.kt      ← Calcular progreso
    └── GetAchievementsUseCase.kt        ← Gestionar logros
```

### Capa UI (Interfaz de Usuario)
```
app/src/main/kotlin/com/matekids/ui/

├── theme/
│   ├── Color.kt                         ← Paleta Material 3 (32 colores)
│   ├── Typography.kt                    ← Tipografía Material 3
│   └── Theme.kt                         ← Tema Compose
│
├── components/
│   └── MachineCard.kt                   ← Tarjeta de máquina (reutilizable)
│
├── screens/
│   ├── SplashScreen.kt                  ← Pantalla de presentación
│   └── DashboardScreen.kt               ← Pantalla principal
│
└── viewmodel/
    └── (Por implementar)
        ├── OperationViewModel.kt
        ├── ProblemViewModel.kt
        ├── CollectionViewModel.kt
        └── StatsViewModel.kt

└── MainActivity.kt                      ← Actividad principal + Navigation
```

### Testing
```
app/src/test/kotlin/com/matekids/

├── domain/usecase/
│   └── ResolveOperationUseCaseTest.kt   ← Tests del use case
│
└── (Por expandir)
    ├── data/repository/                 ← Tests de repositorios
    └── ui/viewmodel/                    ← Tests de ViewModels
```

---

## 📁 Configuración y Recursos

### Gradle y Build
```
├── build.gradle.kts                     ← Build script raíz
├── settings.gradle.kts                  ← Configuración de módulos
├── gradle.properties                    ← Propiedades de Gradle
├── gradle/
│   ├── libs.versions.toml               ← Versiones centralizadas
│   └── wrapper/
│       └── gradle-wrapper.properties    ← Configuración wrapper
├── gradlew                              ← Script Gradle (Linux/Mac)
└── gradlew.bat                          ← Script Gradle (Windows)
```

### Android Resources
```
app/src/main/

├── AndroidManifest.xml                  ← Configuración de la app
├── res/
│   ├── values/
│   │   ├── colors.xml                   ← Paleta Material 3 (claro)
│   │   ├── strings.xml                  ← Strings en español
│   │   └── themes.xml                   ← Tema claro
│   ├── values-night/
│   │   └── themes.xml                   ← Tema oscuro
│   └── xml/
│       ├── backup_rules.xml             ← Reglas de backup
│       └── data_extraction_rules.xml    ← Seguridad de datos
│
└── (Por crear)
    ├── drawable/                        ← Ilustraciones vectoriales
    └── raw/                             ← Sonidos
```

### Base de Datos
```
database/

├── schema.sql                           ← Definición de tablas
└── sample_data.sql                      ← 25 registros iniciales
```

---

## 🎨 Sistema de Diseño

### Colores (Material 3)
**Archivo**: `app/src/main/kotlin/com/matekids/ui/theme/Color.kt`

- 🔵 **Primario**: Azul Eléctrico (#2563EB)
- 🟣 **Secundario**: Violeta (#5E60C0)
- 🟡 **Terciario**: Ámbar (#F59E0B)
- 🟢 **Éxito**: Verde (#10B981)
- 🔴 **Error**: Rojo (#B3261E)
- ⚪ **Background**: Gris Claro (#F8FAFC)

### Tipografía (Material 3)
**Archivo**: `app/src/main/kotlin/com/matekids/ui/theme/Typography.kt`

14 estilos incluidos:
- Display Large/Medium/Small
- Headline Large/Medium/Small
- Title Large/Medium/Small
- Body Large/Medium/Small
- Label Large/Medium/Small

---

## 🧪 Tests Ejecutables

### Correr todos los tests
```bash
./gradlew testDebugUnitTest
```

### Correr test específico
```bash
./gradlew testDebugUnitTest --tests ResolveOperationUseCaseTest
```

### Con reporte
```bash
./gradlew testDebugUnitTest --info
```

---

## 🏗️ Compilación

### Limpiar y compilar
```bash
./gradlew clean build
```

### Solo compilar (sin tests)
```bash
./gradlew clean build -x test
```

### Generar APK debug
```bash
./gradlew assembleDebug
```

### Generar APK release
```bash
./gradlew assembleRelease
```

### Lint (verificación de código)
```bash
./gradlew lintDebug
```

---

## 📊 Progreso Visual

```
Fase 1: Setup + Arquitectura       ████████████████████ 100% ✅
Fase 2: UI Base + Componentes      ████████████████████ 100% ✅
Fase 3: Lógica Educativa                                   0% ⏳
Fase 4: Pantallas Interactivas                             0% ⏳
Fase 5: Ilustraciones + Animaciones                        0% ⏳
Fase 6: Testing Completo                                   0% ⏳
Fase 7: Documentación Final                                0% ⏳
Fase 8: Build Final                                        0% ⏳

TOTAL:                             ██████░░░░░░░░░░░░░░ 35% 🚀
```

---

## 🎯 Puntos de Entrada

### Para Desarrolladores
1. Leer **[README.md](README.md)**
2. Revisar **[ESTADO_PROYECTO.md](ESTADO_PROYECTO.md)**
3. Estudiar **[.claude/plans/zippy-petting-clock.md](.claude/plans/zippy-petting-clock.md)**
4. Explorar `app/src/main/kotlin/com/matekids/`

### Para Compilar
1. Asegurar JDK 17+ instalado
2. Asegurar Android SDK instalado
3. Ejecutar `./gradlew clean build`
4. Verificar `docs/BUILD_REPORT.md`

### Para Continuar Desarrollo
1. Implementar Fase 3: ViewModels
2. Crear Fase 4: Pantallas interactivas
3. Completar Fase 5: Ilustraciones
4. Añadir Fase 6: Tests
5. Generar Fase 7: Documentación
6. Publicar Fase 8: APK

---

## 🔍 Búsqueda Rápida

### Por Patrón de Diseño
- **MVVM**: Ver `data/`, `domain/`, `ui/`
- **Repository**: Ver `data/repository/`
- **Use Case**: Ver `domain/usecase/`
- **Composable**: Ver `ui/screens/` y `ui/components/`

### Por Característica
- **Base de Datos**: `data/local/`
- **Temas**: `ui/theme/`
- **Pantallas**: `ui/screens/`
- **Tests**: `src/test/`

### Por Tecnología
- **Room**: `data/local/`
- **Compose**: `ui/`
- **Navigation**: `MainActivity.kt`
- **Coroutines**: `domain/usecase/`

---

## 📞 Notas Técnicas

- **Lenguaje**: Kotlin 1.9.22
- **Framework UI**: Jetpack Compose
- **Diseño**: Material 3
- **Base de Datos**: Room (SQLite)
- **Arquitectura**: MVVM + Repository + Use Case
- **Testing**: JUnit 5 + Mockito
- **Build**: Gradle 8.2.0

---

## 🚀 Estado Actual

**Versión**: 0.2.0 (Alpha)  
**Última Actualización**: 2026-08-23  
**Próximas Acciones**: Implementar Fases 3-4  
**Estimado**: 3-4 horas  

**¡El proyecto está preparado para continuar! Consulta [ESTADO_PROYECTO.md](ESTADO_PROYECTO.md) para más detalles.**

---

**Última revisión**: 2026-08-23  
**Responsable**: Equipo MateKids  
**Licencia**: MIT

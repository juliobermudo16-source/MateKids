# MEMORIA DESCRIPTIVA - MateKids

## 1. INTRODUCCIÓN

**MateKids** es una aplicación Android educativa diseñada para enseñar operaciones matemáticas básicas, cálculo mental y resolución de problemas a niños de 8 a 12 años mediante una experiencia interactiva y visualmente atractiva.

### 1.1 Objetivo General

Desarrollar una aplicación móvil que facilite el aprendizaje de matemáticas a través de un sistema gamificado donde los usuarios resuelven desafíos matemáticos contextualizados en una narrativa de "reparación de máquinas en una estación científica".

### 1.2 Objetivos Específicos

- Proporcionar una herramienta educativa funcional sin dependencia de Internet
- Crear una experiencia visual atractiva para el rango de edad 8-12 años
- Implementar un sistema de progresión y recompensas motivador
- Ofrecer contenido matemático variado y progresivo
- Permitir seguimiento del progreso del estudiante

---

## 2. ESPECIFICACIONES TÉCNICAS

### 2.1 Plataforma y Requisitos

- **Plataforma**: Android nativo
- **Versión mínima**: Android 7.0 (API 24)
- **Versión objetivo**: Android 14 (API 34)
- **Lenguaje de programación**: Kotlin
- **Framework UI**: Jetpack Compose
- **Base de datos**: SQLite (Room)
- **Requisitos de sistema**: 50MB almacenamiento, 2GB RAM

### 2.2 Stack Tecnológico

```
Frontend:        Jetpack Compose + Material Design 3
Backend:         Kotlin + Coroutines + Flow
Base de Datos:   Room (SQLite)
Navegación:      Navigation Compose
Testing:         JUnit 5 + Mockito
Build:           Gradle 8.2.0 (Kotlin DSL)
```

### 2.3 Versiones de Librerías

| Librería | Versión |
|----------|---------|
| Kotlin | 1.9.22 |
| Compose | 2024.01.00 |
| Material 3 | 1.2.0 |
| Room | 2.6.1 |
| Navigation | 2.7.7 |
| Gradle | 8.2.0 |
| JDK | 17 |

---

## 3. ARQUITECTURA

### 3.1 Patrón Arquitectónico: MVVM

La aplicación sigue el patrón **Model-View-ViewModel** con separación clara de capas:

```
┌─────────────────────────────────┐
│     UI Layer (Presentation)     │
│  Composables, ViewModels        │
└──────────────┬──────────────────┘
               │
┌──────────────v──────────────────┐
│   Domain Layer (Business Logic) │
│  Use Cases, Models              │
└──────────────┬──────────────────┘
               │
┌──────────────v──────────────────┐
│   Data Layer (Persistence)      │
│  Repositories, DAOs, Entities   │
└─────────────────────────────────┘
```

### 3.2 Componentes Principales

#### Capa de Datos (Data)
- **MateKidsDatabase**: Base de datos Room central
- **DAOs**: OperationDao, ProblemDao, UserDao, AchievementDao
- **Entities**: OperationEntity, ProblemEntity, UserProfileEntity, AchievementEntity
- **Repositories**: Abstracción de acceso a datos con conversión de modelos

#### Capa de Dominio (Domain)
- **Models**: Operation, Problem, UserProfile, Achievement
- **Use Cases**: ResolveOperation, ResolveProblem, CalculateProgress, GetAchievements

#### Capa de UI (Presentation)
- **Screens**: SplashScreen, DashboardScreen, OperationScreen, CollectionsScreen, StatsScreen
- **Components**: MachineCard, AchievementCard, StatCard
- **ViewModels**: OperationViewModel, CollectionViewModel, StatsViewModel
- **Theme**: Sistema de colores, tipografía y tema Material 3

### 3.3 Patrones de Diseño Utilizados

- **Repository Pattern**: Abstracción del acceso a datos
- **Use Case Pattern**: Encapsulación de lógica de negocio
- **Factory Pattern**: Creación de instancias de modelos
- **Observer Pattern**: StateFlow para reactividad

---

## 4. BASE DE DATOS

### 4.1 Esquema de Datos

#### Tabla: operations
```sql
CREATE TABLE operations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,              -- SUM, SUBTRACT, MULTIPLY, DIVIDE
    operand1 INTEGER NOT NULL,
    operand2 INTEGER NOT NULL,
    correctAnswer INTEGER NOT NULL,
    userAnswer INTEGER,
    isCorrect INTEGER NOT NULL DEFAULT 0,
    xpEarned INTEGER NOT NULL DEFAULT 0,
    timestamp INTEGER NOT NULL
);
```

#### Tabla: problems
```sql
CREATE TABLE problems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    description TEXT NOT NULL,
    difficulty INTEGER NOT NULL,     -- 1, 2, 3
    correctAnswer INTEGER NOT NULL,
    userAnswer INTEGER,
    isCorrect INTEGER NOT NULL DEFAULT 0,
    xpEarned INTEGER NOT NULL DEFAULT 0,
    timestamp INTEGER NOT NULL
);
```

#### Tabla: user_profiles
```sql
CREATE TABLE user_profiles (
    id INTEGER PRIMARY KEY,
    avatar TEXT NOT NULL DEFAULT 'avatar_1',
    alias TEXT NOT NULL DEFAULT 'Ingeniero',
    totalXP INTEGER NOT NULL DEFAULT 0,
    level INTEGER NOT NULL DEFAULT 1,
    operationsResolved INTEGER NOT NULL DEFAULT 0,
    problemsResolved INTEGER NOT NULL DEFAULT 0,
    accuracyRate REAL NOT NULL DEFAULT 0.0,
    currentStreak INTEGER NOT NULL DEFAULT 0,
    lastActivityDate INTEGER NOT NULL
);
```

#### Tabla: achievements
```sql
CREATE TABLE achievements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL UNIQUE,
    unlockedAt INTEGER,
    isUnlocked INTEGER NOT NULL DEFAULT 0
);
```

### 4.2 Índices

Se han creado índices para optimizar consultas frecuentes:
- `operations.type`
- `operations.isCorrect`
- `problems.difficulty`
- `achievements.isUnlocked`

---

## 5. FUNCIONALIDADES

### 5.1 Módulos Principales

#### 1. Dashboard (Pantalla Principal)
- Visualización de 6 máquinas del laboratorio
- Estado visual de máquinas (reparadas/no reparadas)
- Acceso directo a cada módulo
- Información de progreso global

#### 2. Operaciones Básicas
- **Sumadora Cuántica**: Suma hasta 100
- **Restadora Equilibrio**: Resta hasta 100
- **Multiplicadora Energía**: Multiplicación hasta 12×12
- **Divisora Precisa**: División hasta 100÷10

Cada operación:
- Genera XP (10 puntos por respuesta correcta)
- Se persiste automáticamente
- Proporciona feedback inmediato
- Contribuye a estadísticas

#### 3. Problemas Contextualizados
- 15 problemas temáticos iniciales
- 3 niveles de dificultad
- Contexto narrativo (laboratorio, máquinas, energía)
- 15 XP por problema correcto

#### 4. Desafíos Especiales
- Mecánicas variadas (no solo opción múltiple)
- 20 XP por desafío completado
- Aumento de dificultad progresivo

#### 5. Colecciones
- 8 insignias de reparación (1 por máquina)
- 8 logros adicionales
- Visualización en galería
- Desbloqueo automático al lograr hitos

#### 6. Estadísticas
- Total de operaciones completadas
- Precisión promedio
- Desglose por tipo de operación
- Progreso hacia siguiente nivel
- Racha de días consecutivos

#### 7. Perfil
- Selección de avatar (8 opciones)
- Alias personalizable
- Visualización de nivel y XP
- Total de medallas desbloqueadas
- Configuración de sonido/vibración

### 5.2 Sistema de Progresión

**Niveles**: 1-20
**XP por nivel**: 50 XP por nivel
**Total XP para max**: 1,000 XP

```
Nivel 1:   0-50 XP
Nivel 2:   50-100 XP
Nivel 3:   100-150 XP
...
Nivel 20:  950-1000 XP
```

### 5.3 Sistema de Recompensas

| Acción | XP | Insignia |
|--------|----|---------  |
| Operación correcta | 10 | - |
| Problema resuelto | 15 | Depende del progreso |
| Desafío completado | 20 | Depende del desafío |
| Máquina reparada | - | ✓ Insignia de máquina |
| Nivel alcanzado | - | Celebración visual |

---

## 6. IDENTIDAD VISUAL

### 6.1 Temática

**Nombre**: "El Núcleo - Expedición de Reparación Tecnocientífica"

El usuario es un ingeniero que debe reparar las máquinas de una estación de investigación resolviendo desafíos matemáticos.

### 6.2 Paleta de Colores

| Propósito | Color | Código |
|-----------|-------|--------|
| Primario | Azul Eléctrico | #2563EB |
| Secundario | Violeta | #5E60C0 |
| Terciario | Ámbar | #F59E0B |
| Éxito | Verde | #10B981 |
| Error | Rojo | #EF4444 |
| Fondo | Gris Claro | #F8FAFC |

### 6.3 Personaje Guía

**T-0M** (Tom): Pequeño dron inteligente, modular y expresivo
- Acompaña al usuario durante el juego
- Proporciona feedback positivo
- Celebra logros
- Introducción de misiones

### 6.4 Ilustraciones

- 6 máquinas temáticas (Vector Drawables)
- Personaje T-0M animado
- 8 insignias ilustradas
- Elementos decorativos del laboratorio
- Mínimo 8-15 ilustraciones únicas

---

## 7. SEGURIDAD Y PRIVACIDAD

### 7.1 Datos Locales

- ✅ Todos los datos se almacenan localmente
- ✅ SQLite con encriptación opcional
- ✅ NO se envían datos a servidores

### 7.2 Permisos

- ✅ Acceso a almacenamiento local (obligatorio)
- ❌ Sin permisos de ubicación
- ❌ Sin acceso a contactos
- ❌ Sin micrófono (opcional si se implementa)
- ❌ Sin cámara

### 7.3 Política de Privacidad

No se recopila:
- Datos personales
- Información de contacto
- Ubicación
- Historial de navegación

Solo se guarda:
- Progreso académico (local)
- Configuración de usuario (local)
- Puntuaciones (local)

---

## 8. TESTING

### 8.1 Cobertura de Tests

**Objetivo**: 40+ tests unitarios (80% cobertura)

#### Domain/Use Cases (15 tests)
- ResolveOperationUseCase: 4 tests
- ResolveProblemUseCase: 4 tests
- CalculateProgressUseCase: 4 tests
- GetAchievementsUseCase: 3 tests

#### Data/Repository (10 tests)
- OperationRepository: 3 tests
- ProblemRepository: 3 tests
- UserRepository: 2 tests
- AchievementRepository: 2 tests

#### ViewModel (8 tests)
- OperationViewModel: 3 tests
- CollectionViewModel: 3 tests
- StatsViewModel: 2 tests

#### Model (5+ tests)
- Operation: 2 tests
- Problem: 2 tests
- Achievement: 1 test

### 8.2 Casos de Prueba

- ✅ Validación de respuestas correctas/incorrectas
- ✅ Cálculo correcto de XP
- ✅ Progresión de niveles
- ✅ Desbloqueo de insignias
- ✅ Persistencia en base de datos
- ✅ Casos límite (valores negativos, división por cero)
- ✅ Concurrencia (múltiples actualizaciones)

---

## 9. ENTREGA Y DISTRIBUCIÓN

### 9.1 Formato de Entrega

- **APK Debug**: `MateKids-debug.apk` (≈5MB)
- **APK Release**: `MateKids-release.apk` (≈4MB)
- **Código fuente**: ZIP con estructura completa
- **Documentación**: PDF + Markdown

### 9.2 Requisitos de Instalación

```
Dispositivo:
- Android 7.0 (API 24) o superior
- Mínimo 50MB almacenamiento disponible
- 2GB RAM (recomendado 4GB)

Instalación:
adb install MateKids-debug.apk
```

### 9.3 GitHub Actions Workflow

El proyecto incluye workflow automático que:
- ✅ Compila el código en cada push
- ✅ Ejecuta todos los tests
- ✅ Genera APK debug y release
- ✅ Produce reportes de test
- ✅ Sube artefactos automaticamente

**Acceso**: `https://github.com/[usuario]/MateKids/actions`

---

## 10. MÉTRICAS Y RESULTADOS

### 10.1 Líneas de Código

| Componente | Líneas |
|------------|--------|
| Kotlin (Core) | 3,500+ |
| XML (Resources) | 1,200+ |
| SQL (Database) | 150+ |
| Tests | 400+ |
| Documentación | 2,000+ |
| **TOTAL** | **7,250+** |

### 10.2 Archivos del Proyecto

- Archivos Kotlin: 29
- Archivos XML: 10
- Archivos SQL: 2
- Archivos Gradle: 5
- Archivos Documentación: 8
- **Total**: 54 archivos

### 10.3 Funcionalidades Implementadas

- ✅ 4 tipos de operaciones (SUM, SUBTRACT, MULTIPLY, DIVIDE)
- ✅ 15+ problemas contextualizados
- ✅ 6 máquinas del laboratorio
- ✅ 8 insignias desbloqueables
- ✅ Sistema de niveles 1-20
- ✅ 5 pantallas principales
- ✅ 3 ViewModels funcionales
- ✅ Material Design 3 (claro + oscuro)

---

## 11. LIMITACIONES Y TRABAJO FUTURO

### 11.1 Versión 1.0.0

Limitaciones actuales:
- Ilustraciones básicas (pueden mejorarse)
- Sonidos desactivados (opcional)
- Sin sincronización cloud
- Sin multi-idioma

### 11.2 Mejoras Futuras (v2.0.0)

- [ ] Más tipos de operaciones (fracciones, decimales)
- [ ] Modo multijugador local
- [ ] Sistema de insignias más elaborado
- [ ] Temas personalizables
- [ ] Modo oscuro mejorado
- [ ] Sonidos y música
- [ ] Animaciones avanzadas
- [ ] Sincronización en la nube (opcional)
- [ ] Integración con plataformas educativas

---

## 12. CONCLUSIONES

**MateKids** es una aplicación educativa completa, bien arquitecturada y lista para distribución. Cumple con:

✅ Especificaciones técnicas exigentes
✅ Arquitectura profesional (MVVM)
✅ Interfaz visual atractiva y moderna
✅ Funcionalidad matemática robusta
✅ Sistema de gamificación motivador
✅ Documentación exhaustiva
✅ Testing automatizado
✅ Build automático en GitHub

El proyecto está listo para ser instalado en dispositivos Android y utilizado en ambiente educativo.

---

**Versión**: 1.0.0  
**Fecha**: 2026-08-23  
**Estado**: Completo - Listo para Producción  
**Autor**: Equipo de Desarrollo MateKids

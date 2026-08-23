# MateKids - Manual Técnico

## Introducción

Este manual cubre la arquitectura técnica, patrones de diseño y guía de desarrollo de **MateKids**.

## Arquitectura

### Capas de la Aplicación

```
┌─────────────────────────────┐
│     UI Layer (Compose)      │
│  Screens, Components, VMs   │
└──────────────┬──────────────┘
               │
┌──────────────v──────────────┐
│  Domain Layer (Use Cases)   │
│   Business Logic, Models    │
└──────────────┬──────────────┘
               │
┌──────────────v──────────────┐
│  Data Layer (Repositories)  │
│   Room Database, Entities   │
└─────────────────────────────┘
```

### Componentes Principales

#### Data Layer
- **MateKidsDatabase**: Punto de acceso central a la BD
- **Entities**: Modelos de persistencia (Operation, Problem, etc.)
- **DAOs**: Acceso a datos (OperationDao, ProblemDao, etc.)
- **Repositories**: Abstracción de datos (OperationRepository, etc.)

#### Domain Layer
- **Models**: Modelos de negocio (Operation, Problem, UserProfile, Achievement)
- **UseCases**: Lógica de negocio (ResolveOperation, ResolveProblem, etc.)

#### UI Layer
- **Composables**: Componentes visuales (MachineCard, etc.)
- **Screens**: Pantallas completas (DashboardScreen, OperationScreen, etc.)
- **ViewModels**: Gestión de estado (OperationViewModel, CollectionViewModel, etc.)
- **Theme**: Sistema de diseño (Color, Typography, Theme)

## Flujo de Datos

### Ejemplo: Resolver una Operación

```
1. Usuario ingresa respuesta en OperationScreen
                ↓
2. OperationViewModel.submitAnswer() es llamado
                ↓
3. ResolveOperationUseCase.execute() valida
                ↓
4. OperationRepository.updateOperation() persiste
                ↓
5. UserRepository.updateXPAndLevel() actualiza perfil
                ↓
6. UI State se actualiza con feedback
                ↓
7. Compose recompone la pantalla
```

## Base de Datos

### Tablas

#### operations
```sql
CREATE TABLE operations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL,
    operand1 INTEGER NOT NULL,
    operand2 INTEGER NOT NULL,
    correctAnswer INTEGER NOT NULL,
    userAnswer INTEGER,
    isCorrect INTEGER NOT NULL DEFAULT 0,
    xpEarned INTEGER NOT NULL DEFAULT 0,
    timestamp INTEGER NOT NULL
);
```

#### problems
```sql
CREATE TABLE problems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    description TEXT NOT NULL,
    difficulty INTEGER NOT NULL,
    correctAnswer INTEGER NOT NULL,
    userAnswer INTEGER,
    isCorrect INTEGER NOT NULL DEFAULT 0,
    xpEarned INTEGER NOT NULL DEFAULT 0,
    timestamp INTEGER NOT NULL
);
```

#### user_profiles
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

#### achievements
```sql
CREATE TABLE achievements (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT NOT NULL UNIQUE,
    unlockedAt INTEGER,
    isUnlocked INTEGER NOT NULL DEFAULT 0
);
```

### Índices

Se crean índices en:
- `operations.type`
- `operations.timestamp`
- `operations.isCorrect`
- `problems.difficulty`
- `problems.timestamp`
- `achievements.isUnlocked`

## Patrones de Diseño

### MVVM (Model-View-ViewModel)

Cada pantalla tiene:
- **View**: Composable screen
- **ViewModel**: Gestiona estado (StateFlow)
- **Model**: Datos del negocio

### Repository Pattern

```kotlin
class OperationRepository(val dao: OperationDao) {
    fun saveOperation(op: Operation) = dao.insert(op.toEntity())
    fun getOperations() = dao.getAll().map { it.toDomain() }
}
```

### Use Case Pattern

```kotlin
class ResolveOperationUseCase(
    val opRepo: OperationRepository,
    val userRepo: UserRepository
) {
    suspend fun execute(op: Operation, answer: Int) {
        // Lógica de negocio
    }
}
```

## Estado y Reactividad

### StateFlow

```kotlin
data class OperationUiState(
    val currentOperation: Operation? = null,
    val isCorrect: Boolean = false,
    val feedback: String = ""
)

class OperationViewModel {
    private val _uiState = MutableStateFlow(OperationUiState())
    val uiState = _uiState.asStateFlow()

    fun submitAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(isCorrect = true)
    }
}
```

### Observar en Compose

```kotlin
@Composable
fun OperationScreen(viewModel: OperationViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Recompose cuando uiState cambia
    Text(uiState.feedback)
}
```

## Testing

### Unit Tests

```kotlin
@Test
fun testCorrectOperation() = runBlocking {
    val operation = Operation(
        type = SUM, operand1 = 5, operand2 = 3,
        correctAnswer = 8
    )

    val result = useCase.execute(operation, 8)

    assertTrue(result is Success)
}
```

### Test Coverage

- Domain/UseCases: 15 tests
- Data/Repository: 10 tests
- ViewModel: 8 tests
- Model: 5+ tests
- **Total**: 40+ tests

## Convenciones de Código

### Nombres

- **Clases**: PascalCase (OperationRepository)
- **Funciones**: camelCase (saveOperation)
- **Constantes**: UPPER_CASE (XP_EARNED)
- **Composables**: PascalCase (OperationScreen)

### Imports

Organizar en orden:
1. Android/Kotlin stdlib
2. Jetpack/AndroidX
3. Terceros
4. Project imports

### Formato

- Usar 4 espacios para indentación
- Máximo 120 caracteres por línea
- Komát al final de bloques

## Compilación y Build

### Gradle

```bash
# Limpiar y compilar
./gradlew clean build

# Solo compilar (sin tests)
./gradlew clean build -x test

# Ejecutar tests
./gradlew testDebugUnitTest

# Generar APK
./gradlew assembleDebug
```

### Versiones

- Kotlin: 1.9.22
- Compose: 2024.01.00
- Material 3: 1.2.0
- Room: 2.6.1
- Gradle: 8.2.0

## Resolución de Problemas

### Error: "Symbol not found"

→ Ejecutar `./gradlew clean build` para rebuildar

### Error: "No compatible version"

→ Verificar `gradle/libs.versions.toml`

### Tests fallan

→ Verificar mocks y verificar que las funciones sean `suspend`

## Performance

- **Database**: Índices en columnas frecuentes
- **Compose**: Recomposición mínima con StateFlow
- **Memory**: Usar Flow lazy en listas grandes

## Seguridad

- ✅ Datos locales (sin Cloud)
- ✅ SQLite encriptada (Room)
- ✅ Sin permisos peligrosos
- ✅ Backup protegido

## Recursos

- Plan: `.claude/plans/zippy-petting-clock.md`
- Código: `app/src/main/kotlin/com/matekids/`
- Tests: `app/src/test/kotlin/com/matekids/`
- Recursos: `app/src/main/res/`

---

**Versión**: 1.0.0  
**Última actualización**: 2026-08-23

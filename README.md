# MateKids — Aplicación Android Educativa de Matemáticas

**MateKids** es una aplicación Android educativa para niños de 8 a 12 años diseñada para enseñar operaciones matemáticas básicas, cálculo mental y resolución de problemas a través de una experiencia interactiva y visualmente atractiva.

## Características Principales

- 🎮 **Experiencia Inmersiva**: Juego de expedición para reparar la estación "El Núcleo"
- 🤖 **Personaje Guía**: T-0M, un dron inteligente que acompaña al usuario
- 📊 **Operaciones Matemáticas**: Suma, resta, multiplicación y división
- 🧩 **Problemas Contextualizados**: Desafíos en el contexto del laboratorio
- 🏆 **Gamificación**: Sistema de niveles, XP y colecciones de insignias
- 📱 **Offline**: Funciona completamente sin conexión a Internet
- 🎨 **Diseño Visual**: Identidad visual única con Material 3 y Compose

## Requisitos

- Android 7.0 (API 24) o superior
- JDK 17 o superior
- Gradle 8.2.0 o superior
- Kotlin 1.9.22

## Instalación y Compilación

### Clonar el repositorio
```bash
git clone https://github.com/tuusuario/MateKids.git
cd MateKids
```

### Compilar y ejecutar
```bash
./gradlew clean
./gradlew build
./gradlew installDebug
```

### Ejecutar pruebas unitarias
```bash
./gradlew testDebugUnitTest
```

### Verificar código
```bash
./gradlew lintDebug
```

### Generar APK
```bash
./gradlew assembleDebug
./gradlew assembleRelease
```

## Estructura del Proyecto

```
MateKids/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/matekids/
│   │   │   │   ├── data/
│   │   │   │   ├── domain/
│   │   │   │   ├── ui/
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/
│   │   └── test/
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Arquitectura

MateKids utiliza una arquitectura limpia con separación de capas:

- **Data Layer**: Acceso a datos mediante Room (SQLite)
- **Domain Layer**: Lógica empresarial (Use Cases)
- **UI Layer**: Interfaz de usuario con Jetpack Compose

### Tecnologías Utilizadas

- **Kotlin**: Lenguaje de programación principal
- **Jetpack Compose**: Framework para UI reactiva
- **Material 3**: Diseño visual moderno
- **Room**: Base de datos local SQLite
- **Navigation Compose**: Gestión de navegación
- **Coroutines + Flow**: Operaciones asincrónicas
- **JUnit + Mockito**: Testing unitario

## Módulos Principales

1. **Operaciones Básicas**: Suma, resta, multiplicación, división
2. **Cálculo Mental**: Desafíos de cálculo rápido
3. **Resolución de Problemas**: Problemas contextualizados
4. **Desafíos Especiales**: Retos únicos con mecánicas diferentes
5. **Colecciones**: Galerías de insignias y logros
6. **Estadísticas**: Visualización del progreso
7. **Perfil**: Configuración del usuario

## Gamificación

- **Niveles**: 1-20 (se incrementan cada 50 XP)
- **XP**: 10 por operación correcta, 15 por problema, 20 por desafío
- **Insignias**: 8 insignias de reparación de máquinas
- **Colecciones**: Piezas del reactor desbloqueables

## Testing

El proyecto incluye 30-40 tests unitarios cobriendo:

- **Domain/UseCases**: Lógica de negocio
- **Data/Repository**: Persistencia
- **ViewModels**: Estados de UI

### Ejecutar tests
```bash
./gradlew testDebugUnitTest
```

## Documentación

- [MEMORIA_DESCRIPTIVA.md](docs/MEMORIA_DESCRIPTIVA.md) - Descripción técnica y diseño
- [MANUAL_USUARIO.md](docs/MANUAL_USUARIO.md) - Guía para usuarios
- [MANUAL_TECNICO.md](docs/MANUAL_TECNICO.md) - Guía para desarrolladores
- [BASE_DE_DATOS.md](docs/BASE_DE_DATOS.md) - Schema de base de datos
- [BUILD_REPORT.md](docs/BUILD_REPORT.md) - Reporte de compilación

## Versión

- **Versión Actual**: 1.0.0
- **API Mínima**: 24 (Android 7.0)
- **API Objetivo**: 34 (Android 14)

## Autores

Desarrollo por equipo profesional de desarrollo Android educativo.

## Licencia

Este proyecto está licenciado bajo la licencia MIT.

## Créditos

- Diseño visual y conceptual basado en temática de "El Núcleo" y personaje T-0M
- Ilustraciones vectoriales temáticas de ciencia ficción amigable
- Contenido educativo adaptado para niños de 8-12 años

---

**MateKids** - Reparando El Núcleo, Aprendiendo Matemáticas 🚀

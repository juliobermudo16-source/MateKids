# Base de datos

MateKids guarda todo en **SQLite local mediante Room**. No hay servidor, ni
cuentas, ni sincronización: la aplicación funciona entera sin conexión.

- **Fichero**: `matekids.db`
- **Versión del esquema**: 2
- **Definición**: `data/local/MateKidsDatabase.kt`

## Privacidad

No se almacena ningún dato personal: ni nombre real, ni correo, ni teléfono,
ni fecha de nacimiento, ni ubicación. Lo único que identifica al niño es un
**apodo que él elige** y que puede dejar en blanco.

Los datos **no salen del dispositivo**. Están excluidos de la copia de
seguridad en la nube (`res/xml/backup_rules.xml` y
`res/xml/data_extraction_rules.xml`); solo se permite la transferencia directa
entre móviles, que es local, para no perder el avance al cambiar de teléfono.

## Tablas

### `lesson_progress`

El avance por el camino. Es la tabla que sostiene la experiencia: de ella
salen las lecciones desbloqueadas y las estrellas.

| Columna | Tipo | Descripción |
|---|---|---|
| `lessonId` | TEXT (PK) | Id de la lección, p. ej. `u3-l2` |
| `unitId` | TEXT | Unidad a la que pertenece |
| `bestCorrect` | INTEGER | Mejor número de aciertos a la primera |
| `totalExercises` | INTEGER | Ejercicios de la lección |
| `isPerfect` | INTEGER | 1 si alguna vez se completó sin fallar |
| `timesPlayed` | INTEGER | Veces que se ha jugado |
| `completedAt` | INTEGER | Última vez completada (epoch ms) |

La clave es el id de la lección, así que **repetirla actualiza su fila**, no
crea otra. Se conserva siempre el mejor resultado: volver a jugarla y hacerlo
peor no borra la marca anterior ni quita la estrella.

### `user_profiles`

Perfil del niño. Una sola fila (`id = 1`).

| Columna | Tipo | Descripción |
|---|---|---|
| `id` | INTEGER (PK) | Siempre 1 |
| `avatar` | TEXT | Id del avatar elegido (`avatar_1`…`avatar_8`) |
| `alias` | TEXT | Apodo elegido |
| `totalXP` | INTEGER | Experiencia acumulada |
| `level` | INTEGER | Nivel alcanzado |
| `operationsResolved` | INTEGER | Operaciones resueltas |
| `problemsResolved` | INTEGER | Problemas resueltos |
| `accuracyRate` | REAL | Porcentaje de acierto |
| `currentStreak` | INTEGER | Días seguidos |
| `lastActivityDate` | INTEGER | Última actividad (epoch ms) |

Mientras el niño no termina el alta la tabla está vacía y Room emite `null`;
el repositorio devuelve entonces un perfil por defecto.

### `achievements`, `operations`, `problems`

Tablas de la primera versión del proyecto. Siguen creándose y tienen sus
repositorios, pero la experiencia actual gira alrededor de `lesson_progress`.
Se conservan para el historial de ejercicios y los logros.

## Contenido del camino

Las 8 unidades y sus 36 lecciones **no están en la base de datos**: se definen
en código, en `domain/model/MathCurriculum.kt`. Es contenido fijo que no
cambia en tiempo de ejecución, así que ponerlo en una tabla solo añadiría una
migración cada vez que se ajuste una lección.

Lo que sí se guarda es el avance del niño sobre ese camino.

## Migraciones

La base está configurada con `fallbackToDestructiveMigration()`: al subir de
versión se recrea desde cero y **se pierde el avance**. Es aceptable mientras
la aplicación está en desarrollo, pero antes de publicarla habría que escribir
migraciones reales para no borrarle el progreso a quien ya la use.

## Scripts

- `database/schema.sql` — estructura de las tablas
- `database/sample_data.sql` — datos de ejemplo

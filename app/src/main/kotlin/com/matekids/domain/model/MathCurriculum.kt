package com.matekids.domain.model

/**
 * El camino de MateKids: unidades ordenadas por dificultad creciente, pensadas
 * para ninos de 9 a 12 anos.
 *
 * La progresion es la habitual del segundo ciclo de primaria: primero sumar y
 * restar con numeros pequenos, despues llevadas, luego las tablas, la division
 * exacta y por ultimo problemas en contexto, que exigen decidir que operacion
 * usar y no solo calcular.
 */
object MathCurriculum {

    fun path(): LearningPath = LearningPath(
        units = listOf(
            unit(
                id = "u1",
                order = 1,
                title = "Sumas hasta 20",
                subtitle = "Empieza por lo básico",
                skill = Skill.SUMAR,
                lessons = listOf(
                    "Primeros pasos" to 1,
                    "Sumar en la recta" to 1,
                    "Completar hasta 10" to 1,
                    "Sumas rápidas" to 2
                )
            ),
            unit(
                id = "u2",
                order = 2,
                title = "Restas hasta 20",
                subtitle = "Quitar y comparar",
                skill = Skill.RESTAR,
                lessons = listOf(
                    "Quitar poco a poco" to 1,
                    "¿Cuánto falta?" to 1,
                    "Restas sin llevadas" to 1,
                    "Restas rápidas" to 2
                )
            ),
            unit(
                id = "u3",
                order = 3,
                title = "Hasta el 100",
                subtitle = "Sumas y restas con llevadas",
                skill = Skill.SUMAR_RESTAR,
                lessons = listOf(
                    "Decenas enteras" to 2,
                    "Sumas con llevadas" to 2,
                    "Restas con llevadas" to 2,
                    "Mezcla de las dos" to 2,
                    "Desafío del 100" to 3
                )
            ),
            unit(
                id = "u4",
                order = 4,
                title = "Tablas de multiplicar",
                subtitle = "Del 2 al 9",
                skill = Skill.MULTIPLICAR,
                lessons = listOf(
                    "Tablas del 2 y del 5" to 1,
                    "Tablas del 3 y del 4" to 1,
                    "Tablas del 6 y del 7" to 2,
                    "Tablas del 8 y del 9" to 2,
                    "Todas mezcladas" to 2
                )
            ),
            unit(
                id = "u5",
                order = 5,
                title = "Multiplicar más",
                subtitle = "Números de dos cifras",
                skill = Skill.MULTIPLICAR,
                lessons = listOf(
                    "Por 10 y por 100" to 2,
                    "Dos cifras por una" to 3,
                    "Productos grandes" to 3,
                    "Reto de multiplicación" to 3
                )
            ),
            unit(
                id = "u6",
                order = 6,
                title = "División exacta",
                subtitle = "Repartir en partes iguales",
                skill = Skill.DIVIDIR,
                lessons = listOf(
                    "Repartir entre 2 y 5" to 1,
                    "La mitad y el doble" to 1,
                    "Dividir entre 3 y 4" to 2,
                    "Dividir entre 6 y 9" to 2,
                    "Todas mezcladas" to 3
                )
            ),
            unit(
                id = "u7",
                order = 7,
                title = "Cálculo mental",
                subtitle = "Sin lápiz ni papel",
                skill = Skill.CALCULO_MENTAL,
                lessons = listOf(
                    "Trucos para sumar" to 2,
                    "Trucos para restar" to 2,
                    "Operaciones mezcladas" to 3,
                    "Contrarreloj" to 3
                )
            ),
            unit(
                id = "u8",
                order = 8,
                title = "Problemas",
                subtitle = "Matemáticas de la vida diaria",
                skill = Skill.PROBLEMAS,
                lessons = listOf(
                    "En la tienda" to 1,
                    "Repartir con amigos" to 2,
                    "Tiempo y distancias" to 2,
                    "Dos pasos" to 3,
                    "Reto final" to 3
                )
            )
        )
    )

    /** Monta una unidad numerando sus lecciones y generando sus ids. */
    private fun unit(
        id: String,
        order: Int,
        title: String,
        subtitle: String,
        skill: Skill,
        lessons: List<Pair<String, Int>>
    ) = MathUnit(
        id = id,
        order = order,
        title = title,
        subtitle = subtitle,
        skill = skill,
        lessons = lessons.mapIndexed { index, (lessonTitle, difficulty) ->
            Lesson(
                id = "$id-l${index + 1}",
                unitId = id,
                index = index,
                title = lessonTitle,
                skill = skill,
                difficulty = difficulty
            )
        }
    )
}

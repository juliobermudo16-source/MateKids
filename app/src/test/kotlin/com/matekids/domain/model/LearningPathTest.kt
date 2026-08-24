package com.matekids.domain.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LearningPathTest {

    private val path = MathCurriculum.path()
    private val lessons = path.lessonsInOrder()

    @Test
    fun `el camino tiene contenido suficiente para varias sesiones`() {
        // La especificacion prohibe entregar la app con dos niveles de muestra.
        assertTrue(path.units.size >= 6, "solo hay ${path.units.size} unidades")
        assertTrue(lessons.size >= 30, "solo hay ${lessons.size} lecciones")
    }

    @Test
    fun `cada leccion tiene un id unico`() {
        assertEquals(lessons.size, lessons.map { it.id }.distinct().size)
    }

    @Test
    fun `la primera leccion esta abierta desde el principio`() {
        val progress = PathProgress()
        assertEquals(LessonState.AVAILABLE, path.stateOf(lessons.first(), progress))
    }

    @Test
    fun `el resto del camino empieza bloqueado`() {
        val progress = PathProgress()
        lessons.drop(1).forEach {
            assertEquals(LessonState.LOCKED, path.stateOf(it, progress), "abierta de mas: ${it.id}")
        }
    }

    @Test
    fun `completar una leccion abre la siguiente`() {
        val progress = PathProgress(completed = setOf(lessons[0].id))
        assertEquals(LessonState.AVAILABLE, path.stateOf(lessons[1], progress))
        assertEquals(LessonState.LOCKED, path.stateOf(lessons[2], progress))
    }

    @Test
    fun `una leccion completada se puede repetir`() {
        val progress = PathProgress(completed = setOf(lessons[0].id))
        assertEquals(LessonState.COMPLETED, path.stateOf(lessons[0], progress))
    }

    @Test
    fun `terminar sin fallos se marca como perfecta`() {
        val progress = PathProgress(
            completed = setOf(lessons[0].id),
            perfect = setOf(lessons[0].id)
        )
        assertEquals(LessonState.PERFECT, path.stateOf(lessons[0], progress))
    }

    @Test
    fun `el desbloqueo cruza el limite entre unidades`() {
        val primeraUnidad = path.units.first()
        val ultimaDeLaUnidad = primeraUnidad.lessons.last()
        val primeraDeLaSiguiente = path.units[1].lessons.first()

        val antes = PathProgress()
        assertFalse(path.isUnlocked(primeraDeLaSiguiente, antes))

        val despues = PathProgress(
            completed = primeraUnidad.lessons.map { it.id }.toSet()
        )
        assertTrue(path.isUnlocked(primeraDeLaSiguiente, despues))
        assertEquals(ultimaDeLaUnidad.unitId, primeraUnidad.id)
    }

    @Test
    fun `la siguiente leccion es la primera sin completar`() {
        val progress = PathProgress(completed = setOf(lessons[0].id, lessons[1].id))
        assertEquals(lessons[2].id, path.nextLesson(progress)?.id)
    }

    @Test
    fun `sin avance la siguiente leccion es la primera`() {
        assertEquals(lessons.first().id, path.nextLesson(PathProgress())?.id)
    }

    @Test
    fun `al terminarlo todo ya no queda siguiente leccion`() {
        val progress = PathProgress(completed = lessons.map { it.id }.toSet())
        assertNull(path.nextLesson(progress))
        assertEquals(1f, path.overallProgress(progress))
    }

    @Test
    fun `el progreso global refleja lo completado`() {
        assertEquals(0f, path.overallProgress(PathProgress()))
        val mitad = lessons.take(lessons.size / 2).map { it.id }.toSet()
        val progreso = path.overallProgress(PathProgress(completed = mitad))
        assertTrue(progreso in 0.4f..0.6f, "progreso inesperado: $progreso")
    }

    @Test
    fun `el progreso por unidad es independiente`() {
        val unidad = path.units.first()
        val progress = PathProgress(completed = setOf(unidad.lessons.first().id))
        val esperado = 1f / unidad.lessons.size
        assertEquals(esperado, path.unitProgress(unidad, progress))
        assertEquals(0f, path.unitProgress(path.units[1], progress))
    }

    @Test
    fun `dentro de cada unidad la dificultad nunca retrocede`() {
        path.units.forEach { unit ->
            val niveles = unit.lessons.sortedBy { it.index }.map { it.difficulty }
            assertEquals(niveles.sorted(), niveles, "la dificultad baja dentro de ${unit.id}")
        }
    }

    @Test
    fun `al repetir destreza se retoma donde se dejo`() {
        // Entre unidades si puede bajar: estrenar las tablas de multiplicar
        // despues de dominar las sumas con llevadas debe empezar suave. Lo que
        // no vale es que una destreza ya trabajada vuelva atras.
        path.units.groupBy { it.skill }
            .filterValues { it.size > 1 }
            .forEach { (skill, unidades) ->
                val topes = unidades.sortedBy { it.order }
                    .map { unit -> unit.lessons.maxOf { it.difficulty } }
                assertEquals(topes.sorted(), topes, "la destreza $skill retrocede")
            }
    }

    @Test
    fun `el camino llega a la dificultad maxima`() {
        val tope = path.lessonsInOrder().maxOf { it.difficulty }
        assertEquals(3, tope, "el camino nunca alcanza el nivel mas alto")
    }

    @Test
    fun `se puede localizar una leccion por su id`() {
        assertNotNull(path.findLesson(lessons.first().id))
        assertNull(path.findLesson("no-existe"))
    }

    @Test
    fun `los problemas llegan al final del camino`() {
        // Resolver problemas exige decidir la operacion, no solo calcular.
        assertEquals(Skill.PROBLEMAS, path.units.maxBy { it.order }.skill)
    }
}

package com.matekids.data.repository

import com.matekids.data.local.dao.LessonProgressDao
import com.matekids.data.local.entity.LessonProgressEntity
import com.matekids.domain.model.MathCurriculum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** DAO en memoria: para probar las reglas no hace falta levantar Room. */
private class FakeLessonProgressDao : LessonProgressDao {
    val filas = MutableStateFlow<Map<String, LessonProgressEntity>>(emptyMap())

    override fun observeAll(): Flow<List<LessonProgressEntity>> =
        filas.map { it.values.toList() }

    override suspend fun findById(lessonId: String): LessonProgressEntity? = filas.value[lessonId]

    override suspend fun save(progress: LessonProgressEntity) {
        filas.value = filas.value + (progress.lessonId to progress)
    }

    override suspend fun completedCount(): Int = filas.value.size

    override suspend fun clear() {
        filas.value = emptyMap()
    }
}

class PathProgressRepositoryTest {

    private lateinit var dao: FakeLessonProgressDao
    private lateinit var repository: PathProgressRepository

    private val leccion = MathCurriculum.path().lessonsInOrder().first()
    private val siguiente = MathCurriculum.path().lessonsInOrder()[1]

    @Before
    fun setup() {
        dao = FakeLessonProgressDao()
        repository = PathProgressRepository(dao)
    }

    @Test
    fun `al empezar no hay nada completado`() = runTest {
        val progress = repository.observeProgress().first()
        assertTrue(progress.completed.isEmpty())
        assertTrue(progress.perfect.isEmpty())
    }

    @Test
    fun `terminar una leccion la deja completada`() = runTest {
        repository.recordCompletion(leccion, correct = 6, total = 8)

        val progress = repository.observeProgress().first()
        assertTrue(progress.isCompleted(leccion.id))
        assertTrue(leccion.id !in progress.perfect, "no deberia ser perfecta con 6 de 8")
    }

    @Test
    fun `acertarlo todo la marca como perfecta`() = runTest {
        repository.recordCompletion(leccion, correct = 8, total = 8)

        val progress = repository.observeProgress().first()
        assertTrue(leccion.id in progress.perfect)
    }

    @Test
    fun `repetir una leccion no duplica la fila`() = runTest {
        repository.recordCompletion(leccion, correct = 5, total = 8)
        repository.recordCompletion(leccion, correct = 7, total = 8)

        assertEquals(1, repository.completedCount())
        assertEquals(2, dao.filas.value.getValue(leccion.id).timesPlayed)
    }

    @Test
    fun `se conserva el mejor resultado aunque despues vaya peor`() = runTest {
        repository.recordCompletion(leccion, correct = 8, total = 8)
        repository.recordCompletion(leccion, correct = 2, total = 8)

        val fila = dao.filas.value.getValue(leccion.id)
        assertEquals(8, fila.bestCorrect, "se perdio la mejor marca")
    }

    @Test
    fun `una leccion perfecta no deja de serlo al repetirla peor`() = runTest {
        repository.recordCompletion(leccion, correct = 8, total = 8)
        repository.recordCompletion(leccion, correct = 3, total = 8)

        val progress = repository.observeProgress().first()
        assertTrue(leccion.id in progress.perfect, "perdio la marca de perfecta")
    }

    @Test
    fun `el avance guardado abre la siguiente leccion`() = runTest {
        val path = MathCurriculum.path()
        repository.recordCompletion(leccion, correct = 8, total = 8)

        val progress = repository.observeProgress().first()
        assertTrue(path.isUnlocked(siguiente, progress))
    }

    @Test
    fun `reiniciar borra todo el avance`() = runTest {
        repository.recordCompletion(leccion, correct = 8, total = 8)
        repository.reset()

        assertEquals(0, repository.completedCount())
        assertTrue(repository.observeProgress().first().completed.isEmpty())
    }

    @Test
    fun `cada leccion guarda su unidad`() = runTest {
        repository.recordCompletion(leccion, correct = 4, total = 8)
        assertEquals(leccion.unitId, dao.filas.value.getValue(leccion.id).unitId)
    }
}

package com.matekids.ui.viewmodel

import com.matekids.data.repository.PathProgressRepository
import com.matekids.data.repository.UserRepository
import com.matekids.domain.model.MathCurriculum
import com.matekids.util.FakeLessonProgressDao
import com.matekids.util.FakeUserDao
import com.matekids.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var lessonDao: FakeLessonProgressDao
    private lateinit var userDao: FakeUserDao
    private lateinit var progressRepository: PathProgressRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: ProfileViewModel

    private val path = MathCurriculum.path()

    @Before
    fun setup() {
        lessonDao = FakeLessonProgressDao()
        userDao = FakeUserDao()
        progressRepository = PathProgressRepository(lessonDao)
        userRepository = UserRepository(userDao)
    }

    /** Se crea despues de preparar los datos: el estado se lee al construirlo. */
    private fun crearViewModel() {
        viewModel = ProfileViewModel(userRepository, progressRepository)
    }

    @Test
    fun `sin avanzar nada el progreso esta a cero`() = runTest {
        crearViewModel()

        val state = viewModel.uiState.value
        assertEquals(0, state.lessonsCompleted)
        assertEquals(0, state.perfectLessons)
        assertEquals(0f, state.overallProgress)
    }

    @Test
    fun `el total de lecciones es el del camino completo`() = runTest {
        crearViewModel()
        assertEquals(path.lessonsInOrder().size, viewModel.uiState.value.lessonsTotal)
    }

    /** El fallo que se veia en pantalla: el avance no llegaba al perfil. */
    @Test
    fun `las lecciones terminadas se reflejan en el progreso`() = runTest {
        val leccion = path.lessonsInOrder().first()
        progressRepository.recordCompletion(leccion, correct = 5, total = 8)

        crearViewModel()

        assertEquals(1, viewModel.uiState.value.lessonsCompleted)
    }

    @Test
    fun `las lecciones perfectas se cuentan aparte`() = runTest {
        val lecciones = path.lessonsInOrder()
        progressRepository.recordCompletion(lecciones[0], correct = 8, total = 8)
        progressRepository.recordCompletion(lecciones[1], correct = 4, total = 8)

        crearViewModel()

        val state = viewModel.uiState.value
        assertEquals(2, state.lessonsCompleted)
        assertEquals(1, state.perfectLessons, "solo una fue perfecta")
    }

    @Test
    fun `el avance se reparte por unidades`() = runTest {
        val primeraUnidad = path.units.first()
        progressRepository.recordCompletion(primeraUnidad.lessons[0], correct = 8, total = 8)

        crearViewModel()

        val state = viewModel.uiState.value
        assertEquals(path.units.size, state.units.size)
        assertEquals(1, state.units.first().completed)
        assertEquals(0, state.units[1].completed, "no deberia avanzar otra unidad")
    }

    @Test
    fun `una unidad entera cuenta como terminada`() = runTest {
        val unidad = path.units.first()
        unidad.lessons.forEach { progressRepository.recordCompletion(it, 8, 8) }

        crearViewModel()

        val state = viewModel.uiState.value
        assertEquals(1, state.unitsFinished)
        assertTrue(state.units.first().isFinished)
    }

    /** El otro fallo: restablecer se quedaba colgado y no borraba nada. */
    @Test
    fun `restablecer borra el avance del camino`() = runTest {
        path.lessonsInOrder().take(3).forEach {
            progressRepository.recordCompletion(it, correct = 8, total = 8)
        }
        crearViewModel()
        assertEquals(3, viewModel.uiState.value.lessonsCompleted)

        viewModel.resetProgress()

        assertEquals(0, viewModel.uiState.value.lessonsCompleted)
        assertEquals(0, progressRepository.completedCount())
    }

    @Test
    fun `restablecer conserva el apodo y el avatar`() = runTest {
        userRepository.createProfile(alias = "Ana", avatar = "avatar_5")
        progressRepository.recordCompletion(path.lessonsInOrder().first(), 8, 8)
        crearViewModel()

        viewModel.resetProgress()

        val state = viewModel.uiState.value
        assertEquals("Ana", state.alias)
        assertEquals("avatar_5", state.avatarId)
    }

    @Test
    fun `restablecer pone el nivel y los XP a cero`() = runTest {
        userRepository.createProfile(alias = "Ana", avatar = "avatar_1")
        userRepository.updateXPAndLevel(xp = 320, level = 7)
        crearViewModel()

        viewModel.resetProgress()

        val state = viewModel.uiState.value
        assertEquals(0L, state.totalXP)
        assertEquals(1, state.level)
    }

    @Test
    fun `cambiar el apodo lo guarda`() = runTest {
        userRepository.createProfile(alias = "Ana", avatar = "avatar_1")
        crearViewModel()

        viewModel.updateAlias("Lucía")

        assertEquals("Lucía", viewModel.uiState.value.alias)
    }

    @Test
    fun `un apodo en blanco no deja el perfil sin nombre`() = runTest {
        userRepository.createProfile(alias = "Ana", avatar = "avatar_1")
        crearViewModel()

        viewModel.updateAlias("   ")

        assertEquals("Explorador", viewModel.uiState.value.alias)
    }

    @Test
    fun `cambiar el avatar lo guarda`() = runTest {
        userRepository.createProfile(alias = "Ana", avatar = "avatar_1")
        crearViewModel()

        viewModel.updateAvatar("avatar_7")

        assertEquals("avatar_7", viewModel.uiState.value.avatarId)
    }

    @Test
    fun `el mensaje de exito se puede cerrar`() = runTest {
        userRepository.createProfile(alias = "Ana", avatar = "avatar_1")
        crearViewModel()
        viewModel.updateAlias("Lucía")
        assertTrue(viewModel.uiState.value.successMessage != null)

        viewModel.dismissMessage()

        assertEquals(null, viewModel.uiState.value.successMessage)
    }
}

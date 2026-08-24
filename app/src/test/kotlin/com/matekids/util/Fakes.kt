package com.matekids.util

import com.matekids.data.local.dao.LessonProgressDao
import com.matekids.data.local.dao.UserDao
import com.matekids.data.local.entity.LessonProgressEntity
import com.matekids.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Dobles en memoria de los DAO.
 *
 * Permiten probar repositorios y ViewModels con las reglas de verdad, sin
 * levantar Room ni depender de un dispositivo.
 */
class FakeLessonProgressDao : LessonProgressDao {
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

class FakeUserDao : UserDao {
    /** null mientras no se ha creado el perfil, igual que hace Room. */
    val perfil = MutableStateFlow<UserProfileEntity?>(null)

    override suspend fun insertUser(user: UserProfileEntity) {
        perfil.value = user
    }

    override suspend fun updateUser(user: UserProfileEntity) {
        perfil.value = user
    }

    override fun getUserProfile(): Flow<UserProfileEntity?> = perfil

    override suspend fun getUserProfileSync(): UserProfileEntity? = perfil.value

    /** Suma los XP indicados, como la consulta real. */
    override suspend fun updateXPAndLevel(xp: Long, level: Int) {
        perfil.value = perfil.value?.copy(
            totalXP = (perfil.value?.totalXP ?: 0) + xp,
            level = level
        )
    }

    override suspend fun incrementOperationsResolved() {
        perfil.value = perfil.value?.let { it.copy(operationsResolved = it.operationsResolved + 1) }
    }

    override suspend fun incrementProblemsResolved() {
        perfil.value = perfil.value?.let { it.copy(problemsResolved = it.problemsResolved + 1) }
    }

    override suspend fun updateCurrentStreak(streak: Int) {
        perfil.value = perfil.value?.copy(currentStreak = streak)
    }
}

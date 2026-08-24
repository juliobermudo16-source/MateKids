package com.matekids.domain.model

/**
 * Destreza que entrena una unidad del camino.
 */
enum class Skill {
    SUMAR,
    RESTAR,
    SUMAR_RESTAR,
    MULTIPLICAR,
    DIVIDIR,
    CALCULO_MENTAL,
    PROBLEMAS
}

/**
 * Estado de una leccion en el camino. Se expresa con icono y texto ademas de
 * color, porque la especificacion pide no depender solo del color.
 */
enum class LessonState {
    LOCKED,
    AVAILABLE,
    COMPLETED,
    PERFECT
}

/**
 * Una leccion: un punado corto de ejercicios, pensada para 3-5 minutos.
 *
 * @param index posicion dentro de su unidad, empezando en 0.
 * @param exerciseCount ejercicios que hay que resolver para completarla.
 */
data class Lesson(
    val id: String,
    val unitId: String,
    val index: Int,
    val title: String,
    val skill: Skill,
    val difficulty: Int,
    val exerciseCount: Int = DEFAULT_EXERCISES
) {
    companion object {
        const val DEFAULT_EXERCISES = 8
    }
}

/**
 * Unidad tematica del camino, con sus lecciones en orden.
 */
data class MathUnit(
    val id: String,
    val order: Int,
    val title: String,
    val subtitle: String,
    val skill: Skill,
    val lessons: List<Lesson>
)

/**
 * Avance del nino: que lecciones lleva completadas y con cuantas estrellas.
 *
 * @param completed lecciones terminadas, por id.
 * @param perfect lecciones terminadas sin ningun fallo.
 */
data class PathProgress(
    val completed: Set<String> = emptySet(),
    val perfect: Set<String> = emptySet()
) {
    fun isCompleted(lessonId: String): Boolean = lessonId in completed

    fun stateOf(lesson: Lesson, unlocked: Boolean): LessonState = when {
        lesson.id in perfect -> LessonState.PERFECT
        lesson.id in completed -> LessonState.COMPLETED
        unlocked -> LessonState.AVAILABLE
        else -> LessonState.LOCKED
    }
}

/**
 * El camino completo. Las lecciones se recorren en orden: cada una se abre al
 * terminar la anterior, de modo que el avance se note.
 */
data class LearningPath(val units: List<MathUnit>) {

    /** Todas las lecciones en el orden en que se recorren. */
    fun lessonsInOrder(): List<Lesson> = units.sortedBy { it.order }.flatMap { unit ->
        unit.lessons.sortedBy { it.index }
    }

    /**
     * Una leccion esta abierta si es la primera del camino o si ya se completo
     * la que va justo antes. Las ya completadas siguen abiertas para repetirlas.
     */
    fun isUnlocked(lesson: Lesson, progress: PathProgress): Boolean {
        val ordered = lessonsInOrder()
        val position = ordered.indexOfFirst { it.id == lesson.id }
        if (position <= 0) return true
        val previous = ordered[position - 1]
        return progress.isCompleted(previous.id)
    }

    fun stateOf(lesson: Lesson, progress: PathProgress): LessonState =
        progress.stateOf(lesson, isUnlocked(lesson, progress))

    /** Siguiente leccion pendiente: la que se ofrece al entrar a la app. */
    fun nextLesson(progress: PathProgress): Lesson? =
        lessonsInOrder().firstOrNull { !progress.isCompleted(it.id) }

    /** Avance global de 0 a 1, para la barra del camino. */
    fun overallProgress(progress: PathProgress): Float {
        val all = lessonsInOrder()
        if (all.isEmpty()) return 0f
        return (progress.completed.size.toFloat() / all.size).coerceIn(0f, 1f)
    }

    fun unitProgress(unit: MathUnit, progress: PathProgress): Float {
        if (unit.lessons.isEmpty()) return 0f
        val done = unit.lessons.count { progress.isCompleted(it.id) }
        return (done.toFloat() / unit.lessons.size).coerceIn(0f, 1f)
    }

    fun findLesson(lessonId: String): Lesson? =
        lessonsInOrder().firstOrNull { it.id == lessonId }
}

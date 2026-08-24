package com.matekids.domain.model

/**
 * Las maquinas del laboratorio. Cada una se repara resolviendo su tipo de
 * operacion y devuelve energia al Nucleo.
 */
enum class MachineType {
    SUMADORA,
    RESTADORA,
    MULTIPLICADORA,
    DIVISORA,
    CALCULO_MENTAL,
    FABRICA_DESAFIOS;

    /** Operacion que ejercita la maquina. Las dos ultimas mezclan varias. */
    fun operationType(): OperationType? = when (this) {
        SUMADORA -> OperationType.SUM
        RESTADORA -> OperationType.SUBTRACT
        MULTIPLICADORA -> OperationType.MULTIPLY
        DIVISORA -> OperationType.DIVIDE
        CALCULO_MENTAL, FABRICA_DESAFIOS -> null
    }

    fun displayName(): String = when (this) {
        SUMADORA -> "Sumadora Cuántica"
        RESTADORA -> "Restadora de Equilibrio"
        MULTIPLICADORA -> "Multiplicadora de Energía"
        DIVISORA -> "Divisora Precisa"
        CALCULO_MENTAL -> "Cámara de Cálculo Mental"
        FABRICA_DESAFIOS -> "Fábrica de Desafíos"
    }

    /**
     * Energia del Nucleo necesaria para desbloquearla. Las dos primeras estan
     * disponibles desde el inicio para que siempre haya algo que hacer.
     */
    fun requiredEnergy(): Int = when (this) {
        SUMADORA -> 0
        RESTADORA -> 0
        MULTIPLICADORA -> 6
        DIVISORA -> 10
        CALCULO_MENTAL -> 16
        FABRICA_DESAFIOS -> 22
    }
}

/**
 * Estado visible de una maquina. No se expresa solo con color: la UI le pone
 * icono y texto propios.
 */
enum class MachineState {
    LOCKED,
    BROKEN,
    IN_REPAIR,
    REPAIRED,
    MASTERED
}

/**
 * Una maquina del laboratorio con su avance de reparacion.
 *
 * @param partsRepaired piezas ya encajadas.
 * @param perfectRuns reparaciones completadas sin fallar ninguna pieza.
 */
data class Machine(
    val type: MachineType,
    val partsRepaired: Int = 0,
    val perfectRuns: Int = 0
) {
    companion object {
        /** Piezas que hay que encajar para dejar una maquina operativa. */
        const val TOTAL_PARTS = 5
    }

    fun stateAt(coreEnergy: Int): MachineState = when {
        coreEnergy < type.requiredEnergy() -> MachineState.LOCKED
        partsRepaired >= TOTAL_PARTS && perfectRuns > 0 -> MachineState.MASTERED
        partsRepaired >= TOTAL_PARTS -> MachineState.REPAIRED
        partsRepaired > 0 -> MachineState.IN_REPAIR
        else -> MachineState.BROKEN
    }

    fun progress(): Float = (partsRepaired.toFloat() / TOTAL_PARTS).coerceIn(0f, 1f)

    fun isComplete(): Boolean = partsRepaired >= TOTAL_PARTS
}

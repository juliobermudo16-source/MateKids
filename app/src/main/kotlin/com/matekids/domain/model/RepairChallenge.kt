package com.matekids.domain.model

/** Posicion del mecanismo donde falta la pieza. */
enum class SlotPosition {
    FIRST,
    SECOND,
    RESULT
}

/**
 * Una pieza rota de una maquina: un mecanismo al que le falta un engranaje.
 *
 * A diferencia de una pregunta con respuesta al final, el hueco puede caer en
 * cualquiera de los tres puntos ("? + 8 = 10", "2 + ? = 10", "2 + 8 = ?"), lo
 * que obliga a razonar al reves y no solo a calcular en linea recta.
 *
 * @param pieces valores candidatos que el nino puede arrastrar, ya mezclados.
 */
data class RepairChallenge(
    val type: OperationType,
    val operand1: Int,
    val operand2: Int,
    val result: Int,
    val slot: SlotPosition,
    val pieces: List<Int>
) {
    /** Valor del engranaje que encaja en el hueco. */
    val missingValue: Int
        get() = when (slot) {
            SlotPosition.FIRST -> operand1
            SlotPosition.SECOND -> operand2
            SlotPosition.RESULT -> result
        }

    fun accepts(piece: Int): Boolean = piece == missingValue

    fun symbol(): String = when (type) {
        OperationType.SUM -> "+"
        OperationType.SUBTRACT -> "-"
        OperationType.MULTIPLY -> "×"
        OperationType.DIVIDE -> "÷"
    }

    /**
     * Las tres casillas del mecanismo, con null donde falta la pieza. La UI
     * decide como dibujar el hueco.
     */
    fun slots(): List<Int?> = listOf(
        if (slot == SlotPosition.FIRST) null else operand1,
        if (slot == SlotPosition.SECOND) null else operand2,
        if (slot == SlotPosition.RESULT) null else result
    )

    /**
     * Explicacion breve de por que encaja esa pieza. La especificacion pide
     * feedback educativo, no un simple "incorrecto".
     */
    fun explanation(): String {
        val op = symbol()
        return when (slot) {
            SlotPosition.RESULT ->
                "$operand1 $op $operand2 da $result."
            SlotPosition.FIRST -> when (type) {
                OperationType.SUM -> "Si a $operand2 le falta llegar a $result, la pieza es $operand1."
                OperationType.SUBTRACT -> "Para que al quitar $operand2 queden $result, arriba iba $operand1."
                OperationType.MULTIPLY -> "$result repartido en grupos de $operand2 da $operand1 grupos."
                OperationType.DIVIDE -> "Si al repartir en $operand2 tocan $result, había $operand1."
            }
            SlotPosition.SECOND -> when (type) {
                OperationType.SUM -> "De $operand1 a $result faltan $operand2."
                OperationType.SUBTRACT -> "De $operand1 a $result hay $operand2 de diferencia."
                OperationType.MULTIPLY -> "$operand1 veces $operand2 da $result."
                OperationType.DIVIDE -> "$operand1 repartido entre $operand2 toca a $result."
            }
        }
    }
}

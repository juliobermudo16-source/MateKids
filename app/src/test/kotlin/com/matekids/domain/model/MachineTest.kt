package com.matekids.domain.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MachineTest {

    @Test
    fun `una maquina sin energia suficiente esta bloqueada`() {
        val machine = Machine(MachineType.FABRICA_DESAFIOS)
        assertEquals(MachineState.LOCKED, machine.stateAt(coreEnergy = 0))
    }

    @Test
    fun `las dos primeras maquinas estan disponibles desde el inicio`() {
        assertEquals(MachineState.BROKEN, Machine(MachineType.SUMADORA).stateAt(0))
        assertEquals(MachineState.BROKEN, Machine(MachineType.RESTADORA).stateAt(0))
    }

    @Test
    fun `con piezas encajadas pasa a estar en reparacion`() {
        val machine = Machine(MachineType.SUMADORA, partsRepaired = 2)
        assertEquals(MachineState.IN_REPAIR, machine.stateAt(0))
    }

    @Test
    fun `al encajar todas las piezas queda reparada`() {
        val machine = Machine(MachineType.SUMADORA, partsRepaired = Machine.TOTAL_PARTS)
        assertEquals(MachineState.REPAIRED, machine.stateAt(0))
        assertTrue(machine.isComplete())
    }

    @Test
    fun `una reparacion sin fallos la deja dominada`() {
        val machine = Machine(MachineType.SUMADORA, partsRepaired = Machine.TOTAL_PARTS, perfectRuns = 1)
        assertEquals(MachineState.MASTERED, machine.stateAt(0))
    }

    @Test
    fun `el bloqueo manda sobre el avance`() {
        // Aunque tuviera avance guardado, sin energia sigue bloqueada.
        val machine = Machine(MachineType.DIVISORA, partsRepaired = Machine.TOTAL_PARTS)
        assertEquals(MachineState.LOCKED, machine.stateAt(coreEnergy = 0))
        assertEquals(MachineState.REPAIRED, machine.stateAt(coreEnergy = 10))
    }

    @Test
    fun `el progreso va de cero a uno`() {
        assertEquals(0f, Machine(MachineType.SUMADORA).progress())
        assertEquals(1f, Machine(MachineType.SUMADORA, partsRepaired = Machine.TOTAL_PARTS).progress())
        assertEquals(0.4f, Machine(MachineType.SUMADORA, partsRepaired = 2).progress())
    }

    @Test
    fun `el progreso no se pasa de uno aunque sobren piezas`() {
        val machine = Machine(MachineType.SUMADORA, partsRepaired = 99)
        assertEquals(1f, machine.progress())
    }

    @Test
    fun `una maquina recien creada no esta completa`() {
        assertFalse(Machine(MachineType.SUMADORA).isComplete())
    }

    @Test
    fun `cada maquina ejercita su operacion`() {
        assertEquals(OperationType.SUM, MachineType.SUMADORA.operationType())
        assertEquals(OperationType.SUBTRACT, MachineType.RESTADORA.operationType())
        assertEquals(OperationType.MULTIPLY, MachineType.MULTIPLICADORA.operationType())
        assertEquals(OperationType.DIVIDE, MachineType.DIVISORA.operationType())
        // Las mixtas no fijan una sola operacion.
        assertEquals(null, MachineType.CALCULO_MENTAL.operationType())
        assertEquals(null, MachineType.FABRICA_DESAFIOS.operationType())
    }

    @Test
    fun `los desbloqueos van de menor a mayor energia`() {
        val required = MachineType.entries.map { it.requiredEnergy() }
        assertEquals(required.sorted(), required, "el orden de desbloqueo no es progresivo")
    }
}

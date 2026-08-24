package com.matekids.ui.theme

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AvatarsTest {

    @Test
    fun `hay ocho avatares para elegir`() {
        assertEquals(8, Avatars.all.size)
    }

    @Test
    fun `los identificadores no se repiten`() {
        val ids = Avatars.all.map { it.id }
        assertEquals(ids.size, ids.distinct().size, "hay ids duplicados: $ids")
    }

    @Test
    fun `cada avatar tiene forma y color propios`() {
        assertEquals(
            Avatars.all.size,
            Avatars.all.map { it.shape }.distinct().size,
            "dos avatares comparten forma"
        )
        assertEquals(
            Avatars.all.size,
            Avatars.all.map { it.color }.distinct().size,
            "dos avatares comparten color"
        )
    }

    @Test
    fun `un id desconocido devuelve el avatar por defecto`() {
        // El perfil podria traer un id viejo tras una actualizacion.
        assertEquals(Avatars.default, Avatars.byId("no_existe"))
        assertEquals(Avatars.default, Avatars.byId(""))
    }

    @Test
    fun `se recupera el avatar guardado`() {
        Avatars.all.forEach { avatar ->
            assertEquals(avatar, Avatars.byId(avatar.id))
        }
    }

    @Test
    fun `todos tienen simbolo y nombre visibles`() {
        Avatars.all.forEach { avatar ->
            assertTrue(avatar.symbol.isNotBlank(), "${avatar.id} sin simbolo")
            assertTrue(avatar.name.isNotBlank(), "${avatar.id} sin nombre")
        }
    }
}

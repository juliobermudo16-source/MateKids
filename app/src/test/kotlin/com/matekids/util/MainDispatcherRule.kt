package com.matekids.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Sustituye el dispatcher Main por uno de prueba.
 *
 * Sin esto, cualquier ViewModel que use viewModelScope revienta nada mas
 * construirse en un test unitario: el Main de Android no existe fuera del
 * dispositivo.
 *
 * Se usa UnconfinedTestDispatcher para que lo que se lanza en viewModelScope
 * corra al momento; con el Standard habria que ir avanzando el reloj a mano en
 * cada comprobacion.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

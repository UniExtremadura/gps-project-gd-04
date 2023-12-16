package es.unex.giis.asee.gepeto.view.home

import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.equipamientosDeCocina
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.TreeSet

@RunWith(MockitoJUnitRunner::class)
class EquipamientoFragmentTest{



    private lateinit var fragment: EquipamientoFragment
    private lateinit var equipamientosFiltrados: TreeSet<String>

    @Before
    fun setUp() {
        Session.clear()
        Session.setValue("equipamientosSeleccionados", TreeSet<String>(listOf("Abrelatas", "Arrocera", "Batidora")))
        fragment = EquipamientoFragment()

        equipamientosFiltrados = TreeSet<String>(equipamientosDeCocina)
        equipamientosFiltrados.remove("Abrelatas")
        equipamientosFiltrados.remove("Arrocera")
        equipamientosFiltrados.remove("Batidora")
    }

    @Test
    fun getEquipamientos() {

        val result = fragment.getEquipamientos()

        assertEquals(equipamientosFiltrados, result)
    }

}
package es.unex.giis.asee.gepeto.view.home

import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
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
class IngredientesFragmentTest{



    private lateinit var fragment: IngredientesFragment
    private lateinit var ingredientesFiltrados: TreeSet<String>

    @Before
    fun setUp() {
        Session.clear()
        Session.setValue("ingredientesSeleccionados", TreeSet<String>(listOf("apple butter spread",  "apple cider", "apple juice")))
        fragment = IngredientesFragment()

        ingredientesFiltrados = TreeSet<String>(todosLosIngredientes)
        ingredientesFiltrados.remove("apple butter spread")
        ingredientesFiltrados.remove("apple cider")
        ingredientesFiltrados.remove("apple juice")
    }

    @Test
    fun getIngredientes() {

        val result = fragment.getIngredientes()

        assertEquals(ingredientesFiltrados, result)
    }

}
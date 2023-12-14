package es.unex.giis.asee.gepeto.view.home

import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.data.todosLosIngredientes
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.TreeSet

@RunWith(MockitoJUnitRunner::class)
class ListaFragmentTest {

    private lateinit var fragment: ListaFragment

    @Before
    fun setUp() {
        Session.clear()
        Session.setValue("todoList", hashMapOf("apple butter spread" to true, "apple cider" to true, "apple juice" to true))
        fragment = ListaFragment()
    }

    @Test
    fun getSessionIngredients() {
        val result = fragment.getSessionIngredients()

        // Expected TreeSet after removing selected ingredients
        val expectedIngredients = TreeSet(todosLosIngredientes)
        expectedIngredients.remove("apple butter spread")
        expectedIngredients.remove("apple cider")
        expectedIngredients.remove("apple juice")

        assertEquals(expectedIngredients, result)
    }
}

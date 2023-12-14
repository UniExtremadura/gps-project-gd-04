package es.unex.giis.asee.gepeto.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class RecetaTest {

    private lateinit var receta: Receta

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        receta = Receta(
            1,
            "Tarta de manzana",
            "Tarta de manzana con hojaldre",
            false,
            "harina;azúcar;huevos",
            "",
            0,
            ""
        )
    }


    @Test
    fun getIngredientesPreview() {

        val resultado = receta.getIngredientesPreview()

        assertEquals("Ingredientes: Harina, Azúcar, Huevos.", resultado)
    }


    @Test
    fun testListaIngredientesDetallesConIngredientes() {

        val resultado = receta.listaIngredientesDetalles()

        assertEquals("Ingredientes:\n\n - Harina\n - Azúcar\n - Huevos", resultado)
    }


}

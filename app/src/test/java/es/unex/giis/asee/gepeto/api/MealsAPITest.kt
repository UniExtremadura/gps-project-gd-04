import es.unex.giis.asee.gepeto.api.MealsAPI
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Instructions
import es.unex.giis.asee.gepeto.data.api.Recipes
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MealsAPITest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var mealsAPI: MealsAPI

    @Before
    fun setup() {
        // Configurar el servidor falso antes de cada prueba
        mockWebServer = MockWebServer()
        mealsAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealsAPI::class.java)
    }

    @After
    fun tearDown() {
        // Apagar el servidor después de cada prueba
        mockWebServer.shutdown()
    }

    @Test
    fun getMealByIngredients() {
        val responseBody = """
        [
            {"id": 1, "name": "Recipe 1"},
            {"id": 2, "name": "Recipe 2"}
        ]
    """.trimIndent()

        val response = MockResponse().setBody(responseBody)
        mockWebServer.enqueue(response)
        runBlocking {
            val result = mealsAPI.getMealByIngredients("apple,flour,sugar")

            // Verificar que la solicitud se haya realizado correctamente
            assertTrue(response != null)
            assert(result is Recipes)
        }
    }

    @Test
    fun getMealSteps() {
        val responseBody = """
        [
            {"step": "Step 1"},
            {"step": "Step 2"},
            {"step": "Step 3"}
        ]
    """.trimIndent()

        val response = MockResponse().setBody(responseBody)
        mockWebServer.enqueue(response)
        runBlocking {
            val result = mealsAPI.getMealSteps("123")


            // Verificar que la solicitud se haya realizado correctamente
            assertTrue(response != null)
            assert(result is Instructions)

        }
    }

    @Test
    fun getMealEquipments() {
        val response = MockResponse().setBody("{}")
        mockWebServer.enqueue(response)

        runBlocking {
            val result = mealsAPI.getMealEquipments("456")

            // Verificar que la solicitud se haya realizado correctamente
            assertTrue(response != null)
            assert(result is Equipments)
        }

    }
}

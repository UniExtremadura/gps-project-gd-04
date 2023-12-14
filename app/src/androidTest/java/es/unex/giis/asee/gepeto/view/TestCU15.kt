package es.unex.giis.asee.gepeto.view


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.database.GepetoDatabase
import es.unex.giis.asee.gepeto.database.dao.RecetaDao
import es.unex.giis.asee.gepeto.database.dao.UserDao
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.model.UsuarioRecetasCrossRef
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestCU15 {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var volatileDB: GepetoDatabase
    private lateinit var userDAO: UserDao
    private lateinit var recipeDAO: RecetaDao

    // Se ejecuta antes del test, crea un usuario de prueba
    @Before
    fun setupUserDB() {
        // Obtiene una referencia a la base de datos de Room
        val context: Context = ApplicationProvider.getApplicationContext<Context>()
        volatileDB = GepetoDatabase.getInstance(context)!!

        userDAO = volatileDB.userDao()
        recipeDAO = volatileDB.recetaDao()

        // Crea un usuario de prueba
        val user = User(1, "admin", "admin")


        //13073	Seared Ahi Tuna Salad		0	sashimi grade ahi tuna filet;belgian endive;pea greens;sauce;roasted peppers;yams		2131165323	https://spoonacular.com/recipeImages/13073-312x231.jpg
        val receta1 = Receta(13073,"Seared Ahi Tuna Salad","",false,"sashimi grade ahi tuna filet;belgian endive;pea greens;sauce;roasted peppers;yams","",2131165323,"https://spoonacular.com/recipeImages/13073-312x231.jpg")
        //634180	Banana Smoothie Boost		0	almond milk;banana;chocolate;coffee;honey		2131165323	https://spoonacular.com/recipeImages/634180-312x231.jpg
        val receta2 = Receta(634180,"Banana Smoothie Boost","",false,"almond milk;banana;chocolate;coffee;honey","",2131165323,"https://spoonacular.com/recipeImages/634180-312x231.jpg")
        //660932	Spiced Apple Cider		0	freshly apple juice;allspice;cinnamon sticks;ginger;maple syrup;nutmeg;orange		2131165323	https://spoonacular.com/recipeImages/660932-312x231.jpg
        val receta3 = Receta(660932,"Spiced Apple Cider","",false,"freshly apple juice;allspice;cinnamon sticks;ginger;maple syrup;nutmeg;orange","",2131165323,"https://spoonacular.com/recipeImages/660932-312x231.jpg")
        // Inserta el usuario en la base de datos
        runBlocking {
            userDAO.insert(user)
            recipeDAO.insertAndRelate(receta1, 1)
            recipeDAO.insertAndRelate(receta2, 1)
            recipeDAO.insertAndRelate(receta3, 1)

        }
    }

    @Test
    fun testCU15() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.et_username),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        3
                    ),
                    1
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("admin"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        3
                    ),
                    3
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("admin"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.bt_login), withText("Login"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        4
                    ),
                    0
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.buscador_de_recetas),
                childAtPosition(
                    allOf(
                        withId(R.id.buscador_receta_container),
                        childAtPosition(
                            withId(R.id.relativeLayout),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("Smoothie"), closeSoftKeyboard())

        val editText = onView(
            allOf(
                withId(R.id.buscador_de_recetas), withText("Smoothie"),
                withParent(
                    allOf(
                        withId(R.id.buscador_receta_container),
                        withParent(withId(R.id.relativeLayout))
                    )
                ),
                isDisplayed()
            )
        )
        editText.check(matches(withText("Smoothie")))

        val viewGroup = onView(
            allOf(
                withId(R.id.cl_item),
                withParent(
                    allOf(
                        withId(R.id.rv_historial_list),
                        withParent(withId(R.id.buscador_receta_container))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.rv_historial_list),
                childAtPosition(
                    withId(R.id.buscador_receta_container),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val imageView = onView(
            allOf(
                withId(R.id.receta_detalle_imagen), withContentDescription("Preview de la receta"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    @After
    fun closeVolatileDB() {
        volatileDB.close()
    }
}

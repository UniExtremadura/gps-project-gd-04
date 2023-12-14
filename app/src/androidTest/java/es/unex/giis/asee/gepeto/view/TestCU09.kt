package es.unex.giis.asee.gepeto.view


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.database.GepetoDatabase
import es.unex.giis.asee.gepeto.database.dao.UserDao
import es.unex.giis.asee.gepeto.model.User
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestCU09 {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    private lateinit var volatileDB: GepetoDatabase
    private lateinit var userDAO: UserDao

    // Se ejecuta antes del test, crea un usuario de prueba
    @Before
    fun setupUserDB() {
        // Obtiene una referencia a la base de datos de Room
        var context: Context = ApplicationProvider.getApplicationContext<Context>()
        volatileDB = GepetoDatabase.getInstance(context)!!

        userDAO = volatileDB.userDao()

        // Crea un usuario de prueba
        val user = User(1, "admin", "admin")

        // Inserta el usuario en la base de datos
        runBlocking {
            userDAO.insert(user)
        }
    }

    @Test
    fun testCU09() {
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

        val materialButton3 = onView(
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
        materialButton3.perform(scrollTo(), click())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.ingredientesFragment), withContentDescription("Crear Receta"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.button_item), withText("Ahi tuna"),
                childAtPosition(
                    allOf(
                        withId(R.id.rv_item),
                        childAtPosition(
                            withId(R.id.rvTodosIngredientes),
                            4
                        )
                    ),
                    0
                )
            )
        )
        materialButton4.perform(scrollTo(), click())

        val materialButton5 = onView(
            allOf(
                withId(R.id.button_item), withText("Alfredo pasta sauce"),
                childAtPosition(
                    allOf(
                        withId(R.id.rv_item),
                        childAtPosition(
                            withId(R.id.rvTodosIngredientes),
                            4
                        )
                    ),
                    0
                )
            )
        )
        materialButton5.perform(scrollTo(), click())

        val button = onView(
            allOf(
                withId(R.id.button_item), withText("Ahi tuna"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvIngredientesSeleccionados))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.button_item), withText("Alfredo pasta sauce"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvIngredientesSeleccionados))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val materialButton6 = onView(
            allOf(
                withId(R.id.btnCrearReceta), withText("Crear Receta"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.ingredientes), withText("Ahi tuna, Alfredo pasta sauce."),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Ahi tuna, Alfredo pasta sauce.")))

        val textView2 = onView(
            allOf(
                withId(R.id.equipamiento), withText("."),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText(".")))

        val materialButton7 = onView(
            allOf(
                withId(R.id.crear_receta_btn), withText("Crear Receta"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())

        Thread.sleep(5000)
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
}

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
class TestCU05 {

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
    fun testCU05() {
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

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.buscador_de_ingredientes),
                childAtPosition(
                    allOf(
                        withId(R.id.buscador_container),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("Pork"), closeSoftKeyboard())

        val editText = onView(
            allOf(
                withId(R.id.buscador_de_ingredientes), withText("Pork"),
                withParent(
                    allOf(
                        withId(R.id.buscador_container),
                        withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        editText.check(matches(withText("Pork")))

        val button = onView(
            allOf(
                withId(R.id.button_item), withText("Pork"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvTodosIngredientes))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton4 = onView(
            allOf(
                withId(R.id.button_item), withText("Pork"),
                childAtPosition(
                    allOf(
                        withId(R.id.rv_item),
                        childAtPosition(
                            withId(R.id.rvTodosIngredientes),
                            3
                        )
                    ),
                    0
                )
            )
        )
        materialButton4.perform(scrollTo(), click())

        val materialButton5 = onView(
            allOf(
                withId(R.id.button_item), withText("Lean pork tenderloin"),
                childAtPosition(
                    allOf(
                        withId(R.id.rv_item),
                        childAtPosition(
                            withId(R.id.rvTodosIngredientes),
                            2
                        )
                    ),
                    0
                )
            )
        )
        materialButton5.perform(scrollTo(), click())

        val button2 = onView(
            allOf(
                withId(R.id.button_item), withText("Pork"),
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

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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestCU07 {

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
    fun testCU07() {
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
                withId(R.id.equipamientoFragment), withContentDescription("Equipamiento"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val button = onView(
            allOf(
                withId(R.id.button_item), withText("Abrelatas"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvTodosEquipamientos))
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.button_item), withText("Arrocera"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvTodosEquipamientos))
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val materialButton4 = onView(
            allOf(
                withId(R.id.button_item), withText("Arrocera"),
                childAtPosition(
                    allOf(
                        withId(R.id.rv_item),
                        childAtPosition(
                            withId(R.id.rvTodosEquipamientos),
                            1
                        )
                    ),
                    0
                )
            )
        )
        materialButton4.perform(scrollTo(), click())

        val materialButton5 = onView(
            allOf(
                withId(R.id.button_item), withText("Abrelatas"),
                childAtPosition(
                    allOf(
                        withId(R.id.rv_item),
                        childAtPosition(
                            withId(R.id.rvTodosEquipamientos),
                            0
                        )
                    ),
                    0
                )
            )
        )
        materialButton5.perform(scrollTo(), click())

        val button3 = onView(
            allOf(
                withId(R.id.button_item), withText("Abrelatas"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvEquipamientosSeleccionados))
                    )
                ),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))

        val button4 = onView(
            allOf(
                withId(R.id.button_item), withText("Arrocera"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvEquipamientosSeleccionados))
                    )
                ),
                isDisplayed()
            )
        )
        button4.check(matches(isDisplayed()))

        val materialButton6 = onView(
            allOf(
                withId(R.id.button_item), withText("Arrocera"),
                childAtPosition(
                    allOf(
                        withId(R.id.rv_item),
                        childAtPosition(
                            withId(R.id.rvEquipamientosSeleccionados),
                            1
                        )
                    ),
                    0
                )
            )
        )
        materialButton6.perform(scrollTo(), click())

        val button5 = onView(
            allOf(
                withId(R.id.button_item), withText("Arrocera"),
                withParent(
                    allOf(
                        withId(R.id.rv_item),
                        withParent(withId(R.id.rvTodosEquipamientos))
                    )
                ),
                isDisplayed()
            )
        )
        button5.check(matches(isDisplayed()))
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

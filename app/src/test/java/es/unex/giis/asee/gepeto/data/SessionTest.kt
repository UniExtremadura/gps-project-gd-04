package es.unex.giis.asee.gepeto.data

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.MockitoAnnotations

class SessionTest {

    @Before
    fun setup() {
        Session.setValue("key1", "value1")
        Session.setValue("key2", "value2")
    }

    @Test
    fun getValue() {

        val retrievedValue = Session.getValue("key1")

        assertEquals("value1", retrievedValue)

    }

    @Test
    fun setValue() {

        Session.setValue("key3", "value3")
        val retrievedValue = Session.getValue("key3")

        assertEquals("value3", retrievedValue)
    }

    @Test
    fun removeValue() {

        Session.removeValue("key1")
        val retrievedValue = Session.getValue("key1")

        assertEquals(null, retrievedValue)

    }

    @Test
    fun clear() {

            Session.clear()
            val retrievedValue1 = Session.getValue("key1")
            val retrievedValue2 = Session.getValue("key2")

            assertEquals(null, retrievedValue1)
            assertEquals(null, retrievedValue2)
    }
}
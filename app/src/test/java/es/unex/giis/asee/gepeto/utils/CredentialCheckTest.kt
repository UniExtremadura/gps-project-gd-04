package es.unex.giis.asee.gepeto.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class CredentialCheckTest {

    @Before
    fun setup() {
    }

    @Test
    fun login() {

        //Username vacío
        assertEquals("Invalid username", CredentialCheck.login("", "password123").msg)
        assertTrue(CredentialCheck.login("", "password123").fail)
        //Username corto
        assertEquals("Invalid username", CredentialCheck.login("abc", "password123").msg)
        assertTrue(CredentialCheck.login("abc", "password123").fail)
        //Password vacío
        assertEquals("Invalid password", CredentialCheck.login("username", "").msg)
        assertTrue(CredentialCheck.login("username", "").fail)
        //Password corto
        assertEquals("Invalid password", CredentialCheck.login("username", "abc").msg)
        assertTrue(CredentialCheck.login("username", "abc").fail)
        //Credenciales válidas
        assertEquals("Your credentials are OK", CredentialCheck.login("username", "password123").msg)
        assertFalse(CredentialCheck.login("username", "password123").fail)
    }


    @Test
    fun testPasswordOk() {
        // Password vacío
        assertEquals("Invalid password", CredentialCheck.passwordOk("", "password123").msg)
        assertTrue(CredentialCheck.passwordOk("", "password123").fail)

        // Password corto
        assertEquals("Invalid password", CredentialCheck.passwordOk("abc", "abc").msg)
        assertTrue(CredentialCheck.passwordOk("pass", "password123").fail)

        // Contraseñas no coinciden
        assertEquals("Passwords do not match", CredentialCheck.passwordOk("password123", "password456").msg)
        assertTrue(CredentialCheck.passwordOk("password123", "password456").fail)

        // Contraseñas válidas
        assertEquals("Your credentials are OK", CredentialCheck.passwordOk("password123", "password123").msg)
        assertFalse(CredentialCheck.passwordOk("password123", "password123").fail)
    }

    @Test
    fun testNewPasswordOk() {
        // Password vacío
        assertEquals("Invalid password", CredentialCheck.newPasswordOk("").msg)
        assertTrue(CredentialCheck.newPasswordOk("").fail)

        // Password corto
        assertEquals("Invalid password", CredentialCheck.newPasswordOk("abc").msg)
        assertTrue(CredentialCheck.newPasswordOk("abc").fail)

        // Contraseña válida
        assertEquals("Your credentials are OK", CredentialCheck.newPasswordOk("password123").msg)
        assertFalse(CredentialCheck.newPasswordOk("password123").fail)
    }

    @Test
    fun testJoin() {
        // Username vacío
        assertEquals("Invalid username", CredentialCheck.join("", "password123", "password123").msg)
        assertTrue(CredentialCheck.join("", "password123", "password123").fail)

        // Username corto
        assertEquals("Invalid username", CredentialCheck.join("abc", "password123", "password123").msg)
        assertTrue(CredentialCheck.join("abc", "password123", "password123").fail)

        // Password vacío
        assertEquals("Invalid password", CredentialCheck.join("username", "", "password123").msg)
        assertTrue(CredentialCheck.join("username", "", "password123").fail)

        // Password corto
        assertEquals("Invalid password", CredentialCheck.join("username", "abc", "password123").msg)
        assertTrue(CredentialCheck.join("username", "pass", "password123").fail)

        // Contraseñas no coinciden
        assertEquals("Passwords do not match", CredentialCheck.join("username", "password123", "password456").msg)
        assertTrue(CredentialCheck.join("username", "password123", "password456").fail)

        // Credenciales válidas
        assertEquals("Your credentials are OK", CredentialCheck.join("username", "password123", "password123").msg)
        assertFalse(CredentialCheck.join("username", "password123", "password123").fail)
    }
}
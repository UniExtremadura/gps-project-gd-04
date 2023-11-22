package es.unex.giis.asee.gepeto.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.room.Room
import es.unex.giis.asee.gepeto.database.GepetoDatabase

import es.unex.giis.asee.gepeto.databinding.ActivityLoginBinding
import es.unex.giis.asee.gepeto.utils.CredentialCheck
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var db: GepetoDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding and set content view
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = GepetoDatabase.getInstance(applicationContext)!!

        //views initialization and listeners
        setUpListeners()

        readSettings()
    }

    private fun readSettings(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this).all

        val rememberMe = preferences["rememberme"] as Boolean? ?: false
        val username = preferences["username"] as String? ?: ""
        val password = preferences["password"] as String? ?: ""

        if (rememberMe) {
            binding.etUsername.setText(username)
            binding.etPassword.setText(password)
        }
    }

    private fun setUpListeners() {
        with(binding) {

            btLogin.setOnClickListener {
                correctLogin()
            }

        }
    }

    private fun correctLogin(){
        val credentialCheck = CredentialCheck.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())

        if (credentialCheck.fail) {
            notifyInvalidCredentials(credentialCheck.msg)
            return
        }

        lifecycleScope.launch{
            val user =
                db.userDao().findByName(binding.etUsername.text.toString()) ?: null
            if (user != null) {
                // db.userDao().insert(User(-1, etUsername.text.toString(), etPassword.text.toString()))
                val passwordCheck = CredentialCheck.passwordOk(binding.etPassword.text.toString(), user.password)
                if (passwordCheck.fail) notifyInvalidCredentials(passwordCheck.msg)

            }
            else notifyInvalidCredentials("Invalid username")
        }
    }


    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }



}
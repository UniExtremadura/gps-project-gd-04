package es.unex.giis.asee.gepeto.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.database.GepetoDatabase

import es.unex.giis.asee.gepeto.databinding.ActivityLoginBinding
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.utils.CredentialCheck
import es.unex.giis.asee.gepeto.view.home.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var db: GepetoDatabase

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                with(result.data) {
                    val name = this?.getStringExtra(JoinActivity.USERNAME).orEmpty()
                    val password = this?.getStringExtra(JoinActivity.PASS).orEmpty()

                    with(binding) {
                        etPassword.setText(password)
                        etUsername.setText(name)
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        "New user ($name/$password) created",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding and set content view
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = GepetoDatabase.getInstance(applicationContext)!!

        //views initialization and listeners
        setUpUI()
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

    private fun setUpUI() {
        //get attributes from xml using binding
    }

    private fun setUpListeners() {
        with(binding) {

            btLogin.setOnClickListener {
                correctLogin()
            }

            btRegister.setOnClickListener {
                navigateToJoin()
            }

            btRestore.setOnClickListener {
                navigateToRestore()
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
            val user = db?.userDao()?.findByName(binding.etUsername.text.toString()) //?: User(-1, etUsername.text.toString(), etPassword.text.toString())
            if (user != null) {
                // db.userDao().insert(User(-1, etUsername.text.toString(), etPassword.text.toString()))
                val passwordCheck = CredentialCheck.passwordOk(binding.etPassword.text.toString(), user.password)
                if (passwordCheck.fail) notifyInvalidCredentials(passwordCheck.msg)
                else Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
            }
            else notifyInvalidCredentials("Invalid username")
        }
    }

    private fun navigateToJoin() {
        JoinActivity.start(this, responseLauncher)
    }

    private fun navigateToRestore() {
        val showPopUp = PopUpFragment()
        showPopUp.show(supportFragmentManager, "showPopUp")
    }


    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun correctLogin(){
        val credentialCheck = CredentialCheck.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())

        val user = User(1, binding.etUsername.text.toString(), "")

        navigateToHomeActivity(user)
    }

    private fun navigateToHomeActivity(user: User) {
        Toast.makeText(this, "Welcome ${user.name}!!", Toast.LENGTH_SHORT).show()
        Session.setValue("user", user)
        HomeActivity.start(this, user)
    }

    private fun navigateToJoin() {
        JoinActivity.start(this, responseLauncher)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }



}
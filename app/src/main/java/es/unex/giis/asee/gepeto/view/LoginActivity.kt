package es.unex.giis.asee.gepeto.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
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

        val rememberme = preferences["rememberme"] as Boolean? ?: false
        val username = preferences["username"] as String? ?: ""
        val password = preferences["password"] as String? ?: ""

        if (rememberme) {
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
                checkLogin()
            }

            btRegister.setOnClickListener {
                navigateToJoin()
            }

            btWebsiteLink.setOnClickListener {
                navigateToRecover()
            }
        }
    }
    private fun checkLogin(){
        val check = CredentialCheck.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        if (!check.fail){
            lifecycleScope.launch{
                val user = db?.userDao()?.findByName(binding.etUsername.text.toString()) //?: User(-1, etUsername.text.toString(), etPassword.text.toString())
                if (user != null) {
                    // db.userDao().insert(User(-1, etUsername.text.toString(), etPassword.text.toString()))
                    val check = CredentialCheck.passwordOk(binding.etPassword.text.toString(), user.password)
                    if (check.fail) notifyInvalidCredentials(check.msg)
                    else navigateToHomeActivity(user!!, check.msg)
                }
                else notifyInvalidCredentials("Invalid username")
            }
        }
        else notifyInvalidCredentials(check.msg)
    }

    private fun navigateToHomeActivity(user: User, msg: String) {
        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        HomeActivity.start(this, user)
    }

    private fun navigateToJoin() {
        JoinActivity.start(this, responseLauncher)
    }

    private fun navigateToRecover() {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://trakt.tv/"))
        startActivity(webIntent)
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}
package es.unex.giis.asee.gepeto.view.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import es.unex.giis.asee.gepeto.R

import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.view.home.recetas.FavoritasFragment
import es.unex.giis.asee.gepeto.view.home.recetas.RecetasFragment
import es.unex.giis.asee.gepeto.databinding.ActivityHomeBinding
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.view.home.recetas.FavoritasFragmentDirections
import es.unex.giis.asee.gepeto.view.home.recetas.RecetasFragmentDirections

class HomeActivity : AppCompatActivity(), RecetasFragment.OnRecetaClickListener, FavoritasFragment.OnReceta2ClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }


    companion object {
        const val USER_INFO = "USER_INFO"
        fun start(
            context: Context,
            user: User,
        ) {
            val intent = Intent(context, HomeActivity::class.java).apply {
                putExtra(USER_INFO, user)
            }
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getSerializableExtra(USER_INFO) as User

        setUpUI(user)

        //Esto y el siguiente metodo es para probar que funciona favoritas
                // Configurar el NavController con el NavHostFragment
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController

                // Verificar si se debe navegar directamente a FavoritasFragment
                if (shouldNavigateToFavoritas()) {
                    val action = RecetasFragmentDirections.actionRecetasFragmentToFavoritasFragment()
                    navController.navigate(action)
                }
    }

                private fun shouldNavigateToFavoritas(): Boolean {
                    // Agrega lógica aquí para determinar si debes navegar directamente a FavoritasFragment.
                    // Por ejemplo, puedes verificar algún estado o preferencia compartida.
                    // Devuelve true si se debe navegar directamente a FavoritasFragment, de lo contrario, false.
                    return false
                }

    fun setUpUI(user: User) {
        binding.bottomNavigation.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.recetasFragment,
                R.id.listaFragment,
                R.id.ingredientesFragment,
                R.id.equipamientoFragment
            )
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Hide toolbar and bottom navigation when in detail fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if ((destination.id == R.id.recetaDetailFragment)){
                //   binding.toolbar.visibility = View.GONE
                binding.toolbar.menu.clear()
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)

//        val searchItem = menu?.findItem(R.id.action_search)
//        val searchView = searchItem?.actionView as SearchView

        // Configure the search info and add any event listeners.
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chooses the "Settings" item. Show the app settings UI.
            Toast.makeText(this, "Settings option", Toast.LENGTH_SHORT).show()
            true
        }

        else -> {
            // The user's action isn't recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    override fun onRecetaClick(receta: Receta) {
        val action = RecetasFragmentDirections.actionRecetasFragmentToRecetaDetailFragment(receta)
        navController.navigate(action)
    }

    override fun onReceta2Click(receta: Receta) {
        val action = FavoritasFragmentDirections.actionFavoritasFragmentToRecetaDetailFragment(receta)
        navController.navigate(action)
    }
}
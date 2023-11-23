package es.unex.giis.asee.gepeto.view.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.Session

import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.view.home.recetas.FavoritasFragment
import es.unex.giis.asee.gepeto.view.home.recetas.HistorialFragment
import es.unex.giis.asee.gepeto.databinding.ActivityHomeBinding
import es.unex.giis.asee.gepeto.model.Receta

import es.unex.giis.asee.gepeto.view.home.recetas.RecetasFragmentDirections
import java.util.TreeSet

class HomeActivity :
    AppCompatActivity(),
    HistorialFragment.OnRecetaClickListener,
    FavoritasFragment.OnReceta2ClickListener,
    IngredientesFragment.OnCrearRecetaListener,
    ObservacionesFragment.OnGenerarRecetaListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }



    companion object {
        const val USER_INFO = "USER_INFO"
        fun start( context: Context, user: User ) {
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

        setUpUI()
    }

    private fun setUpUI() {
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
            if ((destination.id == R.id.recetaDetailFragment) or
                (destination.id == R.id.observacionesFragment) or
                (destination.id == R.id.settingsFragment)){
//                binding.toolbar.menu.clear()
                binding.bottomNavigation.visibility = View.GONE
            } else {
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

        // Configure the search info and add any event listeners.
        return super.onCreateOptionsMenu(menu)
    }

    fun mostrarLupaAppbar ( mostrar: Boolean ) {
        val searchView = binding.toolbar.menu.findItem(R.id.action_search)
        if (searchView != null)
            searchView.isVisible = mostrar
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chooses the "Settings" item. Show the app settings UI.
            val action = RecetasFragmentDirections.actionHomeToSettingsFragment()
            navController.navigate(action)
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)

        // Configure the search info and add any event listeners.
        return super.onCreateOptionsMenu(menu)
    }

    fun mostrarLupaAppbar ( mostrar: Boolean ) {
        val searchView = binding.toolbar.menu.findItem(R.id.action_search)
        if (searchView != null)
            searchView.isVisible = mostrar
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chooses the "Settings" item. Show the app settings UI.
            val action = RecetasFragmentDirections.actionHomeToSettingsFragment()
            navController.navigate(action)
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
        val action = RecetasFragmentDirections.actionRecetasFragmentToRecetaDetailFragment(receta)
        navController.navigate(action)
    }

    override fun onCrearRecetaClick(ingredientes: TreeSet<String>) {
        val action = IngredientesFragmentDirections
            .actionIngredientesFragmentToObservacionesFragment(
                ingredientes.joinToString(separator = ", ", prefix = "", postfix = ".")
            )
        navController.navigate(action)
    }


    override fun onDestroy() {
        super.onDestroy()
        Session.clear() // Clear session on logout
    }
}



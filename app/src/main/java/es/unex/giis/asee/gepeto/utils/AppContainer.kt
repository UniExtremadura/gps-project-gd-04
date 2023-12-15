package es.unex.giis.asee.gepeto.utils

import android.content.Context
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.database.GepetoDatabase
import es.unex.giis.asee.gepeto.view.home.HomeViewModel

class AppContainer (context: Context?) {
    private val networkService = getNetworkService()
    private val db = GepetoDatabase.getInstance(context!!)
    val repository = Repository(db!!.userDao(), db.recetaDao(), networkService)
    val homeViewModel : HomeViewModel by lazy {
        HomeViewModel()
    }
}
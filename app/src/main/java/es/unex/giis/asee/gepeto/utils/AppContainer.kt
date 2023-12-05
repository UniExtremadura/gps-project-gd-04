package es.unex.giis.asee.gepeto.utils

import android.content.Context
import es.unex.giis.asee.gepeto.api.getNetworkService
import es.unex.giis.asee.gepeto.data.Repository
import es.unex.giis.asee.gepeto.database.GepetoDatabase

class AppContainer (context: Context?) {
    private val networkService = getNetworkService()
    private val db = GepetoDatabase.getInstance(context!!)
    val repository = Repository(db!!.userDao(), db.recetaDao(), networkService)
}
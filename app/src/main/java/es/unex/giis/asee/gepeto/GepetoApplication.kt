package es.unex.giis.asee.gepeto

import android.app.Application
import es.unex.giis.asee.gepeto.utils.AppContainer

class GepetoApplication: Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
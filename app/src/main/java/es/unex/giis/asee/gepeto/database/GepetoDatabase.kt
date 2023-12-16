package es.unex.giis.asee.gepeto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.unex.giis.asee.gepeto.database.dao.RecetaCacheDao
import es.unex.giis.asee.gepeto.database.dao.RecetaDao
import es.unex.giis.asee.gepeto.database.dao.UserDao
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.RecetaCache
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.model.UsuarioRecetasCrossRef

@Database(entities = [User::class,Receta::class,UsuarioRecetasCrossRef::class,RecetaCache::class], version = 3)
abstract class GepetoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recetaDao(): RecetaDao
    abstract fun recetaCacheDao(): RecetaCacheDao

    companion object {
        private var INSTANCE: GepetoDatabase? = null

        fun getInstance(context: Context): GepetoDatabase? {
            if (INSTANCE == null) {
                synchronized(GepetoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        GepetoDatabase::class.java, "gepeto.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}

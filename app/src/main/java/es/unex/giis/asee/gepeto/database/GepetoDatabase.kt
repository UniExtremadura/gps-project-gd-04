package es.unex.giis.asee.gepeto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.unex.giis.asee.gepeto.database.dao.UserDao
import es.unex.giis.asee.gepeto.model.User

@Database(entities = [User::class], version = 2)
abstract class GepetoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

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

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

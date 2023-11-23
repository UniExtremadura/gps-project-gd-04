package es.unex.giis.asee.gepeto.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.unex.giis.asee.gepeto.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): User

    @Update( onConflict = OnConflictStrategy.REPLACE )
    suspend fun update(user: User)

    @Insert
    suspend fun insert(user: User): Long

}

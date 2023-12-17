package es.unex.giis.asee.gepeto.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.model.UserConRecetas
import es.unex.giis.asee.gepeto.model.UsuarioRecetasCrossRef


@Dao
interface RecetaDao {

    @Query("SELECT * FROM receta WHERE recetaId = :recetaId")
    suspend fun findById( recetaId: Int ): Receta

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUserConRecetas( userId: Long ): UserConRecetas

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    suspend fun insert( receta: Receta ): Long

    @Insert( onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertUsuarioReceta ( UsuarioRecetasCrossRef: UsuarioRecetasCrossRef )

    @Update( onConflict = OnConflictStrategy.REPLACE )
    suspend fun update( receta: Receta )

    @Delete()
    suspend fun delete( receta: Receta )

    @Transaction()
    suspend fun insertAndRelate ( receta: Receta, userId: Long ) {
        val recetaId = insert( receta )
        insertUsuarioReceta( UsuarioRecetasCrossRef(userId, recetaId.toInt()) )
    }

    // Para el REFACTOR
    @Query("SELECT * FROM Receta")
    fun  getAllRecetas(): LiveData<List<Receta>>


}
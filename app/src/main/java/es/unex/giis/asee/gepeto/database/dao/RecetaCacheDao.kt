package es.unex.giis.asee.gepeto.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import es.unex.giis.asee.gepeto.model.RecetaCache

@Dao
interface RecetaCacheDao {

    @Query("SELECT * FROM recetacache WHERE query_param = :ingredientes LIMIT 1")
    suspend fun findByIngredients(ingredientes: String): RecetaCache

    @Query("SELECT count(*) FROM recetacache WHERE query_param = :ingredientes")
    suspend fun getSize(ingredientes: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recetasCache: List<RecetaCache>)

    @Delete
    suspend fun delete(receta: RecetaCache)

    @Transaction()
    suspend fun getAndDelete ( ingredientes: String ) : RecetaCache? {

        if (getSize(ingredientes) == 0)
            return null

        val receta = findByIngredients(ingredientes)
        delete(receta)
        return receta
    }

}
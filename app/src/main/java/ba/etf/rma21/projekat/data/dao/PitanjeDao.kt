package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Pitanje

@Dao
interface PitanjeDao {
    @Query("SELECT * FROM pitanje WHERE kvizId = :kvizId")
    suspend fun getPitanja(kvizId: Int): List<Pitanje>

    @Query("DELETE FROM pitanje")
    suspend fun obrisiDb()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun napraviDb(pitanje: List<Pitanje>)
}
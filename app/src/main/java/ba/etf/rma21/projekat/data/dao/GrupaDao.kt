package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Grupa

@Dao
interface GrupaDao {
    @Query("DELETE FROM grupa")
    suspend fun obrisiDb()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun napraviDb(grupe: List<Grupa>)

    // Provjeri
    @Query("SELECT * FROM grupa WHERE PredmetId =:PredmetId")
    suspend fun dajGrupeZaPredmet(PredmetId: Int): List<Grupa>
}
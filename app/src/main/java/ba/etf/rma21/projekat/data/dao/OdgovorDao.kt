package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Odgovor

@Dao
interface OdgovorDao {
    @Query("DELETE FROM odgovor")
    suspend fun obrisiDb()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun napraviDb(odgovori: List<Odgovor>)

    @Query("SELECT * FROM odgovor WHERE KvizId = :kvizId")
    suspend fun getOdgovori(kvizId: Int): List<Odgovor>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun dodajOdgovor(odgovor: Odgovor)

    @Query("SELECT * FROM odgovor WHERE KvizTakenId = :kvizTakenId")
    suspend fun postojiOdgovor(kvizTakenId: Int): List<Odgovor>

    @Query("SELECT * FROM odgovor")
    suspend fun getAllOdgovori(): List<Odgovor>
}
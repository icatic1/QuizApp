package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// id	integer
//The Auto-generated id

//odgovoreno*	integer
//indeks odgovora

@Entity
class Odgovor(
    @PrimaryKey @SerializedName("id") var id: Int,
    @ColumnInfo(name = "odgovoreno") @SerializedName("odgovoreno") var odgovoreno: Int,
    @ColumnInfo(name = "PitanjeId") @SerializedName("PitanjeId") var pitanjeId: Int,
    @ColumnInfo(name = "KvizId") var kvizId: Int,
    @ColumnInfo(name = "KvizTakenId") var idKvizTaken: Int
    ) {

}

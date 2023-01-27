package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


// - naziv: String - jedinstveni naziv pitanja u okviru kvizova u kojima se nalazi
// - tekst: String - tekst pitanja
// - opcije: List<String> - lista ponuđenih odgovora (3 odgovora su ponuđena)
// - tacan: Int - indeks tačnog odgovora u listi ponuđenih
// example: OrderedMap { "id": 1, "naziv": "P1", "tekstPitanja": "Koji je prvi odgovor?", "opcije": List [ "A", "B", "C" ], "tacan": 0 }]

@Entity
class Pitanje(
    @PrimaryKey var idDb: Int,
    @SerializedName("id") var id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") var naziv: String,
    @ColumnInfo(name = "tekstPitanja") @SerializedName("tekstPitanja") var tekstPitanja: String,
    @Ignore @SerializedName("opcije") var opcije: List<String>,
    @ColumnInfo(name = "opcije") var opcijeDb: String,
    @ColumnInfo(name = "tacan") @SerializedName("tacan") var tacan: Int,
    @ColumnInfo(name = "kvizId") var kvizId: Int
) {
    constructor(): this(
        idDb = -1,
        id = -1,
        naziv = "",
        tekstPitanja = "",
        opcije = emptyList<String>(),
        opcijeDb = "",
        tacan = -1,
        kvizId = -1
   )
}
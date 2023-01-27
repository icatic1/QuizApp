package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Kviz(
    @PrimaryKey @SerializedName("id") var id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") var naziv: String,
    @Ignore @SerializedName("datumPocetak") var datumPocetka: Date,
    @Ignore @SerializedName("datumKraj") var datumKraj: Date?,
    @ColumnInfo(name = "datumPocetka") var datumPocetakDb: String,
    @ColumnInfo(name = "datumKraj") var datumKrajDb: String,
    @ColumnInfo(name = "datumRada") var datumRadaDb: String,
    @Ignore var datumRada: Date?,
    @ColumnInfo(name = "trajanje") @SerializedName("trajanje") var trajanje: Int,
    @ColumnInfo(name = "nazivGrupe") var nazivGrupe: String?,
    @ColumnInfo(name = "nazivPredmeta") var nazivPredmeta: String?,
    @ColumnInfo(name = "osvojeniBodovi") var osvojeniBodovi: Int?,
    @ColumnInfo(name = "predan") var predan: Boolean
) {
    constructor(): this(
        id = -1,
        naziv = "",
        datumPocetka = Date(),
        datumKraj = null,
        datumPocetakDb = "",
        datumKrajDb = "",
        datumRadaDb = "",
        datumRada = null,
        trajanje = 0,
        nazivGrupe = null,
        nazivPredmeta = null,
        osvojeniBodovi = null,
        predan = false
    )
}
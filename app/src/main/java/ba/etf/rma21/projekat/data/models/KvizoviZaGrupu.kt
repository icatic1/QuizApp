package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

class KvizoviZaGrupu (
    @SerializedName("id")val id: Int,
    @SerializedName("naziv")val naziv: String?,
    @SerializedName("datumPocetak")val datumPocetak: Date,
    @SerializedName("datumKraj")val datumKraj: Date?,
    @SerializedName("trajanje")val trajanje: Int?,
    @SerializedName("KvizoviGrupe")val KvizoviGrupe: KvizGrupa
        ){
}
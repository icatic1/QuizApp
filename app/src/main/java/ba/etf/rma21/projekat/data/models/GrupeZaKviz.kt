package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

class GrupeZaKviz(
    @SerializedName("id")val id: Int,
    @SerializedName("naziv")val naziv: String?,
    @SerializedName("PredmetId")val PredmetId: Int,
    @SerializedName("KvizoviGrupe")val KvizoviGrupe: KvizGrupa
) {
}
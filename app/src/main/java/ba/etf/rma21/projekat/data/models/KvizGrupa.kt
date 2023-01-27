package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

class KvizGrupa(
    @SerializedName("GrupaId")val GrupaId: Int,
    @SerializedName("KvizId")val KvizId: Int
    ) {
}
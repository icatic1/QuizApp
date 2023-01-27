package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

class GrupaIPredmetId(
    @SerializedName("naziv") val nazivGrupe: String,
    @SerializedName("PredmetId") val predmetId: Int
) {


}
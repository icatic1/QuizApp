package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

class PitanjeZaKviz(
    @SerializedName("id")val id: Int,
    @SerializedName("naziv")val naziv: String?,
    @SerializedName("tekstPitanja")val tekstPitanja: String?,
    @SerializedName("opcije")val opcije: List<String>?,
    @SerializedName("tacan")val tacan: Int,
    @SerializedName("PitanjeKviz")val PitanjeKviz: PitanjeKviz
) {
}
package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

class OdgovorZaTaken(
    @SerializedName("KvizTakenId")val KvizTakenId: Int,
    @SerializedName("odgovoreno")val odgovoreno: Int,
    @SerializedName("PitanjeId")val pitanjeId: Int
) {
}
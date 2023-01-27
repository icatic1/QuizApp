package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName
//"odgovoreno": 1,
//  "KvizTakenId": 35,
//  "PitanjeId": 2,

class PovratniOdgovor(
    @SerializedName("odgovoreno") var odgovoreno: Int?,
    @SerializedName("KvizTakenId") var kvizTakenId: Int?,
    @SerializedName("PitanjeId") var pitanjeId: Int?
) {
}
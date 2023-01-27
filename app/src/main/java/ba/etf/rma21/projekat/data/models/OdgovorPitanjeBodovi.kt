package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

class OdgovorPitanjeBodovi (
    @SerializedName("odgovor")val odgovor: Int,
    @SerializedName("pitanje")val pitanje:Int,
    @SerializedName("bodovi")val bodovi: Int
        ){
}
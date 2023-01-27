package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName
import java.util.*
//    id	integer
//    The Auto-generated id
//
//    student*	string
//    email studenta
//
//    osvojeniBodovi*	number
//    procenat tačno urađenog kviza od 0 do 100
//
//    datumRada	date
//    datum rada kviza

class KvizTaken(
    @SerializedName("id") val id: Int,
    @SerializedName("student") val student: String,
    @SerializedName("osvojeniBodovi") val osvojeniBodovi: Int,
    @SerializedName("datumRada") val datumRada: Date,
    @SerializedName("KvizId") val KvizId: Int
    ) {


}

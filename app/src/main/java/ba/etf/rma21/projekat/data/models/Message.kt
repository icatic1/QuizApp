package ba.etf.rma21.projekat.data.models


import com.google.gson.annotations.SerializedName

class Message(
    @SerializedName("message") val message: String
) {
}
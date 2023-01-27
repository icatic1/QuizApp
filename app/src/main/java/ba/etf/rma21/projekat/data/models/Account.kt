package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class Account(
    @PrimaryKey @SerializedName("id") var id: Int,
    @ColumnInfo (name = "student") @SerializedName("student") var studentEmail: String,
    @ColumnInfo (name = "acHash") @SerializedName("acHash") var hashStudent: String,
    @ColumnInfo (name = "lastUpdate") var lastUpdate: String
) {

}
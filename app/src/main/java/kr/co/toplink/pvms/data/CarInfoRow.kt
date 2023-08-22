package kr.co.toplink.pvms.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class CarInfoRow(
    val id : Int = 0 ,
    val carnumber: String = "",
    val phone: String = "",
    val etc: String = "",
    val date: String = ""
): Parcelable

data class SendKakaoAlrim(
    var id : String,
    var phone : String,
    var msg : String
)

@Serializable
data class SerializeConvert(
    var id : String,
    var phone : String,
    val msg : String
)
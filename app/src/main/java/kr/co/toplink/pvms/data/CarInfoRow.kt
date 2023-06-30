package kr.co.toplink.pvms.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarInfoRow(
    val id : Int = 0 ,
    val carnumber: String = "",
    val phone: String = "",
    val etc: String = "",
    val date: String = ""
): Parcelable
package kr.co.toplink.pvms.data

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class CarInfoList(
    val id : Int,
    val carnumber: String?,
    val phone: String?,
    val date: Date,
    val etc: String?
): Parcelable
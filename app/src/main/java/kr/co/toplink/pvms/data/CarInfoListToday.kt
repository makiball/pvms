package kr.co.toplink.pvms.data

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime
import java.util.*

@Parcelize
data class CarInfoListToday(
    val id: Int,      //차량 번호 풀번호
    val carnumber: String?,      //차량 번호 풀번호
    val phone: String?,
    val date: Date,
    val etc: String?,
    val type: Int = 0     //0 등록차량, 1미등록차량
) : Parcelable
package kr.co.toplink.pvms.data

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo

data class CarInfoList(
    val id : Int,
    val carnumber: String?,
    val phone: String?,
    val etc: String?,
    val date: String?
)

object CarInfoListDiffCallback : DiffUtil.ItemCallback<CarInfoList>() {
    override fun areItemsTheSame(oldItem: CarInfoList, newItem: CarInfoList) =
        oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: CarInfoList, newItem: CarInfoList) =
        oldItem.carnumber == newItem.carnumber
                && oldItem.phone == newItem.phone
                && oldItem.etc == newItem.etc
}
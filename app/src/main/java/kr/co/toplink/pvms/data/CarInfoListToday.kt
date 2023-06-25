package kr.co.toplink.pvms.data

import androidx.recyclerview.widget.DiffUtil

data class CarInfoListToday(
    val id: Int?,      //차량 번호 풀번호
    val carnumber: String?,      //차량 번호 풀번호
    val phone: String?,
    val date: String?,
    val time: String?,
    val etc: String?,
    val type: Int = 0      //0 등록차량, 1미등록차량
)

object CarInfoListTodayDiffCallback : DiffUtil.ItemCallback<CarInfoListToday>() {
    override fun areItemsTheSame(oldItem: CarInfoListToday, newItem: CarInfoListToday) =
        oldItem.date == newItem.date
    override fun areContentsTheSame(oldItem: CarInfoListToday, newItem: CarInfoListToday) =
        oldItem.carnumber == newItem.carnumber
                && oldItem.phone == newItem.phone
                && oldItem.etc == newItem.etc
}
package kr.co.toplink.pvms.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarInfoListModel(val carinfolist : CarInfoList) : Parcelable

@Parcelize
data class CarInfoListTodayModel(val carinfolisttoday : CarInfoListToday) : Parcelable

@Parcelize
data class SmsMangerModel(val smsmanager : SmsManagerList) : Parcelable
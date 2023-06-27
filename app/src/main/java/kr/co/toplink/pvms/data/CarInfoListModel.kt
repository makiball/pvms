package kr.co.toplink.pvms.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarInfoListModel(val carinfolist : CarInfoList) : Parcelable

@Parcelize
data class CarInfoListTodayModel(val carinfolisttoday : CarInfoListToday) : Parcelable
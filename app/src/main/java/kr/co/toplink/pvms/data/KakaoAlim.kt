package kr.co.toplink.pvms.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KakaoAlim(
    val id: String,
    val msg: String,
    val phone: String
): Parcelable

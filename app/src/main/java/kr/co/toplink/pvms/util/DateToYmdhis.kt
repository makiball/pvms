package kr.co.toplink.pvms.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.text.TextUtils.replace
import java.util.*

fun DateToYmdhis(date : Date) : String {

    val utilDate = date
    // Set the time zone to Korea (Asia/Seoul)
    val koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul")
    // Create a SimpleDateFormat with the desired format
    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    dateFormat.timeZone = koreaTimeZone
    // Format the date in the desired time zone
    val formattedDate = dateFormat.format(utilDate)

    return formattedDate
}

fun PhoneHidden(phone: String) : String {

    val regex =  Regex("\\d{2,3}-\\d{3,4}")  //완전한 번호판
    val phone = phone.replace(regex, "***-****")

    return phone
}
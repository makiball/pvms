package kr.co.toplink.pvms.dto

/* result ok, notmatch, point */
data class UserResponse(
    val result      : String,
    val id          : String,
    val smsPoint    : Int,
    val msg         : String
)
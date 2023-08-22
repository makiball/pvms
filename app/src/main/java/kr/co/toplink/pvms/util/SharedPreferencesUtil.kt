package kr.co.toplink.pvms.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kr.co.toplink.pvms.dto.User

class SharedPreferencesUtil(context: Context) {
    val SHARED_PREFERENCES_NAME = "pvms_preference"
    val preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addUser(user: User) {
        val editor = preferences.edit()
        editor.putString("email", user.email)
        editor.putString("passwd", user.passwd)
        editor.putString("fcm", user.fcm)
        editor.apply()
    }

    fun getUser(): User  {
        val email = preferences.getString("email", "")
        val passwd = preferences.getString("passwd", "")
        val fcm = preferences.getString("fcmToken", "")

        return if (email != "") {
            User(email!!, passwd!!, fcm!!)
        } else {
            User("비회원", "", "")
        }
    }

    // FCM Token 가져오기
    fun getFCMToken(): String{
        val token = preferences.getString("fcmToken", "")
        return token!!
    }

    // FCM Token 저장하기
    fun setFCMToken(token: String){
        val editor = preferences.edit()
        editor.putString("fcmToken", token)
        editor.apply()
    }
}
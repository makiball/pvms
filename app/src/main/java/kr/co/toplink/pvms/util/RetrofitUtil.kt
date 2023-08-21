package kr.co.toplink.pvms.util

import kr.co.toplink.pvms.config.ApplicationClass
import kr.co.toplink.pvms.network.api.AuthApi

class RetrofitUtil {
    companion object {
        val authService = ApplicationClass.retrofit.create(AuthApi::class.java)
    }
}
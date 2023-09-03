package kr.co.toplink.pvms.network.api

import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface KakaoAlrimApi {

    @POST("sendMessage.php")
    suspend fun sendSmsMsg(@Query("id") id:String, @Query("msg") msg:String) : KakaoAlrimResponse
}
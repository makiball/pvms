package kr.co.toplink.pvms.network.api

import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("login.php")
    suspend fun signIn(@Body user: User) : UserResponse


    @POST("sendMessage.php")
    suspend fun sendSmsMsg(@Body  kakaoalrim: KakaoAlim) : KakaoAlrimResponse

    @POST("reportMessage.php")
    suspend fun reportMsg(@Body  kakaoalrim: KakaoAlim) : KakaoAlrimResponse
}
package kr.co.toplink.pvms.network.api

import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("login.php")
    suspend fun signIn(@Body user: User) : UserResponse
}
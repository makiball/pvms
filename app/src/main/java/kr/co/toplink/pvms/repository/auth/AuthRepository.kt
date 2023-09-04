package kr.co.toplink.pvms.repository.auth

import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse

class AuthRepository(private val remoteDataSource: AuthRemoteDataSource) {
    suspend fun signIn(user : User) : UserResponse {
        return remoteDataSource.singIn(user)
    }

    suspend fun kakaoAlimSend(kakaoalim : KakaoAlim): KakaoAlrimResponse {
        return remoteDataSource.kakaoAlimSend(kakaoalim)
    }
}
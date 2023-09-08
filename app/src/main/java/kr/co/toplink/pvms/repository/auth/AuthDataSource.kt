package kr.co.toplink.pvms.repository.auth

import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse

interface AuthDataSource {
    suspend fun singIn(user: User) : UserResponse

    suspend fun kakaoAlimSend(kakaoalim : KakaoAlim): KakaoAlrimResponse

    suspend fun kakaoReportSend(kakaoalim : KakaoAlim): KakaoAlrimResponse
}
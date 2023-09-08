package kr.co.toplink.pvms.repository.kakao

import kr.co.toplink.pvms.network.api.KakaoAlrimApi
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse

interface KakaoDataSource {

    suspend fun kakaoAlimSend(id: String, msg: String): KakaoAlrimResponse

    suspend fun kakaoReportSend(id: String, msg: String): KakaoAlrimResponse

}
package kr.co.toplink.pvms.repository.kakao

import kr.co.toplink.pvms.network.response.KakaoAlrimResponse

class KakaoRepository(private val remoteDataSource: KakaoRemoteDataSource) {

    suspend fun kakaoAlimSend(id: String, msg: String): KakaoAlrimResponse {
        return remoteDataSource.kakaoAlimSend(id, msg)
    }

    suspend fun kakaoReportSend(id: String, msg: String): KakaoAlrimResponse {
        return remoteDataSource.kakaoReportSend(id, msg)
    }
}
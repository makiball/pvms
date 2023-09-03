package kr.co.toplink.pvms.repository.kakao

import kr.co.toplink.pvms.network.api.KakaoAlrimApi
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse
import kr.co.toplink.pvms.repository.sms.SmsDataSource

class KakaoRemoteDataSource(private val apiclient:KakaoAlrimApi): KakaoDataSource {

    override suspend fun kakaoAlimSend(id: String, msg: String): KakaoAlrimResponse {
        return apiclient.sendSmsMsg(id, msg)
    }
}
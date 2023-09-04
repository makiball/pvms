package kr.co.toplink.pvms.repository.sms

import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse

interface SmsDataSource {

    suspend fun smsMagAll() : List<SmsManager>

    suspend fun smsManager(id: Int) : SmsManager

    suspend fun smsMagDeletebyid(id: Int)

    suspend fun smsMagUpdatebyid(smsmanager: SmsManager)

    suspend fun smsMagInsert(smsmanager: SmsManager)

}
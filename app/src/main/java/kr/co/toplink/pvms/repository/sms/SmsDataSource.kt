package kr.co.toplink.pvms.repository.sms

import kr.co.toplink.pvms.database.SmsManager

interface SmsDataSource {

    suspend fun smsMagAll() : List<SmsManager>

    suspend fun smsMagDeletebyid(id: Int)

    suspend fun smsMagUpdatebyid(smsmanager: SmsManager)

    suspend fun smsMagInsert(smsmanager: SmsManager)
}
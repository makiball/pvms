package kr.co.toplink.pvms.repository.sms

import kr.co.toplink.pvms.database.CarInfoDao
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.network.response.KakaoAlrimResponse

class SmsLocalDataSource(
    private val dao : CarInfoDao
) : SmsDataSource {
    override suspend fun smsMagAll(): List<SmsManager> {
        return dao.SmsMagAll()
    }

    override suspend fun smsManager(id: Int): SmsManager {
        return dao.SmsManager(id)
    }

    override suspend fun smsMagDeletebyid(id: Int) {
        dao.SmsMagDeletebyid(id)
    }

    override suspend fun smsMagUpdatebyid(smsmanager: SmsManager) {
        dao.SmsMagUpdatebyid(smsmanager)
    }

    override suspend fun smsMagInsert(smsmanager: SmsManager) {
        dao.SmsMagInsert(smsmanager)
    }
}
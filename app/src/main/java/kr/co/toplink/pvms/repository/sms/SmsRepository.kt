package kr.co.toplink.pvms.repository.sms

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.toplink.pvms.database.SmsManager

class SmsRepository(
    private val localDataSource: SmsLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun smsMagAll() : List<SmsManager> {
        return withContext(ioDispatcher) {
            localDataSource.smsMagAll()
        }
    }

    suspend fun smsManager(id: Int): SmsManager {
        return withContext(ioDispatcher) {
            localDataSource.smsManager(id)
        }
    }

    suspend fun smsMagDeletebyid(id: Int) {
        withContext(ioDispatcher) {
            localDataSource.smsMagDeletebyid(id)
        }
    }

    suspend fun smsMagUpdatebyid(smsManager: SmsManager) {
        withContext(ioDispatcher) {
            localDataSource.smsMagUpdatebyid(smsManager)
        }
    }

    suspend fun smsMagInsert(smsManager: SmsManager) {
        withContext(ioDispatcher) {
            localDataSource.smsMagInsert(smsManager)
        }
    }
}

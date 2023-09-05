package kr.co.toplink.pvms.repository.cam

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.repository.car.CarLocalDataSource

class CamRepository(
    private val localDataSource: CamDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {


    suspend fun carInfoSearchByCarnumberOnly(carnum: String) : CarInfo {
        return withContext(ioDispatcher) {
            localDataSource.carInfoSearchByCarnumberOnly(carnum)
        }
    }

    suspend fun carInfoSearchByCarnumber(carnum: String) : CarInfo {
        return withContext(ioDispatcher) {
            localDataSource.carInfoSearchByCarnumber(carnum)
        }
    }


    suspend fun carInfoTodayList(): List<CarInfoToday> {
        return withContext(ioDispatcher) {
            localDataSource.carInfoTodayList()
        }
    }

    suspend fun carInfoToday(carnum: String) : CarInfoToday {
        return withContext(ioDispatcher) {
            localDataSource.carInfoToday(carnum)
        }
    }

    suspend fun carInfoInsertToday(carinfotoday: CarInfoToday) {
        withContext(ioDispatcher) {
            localDataSource.carInfoInsertToday(carinfotoday)
        }
    }

    suspend fun carInfoTodayDelete() {
        withContext(ioDispatcher) {
            localDataSource.carInfoTodayDelete()
        }
    }

    suspend fun carInfoTodayUpdateById(carinfotoday: CarInfoToday)  {
        withContext(ioDispatcher) {
            localDataSource.carInfoTodayUpdateById(carinfotoday)
        }
    }

    suspend fun carInfoTodayDeleteById (id: Int) {
        withContext(ioDispatcher) {
            localDataSource.carInfoTodayDeleteById(id)
        }
    }
}
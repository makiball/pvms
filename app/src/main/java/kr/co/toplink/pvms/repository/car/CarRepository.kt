package kr.co.toplink.pvms.repository.car

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.toplink.pvms.database.CarInfo

class CarRepository(
    private val localDataSource: CarLocalDataSource,
    private val ioDispatcher:CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun addCarInfo(carInfo : CarInfo) {
        withContext(ioDispatcher) {
            val carInfoData = CarInfo(
                carnumber = carInfo.carnumber,
                carnumber4d = carInfo.carnumber4d,
                carnumberonly = carInfo.carnumberonly,
                phone = carInfo.phone,
                date = carInfo.date,
                etc = carInfo.etc
            )
            localDataSource.addCarInfo(carInfoData)
        }
    }

    suspend fun getCarInfo() : List<CarInfo> {
        return withContext(ioDispatcher) {
            localDataSource.getCarInfo()
        }
    }

    suspend fun carInfoDelete() {
        withContext(ioDispatcher) {
            localDataSource.carInfoDelete()
        }
    }

    suspend fun carInfoSearchLikeCarnumber(carnum : String): List<CarInfo>  {
        return withContext(ioDispatcher) {
            localDataSource.carInfoSearchLikeCarnumber(carnum)
        }
    }

    suspend fun carInfoSearchLikePhone(phone: String): List<CarInfo>  {
        return   withContext(ioDispatcher) {
            localDataSource.carInfoSearchLikePhone(phone)
        }
    }

    suspend fun carInfoSearchLikeEtc(etc: String): List<CarInfo>  {
        return   withContext(ioDispatcher) {
            localDataSource.carInfoSearchLikeEtc(etc)
        }
    }

    suspend fun carInfoSearchByCarnumber(carnum: String): CarInfo  {
        return   withContext(ioDispatcher) {
            localDataSource.carInfoSearchByCarnumber(carnum)
        }
    }
}
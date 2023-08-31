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
            val carInfo = CarInfo(
                carnumber = carInfo.carnumber,
                carnumber4d = carInfo.carnumber4d,
                carnumberonly = carInfo.carnumberonly,
                phone = carInfo.phone,
                date = carInfo.date,
                etc = carInfo.etc
            )
            localDataSource.addCarInfo(carInfo)
        }
    }

    suspend fun getCarInfo() : List<CarInfo> {
        return withContext(ioDispatcher) {
            localDataSource.getCarInfo()
        }
    }
}
package kr.co.toplink.pvms.repository.car

import androidx.room.Dao
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoDao

class CarLocalDataSource (
    private val dao : CarInfoDao
) : CarDataSource {
    override suspend fun addCarInfo(carinfo: CarInfo) {
        dao.CarInfoInsert(carinfo)
    }

    override suspend fun getCarInfo(): List<CarInfo> {
        return dao.CarInfoAll()
    }


}
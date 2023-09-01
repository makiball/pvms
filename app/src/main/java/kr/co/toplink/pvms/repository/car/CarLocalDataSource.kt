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


    override suspend fun carInfoDelete() {
        return dao.CarInfoDelete()
    }

    override suspend fun carInfoSearchLikeCarnumber(carnum: String): List<CarInfo> {
        return dao.CarInfoSearchLikeCarnumber(carnum)
    }

    override suspend fun carInfoSearchLikePhone(phone: String): List<CarInfo> {
        return dao.CarInfoSearchLikePhone(phone)
    }

    override suspend fun carInfoSearchLikeEtc(etc: String): List<CarInfo> {
        return dao.CarInfoSearchLikeEtc(etc)
    }


}
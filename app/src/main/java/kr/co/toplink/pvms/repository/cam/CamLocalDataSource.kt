package kr.co.toplink.pvms.repository.cam

import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoDao
import kr.co.toplink.pvms.database.CarInfoToday

class CamLocalDataSource (
    private val dao : CarInfoDao
) : CamDataSource {

    override suspend fun carInfoSearchByCarnumberOnly(carnum: String) : CarInfo {
        return dao.CarInfoSearchByCarnumberOnly(carnum)
    }

    override suspend fun carInfoSearchByCarnumber(carnum: String) : CarInfo {
        return dao.CarInfoSearchByCarnumber(carnum)
    }

    override suspend fun carInfoTodayList(): List<CarInfoToday> {
        return dao.CarInfoTodayList()
    }

    override suspend fun carInfoToday(carnum: String) : CarInfoToday {
        return dao.CarInfoToday(carnum)
    }

    override suspend fun carInfoInsertToday(carinfotoday: CarInfoToday) {
        return dao.CarInfoInsertToday(carinfotoday)
    }

    override suspend fun carInfoTodayDelete(){
        return dao.CarInfoTodayDelete()
    }

    override suspend fun carInfoTodayUpdateById(carinfotoday: CarInfoToday) {
        return dao.CarInfoTodayUpdateById(carinfotoday)
    }

    override suspend fun carInfoTodayDeleteById(id: Int) {
        return dao.CarInfoTodayDeleteById(id)
    }

}
package kr.co.toplink.pvms.repository.cam

import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoToday

interface CamDataSource {

    suspend fun carInfoSearchByCarnumberOnly(carnum: String) : CarInfo

    suspend fun carInfoSearchByCarnumber(carnum: String) : CarInfo

    suspend fun carInfoTodayList() : List<CarInfoToday>

    suspend fun carInfoToday(carnum: String) : CarInfoToday

    suspend fun carInfoInsertToday(carinfotoday: CarInfoToday)

    suspend fun carInfoTodayDelete()

    suspend fun carInfoTodayUpdateById(carinfotoday: CarInfoToday)

    suspend fun carInfoTodayDeleteById (id: Int)

}
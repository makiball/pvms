package kr.co.toplink.pvms.repository.car

import kotlinx.coroutines.withContext
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.database.CarInfo

interface CarDataSource {

    /* 자동차 번호 등록 */
    suspend fun addCarInfo(carInfo : CarInfo)

    /* 자동차 번호 리스트 */
    suspend fun getCarInfo() : List<CarInfo>

    /* 자동차 번호 삭제 */
    /* 자동차 번호 전부 삭제 */
    /* 자동차 번호 수정 */



}
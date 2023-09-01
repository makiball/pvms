package kr.co.toplink.pvms.repository.car

import kr.co.toplink.pvms.database.CarInfo

interface CarDataSource {

    suspend fun addCarInfo(carinfo: CarInfo)

    suspend fun getCarInfo() : List<CarInfo>

    /* 등록된 것 전부 삭제 */
    suspend fun carInfoDelete()

    /* 번호판 검색 */
    suspend fun carInfoSearchLikeCarnumber(carnum : String) : List<CarInfo>

    /* 휴대폰 검색 */
    suspend fun carInfoSearchLikePhone(phone : String) : List<CarInfo>

    /* 기타 검색 */
    suspend fun carInfoSearchLikeEtc(etc : String) : List<CarInfo>

    suspend fun carInfoSearchByCarnumber(carnum : String) : CarInfo

}
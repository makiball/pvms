package kr.co.toplink.pvms.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListToday
import kr.co.toplink.pvms.data.CarInfoRow

@Dao
interface CarInfoDao {

    @Query("SELECT * FROM CarInfo")
    fun CarInfoAll(): List<CarInfo>

    @Query("SELECT id, carnumber, phone, date, etc FROM CarInfo ORDER BY id DESC")
    fun CarInfoGetAll(): LiveData<MutableList<CarInfoList>>

    @Query("SELECT id, carnumber, phone, date, time, etc, type FROM CarInfoToday")
    fun CarInfoGetTdoday(): LiveData<MutableList<CarInfoListToday>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoInsert(carinfo: CarInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoInsertToday(carsearchToday: CarInfoToday)


    @Query("DELETE FROM CarInfo")
    fun CarInfoDelete()


    @Query("DELETE FROM CarInfo WHERE id = :id")
    fun CarInfoDeletebyid(id: Int)

    /* 차랑번호 검색 */
    @Query("SELECT id, carnumber, phone, date, etc FROM CarInfo WHERE carnumber LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikeCarnumber(searchText: String?) : List<CarInfo>

    /* 번호판 검색 */
    @Query("SELECT id, carnumber, phone, etc, date FROM CarInfo WHERE carnumberonly = :searchText")
    fun CarInfoSearchByCarnumberOnly(searchText: String?) : List<CarInfoRow>

    /* 번호판 검색 */
    @Query("SELECT id, carnumber, phone, etc, date FROM CarInfo WHERE carnumber = :searchText limit 1")
    fun CarInfoSearchByCarnumber(searchText: String?) : List<CarInfoRow>


    /* 휴대폽번호 검색 */
    @Query("SELECT id, carnumber, phone, date, etc FROM CarInfo WHERE phone LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikePhone(searchText: String?) : List<CarInfo>

    /* 비고 검색 */
    @Query("SELECT id, carnumber, phone, date, etc FROM CarInfo WHERE etc LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikeEtc(searchText: String?) : List<CarInfo>

}
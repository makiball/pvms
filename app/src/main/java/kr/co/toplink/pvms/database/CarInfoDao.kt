package kr.co.toplink.pvms.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListToday
import kr.co.toplink.pvms.data.CarInfoRow

@Dao
interface CarInfoDao {

    /* 자동차 반호 입력 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoInsert(carinfo: CarInfo)

    /* 전체 리스트 가져오기 */
    @Query("SELECT * FROM CarInfo ORDER BY id DESC")
    fun CarInfoAll(): List<CarInfo>

    /* 차랑번호 검색 */
    @Query("SELECT * FROM CarInfo WHERE carnumber LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikeCarnumber(searchText: String) : List<CarInfo>

    /* 번호판 검색 숫자만 */
    @Query("SELECT id, carnumber, phone, etc, date FROM CarInfo WHERE carnumberonly = :searchText")
    fun CarInfoSearchByCarnumberOnly(searchText: String) : List<CarInfo>

    /* 번호판 검색 전체 */
    @Query("SELECT id, carnumber, phone, etc, date FROM CarInfo WHERE carnumber = :searchText limit 1")
    fun CarInfoSearchByCarnumber(searchText: String) : List<CarInfo>


    /* 휴대폽번호 검색 */
    @Query("SELECT * FROM CarInfo WHERE phone LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikePhone(searchText: String) : List<CarInfo>

    /* 비고 검색 */
    @Query("SELECT * FROM CarInfo WHERE etc LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikeEtc(searchText: String) : List<CarInfo>


    @Query("DELETE FROM CarInfo")
    fun CarInfoDelete()

    @Query("DELETE FROM CarInfo WHERE id = :id")
    fun CarInfoDeletebyid(id: Int)

    /* 처음 로딩을 위해서 */
    @Query("SELECT  id, carnumber, phone, date, etc, type FROM CarInfoToday ORDER BY id DESC")
    fun CarInfoTodayAll(): LiveData<MutableList<CarInfoToday>>

    /* 데이터 변경을 위해서 */
    @Query("SELECT id, carnumber, phone, date, etc FROM CarInfo ORDER BY id DESC")
    fun CarInfoGetAll(): LiveData<MutableList<CarInfoList>>

    @Query("SELECT id, carnumber, phone, date, etc, type FROM CarInfoToday WHERE carnumber LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoGetTdoday(searchText: String?): LiveData<MutableList<CarInfoToday>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoInsertToday(carinfotoday: CarInfoToday)

    @Query("DELETE FROM CarInfoToday")
    fun CarInfoTodayDelete()

    /* SMS 데이터 목록 가져오기 */
    @Query("SELECT id, smstitle, smscontent FROM SmsManager ORDER BY id DESC")
    fun SmsMagAll(): LiveData<MutableList<SmsManager>>

    /* SMS 데이터 한개 삭제하기 */
    @Query("DELETE FROM SmsManager WHERE id = :id")
    fun SmsMagDeletebyid(id: Int)

    /* SMS 데이터 한개 수정하기 */
    @Update
    fun SmsMagUpdatebyid(vararg smsmanager: SmsManager)

    /* SMS 입력 하기 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun SmsMagInsert(smsmanager: SmsManager)
}
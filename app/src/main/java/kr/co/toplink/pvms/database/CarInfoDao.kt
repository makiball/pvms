package kr.co.toplink.pvms.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kr.co.toplink.pvms.data.CarInfoList

@Dao
interface CarInfoDao {

    @Query("SELECT * FROM CarInfo")
    fun CarInfoAll(): List<CarInfo>

    @Query("SELECT id, carnumber, phone, etc FROM CarInfo ORDER BY id DESC")
    fun CarInfoGetAll(): LiveData<MutableList<CarInfoList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoInsert(carinfo: CarInfo)

    @Query("DELETE FROM CarInfo")
    fun CarInfoDelete()


    @Query("DELETE FROM CarInfo WHERE id = :id")
    fun CarInfoDeletebyid(id: Int)

    /* 차랑번호 검색 */
    @Query("SELECT id, carnumber, phone, etc FROM CarInfo WHERE carnumber LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikeCarnumber(searchText: String?) : List<CarInfo>

    /* 휴대폽번호 검색 */
    @Query("SELECT id, carnumber, phone, etc FROM CarInfo WHERE phone LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikePhone(searchText: String?) : List<CarInfo>

    /* 비고 검색 */
    @Query("SELECT id, carnumber, phone, etc FROM CarInfo WHERE etc LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikeEtc(searchText: String?) : List<CarInfo>

}
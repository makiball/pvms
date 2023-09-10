package kr.co.toplink.pvms.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListToday
import kr.co.toplink.pvms.data.CarInfoRow

@Dao
interface CarInfoDao {

    /* ------------------------------------------------- 차량전체 -----------------------------------------------*/
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
    @Query("SELECT * FROM CarInfo WHERE carnumberonly = :searchText limit 1")
    fun CarInfoSearchByCarnumberOnly(searchText: String) : CarInfo

    /* 번호판 검색 전체 */
    @Query("SELECT * FROM CarInfo WHERE carnumber = :searchText limit 1")
    fun CarInfoSearchByCarnumber(searchText: String) : CarInfo


    /* 휴대폽번호 검색 */
    @Query("SELECT * FROM CarInfo WHERE phone LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikePhone(searchText: String) : List<CarInfo>

    /* 비고 검색 */
    @Query("SELECT * FROM CarInfo WHERE etc LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoSearchLikeEtc(searchText: String) : List<CarInfo>


    @Query("DELETE FROM CarInfo")
    fun CarInfoDelete()

    @Query("DELETE FROM CarInfo WHERE carnumber = :searchText ")
    fun CarInfoDeletebyid(searchText: String)
    /* ------------------------------------------------- 차량전체 -----------------------------------------------*/



    /* ------------------------------------------------- 캠조회 -----------------------------------------------*/
    @Query("SELECT * FROM CarInfoToday ORDER BY id DESC")
    fun CarInfoTodayList():  List<CarInfoToday>

    @Query("SELECT * FROM CarInfoToday WHERE carnumber = :carnum")
    fun CarInfoToday(carnum: String):  CarInfoToday

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoInsertToday(carinfotoday: CarInfoToday)

    @Query("DELETE FROM CarInfoToday")
    fun CarInfoTodayDelete()

    /* SMS 데이터 한개 수정하기 */
    @Update
    fun CarInfoTodayUpdateById(vararg carinfotoday: CarInfoToday)

    @Query("DELETE FROM CarInfoToday WHERE id = :id")
    fun CarInfoTodayDeleteById(id: Int)
    /* ------------------------------------------------- 캠조회 -----------------------------------------------*/


    /* ------------------------------------------------- 주차보고서 -----------------------------------------------*/
    @Query("SELECT * FROM Report ORDER BY id DESC")
    fun ReportList():  List<Report>

    @Query("SELECT * FROM Report WHERE id = :id")
    fun Report(id: Int):  Report

    @Query("SELECT id FROM Report ORDER BY id DESC limit 1")
    fun ReportLastId(): Int

    @Insert
    fun ReportInsert(report: Report)

    @Query("DELETE FROM Report")
    fun ReportDelete()

    @Update
    fun ReportUpdateById(vararg report: Report)

    @Query("DELETE FROM Report WHERE id = :id")
    fun ReportDeleteById(id: Int)

    @Query("DELETE FROM CarInfoTotal WHERE reportnum = :reportid")
    fun CarInfoTotalDelteReportId(reportid : Int)

    @Query("SELECT * FROM CarInfoTotal ORDER BY id DESC")
    fun CarInfoTotalList():  List<CarInfoTotal>

    @Query("SELECT * FROM CarInfoTotal WHERE carnumber = :carnum  ORDER BY id DESC")
    fun CarInfoTotalListCarnum(carnum: String):  List<CarInfoTotal>

    @Query("SELECT * FROM CarInfoTotal WHERE reportnum = :reportid AND carnumber LIKE '%' || :carnum || '%'  ORDER BY id DESC")
    fun CarInfoTotalListId(reportid: Int, carnum: String):  List<CarInfoTotal>

    @Query("SELECT * FROM CarInfoTotal WHERE reportnum = :reportid AND type = :type AND carnumber LIKE '%' || :carnum || '%'  ORDER BY id DESC")
    fun CarInfoTotalListType(reportid: Int, type : Int, carnum: String):  List<CarInfoTotal>

    @Query("SELECT * FROM CarInfoTotal WHERE reportnum = :reportid AND lawstop = 1 AND carnumber LIKE '%' || :carnum || '%'  ORDER BY id DESC")
    fun CarInfoTotalListLaw(reportid: Int, carnum: String):  List<CarInfoTotal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoTotalInsert(carInfoTotal: CarInfoTotal)

    @Query("SELECT * FROM CarInfoTotal WHERE id = :id LIMIT 1")
    fun CarInfoTotalById(id: Int): CarInfoTotal

    @Query("UPDATE Report SET total_type_0 = total_type_0 - :type_0 WHERE id = :id ")
    fun ReportType_0_Increase(id: Int, type_0: Int)

    @Query("UPDATE Report SET total_type_1 = total_type_1 - :type_1 WHERE id = :id ")
    fun ReportType_1_Increase(id: Int, type_1: Int)

    @Query("UPDATE Report SET total_lawstop = total_lawstop - :lawstop WHERE id = :id ")
    fun ReportLawStopIncrease(id: Int, lawstop: Int)

    @Delete
    fun CarInfoTotalDeleteById(carInfoTotal: CarInfoTotal)

    /* ------------------------------------------------- 주차보고서 -----------------------------------------------*/


    /* ------------------------------------------------- 이전버전 -----------------------------------------------*/
    /* 처음 로딩을 위해서 */
    @Query("SELECT  * FROM CarInfoToday ORDER BY id DESC")
    fun CarInfoTodayAll(): LiveData<MutableList<CarInfoToday>>

    /* 데이터 변경을 위해서
    @Query("SELECT id, carnumber, phone, date, etc FROM CarInfo ORDER BY id DESC")
    fun CarInfoGetAll(): LiveData<MutableList<CarInfoList>>
     */

    @Query("SELECT * FROM CarInfoToday WHERE carnumber LIKE '%' || :searchText || '%' ORDER BY id DESC")
    fun CarInfoGetTdoday(searchText: String?): LiveData<MutableList<CarInfoToday>>
    /* ------------------------------------------------- 이전버전 -----------------------------------------------*/

    /* SMS 데이터 목록 가져오기 */
    @Query("SELECT id, smstitle, smscontent FROM SmsManager ORDER BY id DESC")
    fun SmsMagAll(): List<SmsManager>

    @Query("SELECT * FROM SmsManager WHERE id = :id")
    fun SmsManager(id: Int): SmsManager

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
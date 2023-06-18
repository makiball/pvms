package kr.co.toplink.pvms.database

import androidx.room.*

@Dao
interface CarInfoDao {
    @Query("SELECT * FROM CarInfo")
    fun CarInfoGetAll(): List<CarInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoInsert(carinfo: CarInfo)

    @Query("DELETE FROM CarInfo WHERE id = :id")
    fun CarInfoDeletebyid(id: Int)

}
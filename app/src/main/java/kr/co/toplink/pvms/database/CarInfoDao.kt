package kr.co.toplink.pvms.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface CarInfoDao {
    @Query("SELECT * FROM CarInfo")
    fun CarInfoGetAll(): List<CarInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun CarInfoIfnsert(carinfo: CarInfo)

    @Query("DELETE FROM CarInfo WHERE id = :id")
    fun CarInfoDeletebyid(id: Int)

}
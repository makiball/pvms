package kr.co.toplink.pvms.database

import androidx.room.*
import androidx.room.Entity
import kr.co.toplink.pvms.data.Type
import java.util.Date

@Entity(
    indices = [Index(value = ["carnumber"], unique = true)]
)
data class CarInfo(
    @PrimaryKey(autoGenerate =true)
    val id: Int = 0,
    val carnumber: String? = "",      //차량 번호 풀번호
    val carnumber4d: String? = "",    //차량 번호 뒷 4자리
    val carnumberonly: String? = "",  //차량 번호 숫자만
    val phone: String,
    val date: Date,
    val etc: String
)

@Entity(
    indices = [Index(value = ["carnumber"], unique = true)]
)
data class CarInfoToday(
    @PrimaryKey(autoGenerate =true)
    val id: Int = 0,
    val carnumber: String? = "",      //차량 번호 풀번호
    val phone: String,
    val date: Date,
    val etc: String,
    val type: Int = 0     //0 등록차량, 1미등록차량
)

@Entity
data class Report(
    @PrimaryKey(autoGenerate =true)
    val id: Int = 0,
    val date: Date,
    val type_0: Int = 0,
    val type_1: Int = 0
)

@Entity
data class SmsManager(
    @PrimaryKey(autoGenerate =true)
    var id: Int = 0,
    var smstitle: String,     //없으면 외부차량, 있으면 등록 차량
    var smscontent: String
)
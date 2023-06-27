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
    val carnumber: String?,      //차량 번호 풀번호
    val carnumber4d: String?,    //차량 번호 뒷 4자리
    val carnumberonly: String?,  //차량 번호 숫자만
    val phone: String?,
    val date: Date,
    val etc: String?
)

@Entity(
    indices = [Index(value = ["carnumber","date"], unique = true)]
)
data class CarInfoListToday(
    @PrimaryKey(autoGenerate =true)
    val id: Int = 0,
    val carnumber: String?,      //차량 번호 풀번호
    val phone: String?,
    val date: Date,
    val etc: String?,
    val type: Int = 0
)


@Entity
data class CarSearch(
    @PrimaryKey(autoGenerate =true)
    var id: Int = 0,
    var carInfoId: Int,     //없으면 외부차량, 있으면 등록 차량
    var carnumber: String,
    var carnumber4d: String,
    var carnumberOnly: String,
    var phone: String,
    var date: String,
    var etc: String
)

@Entity
data class SmsMsg(
    @PrimaryKey(autoGenerate =true)
    var id: Int = 0,
    var type: Int,     //없으면 외부차량, 있으면 등록 차량
    var carnumber: String
)

@Entity
data class SmsSendLog(
    @PrimaryKey(autoGenerate =true)
    var id: Int = 0,
    var carInfoId: Int,     //없으면 외부차량, 있으면 등록 차량
    var carnumber: String,
    var type: Int,          //없으면 외부차량, 있으면 등록 차량
    var msg: String,
    var date: String
)
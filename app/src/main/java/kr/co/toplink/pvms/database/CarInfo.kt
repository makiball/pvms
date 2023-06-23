package kr.co.toplink.pvms.database

import androidx.room.*
import androidx.room.Entity

@Entity(
    tableName = "carinfo",
    indices = [Index(value = ["carnumber"], unique = true)]
)
data class CarInfo(
    @PrimaryKey(autoGenerate =true)         val id: Int = 0,
    @ColumnInfo(name = "carnumber")         val carnumber: String?,      //차량 번호 풀번호
    @ColumnInfo(name = "carnumber4d")       val carnumber4d: String?,    //차량 번호 뒷 4자리
    @ColumnInfo(name = "carnumberonly")     val carnumberonly: String?,  //차량 번호 숫자만
    @ColumnInfo(name = "phone")             val phone: String?,
    @ColumnInfo(name = "date")              val date: String?,
    @ColumnInfo(name = "etc")               val etc: String?
)

@Entity(
    tableName = "carsearchtoday",
    indices = [Index(value = ["carnumber","date"], unique = true)]
)
data class CarSearchToday(
    @PrimaryKey(autoGenerate =true)         val id: Int = 0,
    @ColumnInfo(name = "carnumber")         val carnumber: String?,      //차량 번호 풀번호
    @ColumnInfo(name = "carnumber4d")       val carnumber4d: String?,    //차량 번호 뒷 4자리
    @ColumnInfo(name = "carnumberonly")     val carnumberonly: String?,  //차량 번호 숫자만
    @ColumnInfo(name = "phone")             val phone: String?,
    @ColumnInfo(name = "date")              val date: String?,
    @ColumnInfo(name = "etc")               val etc: String?
)


@Entity(
    tableName = "carsearch"
)
data class CarSearch(
    @ColumnInfo(name = "carInfoId")         var carInfoId: Int,     //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "carnumber")         var carnumber: String,
    @ColumnInfo(name = "carnumber4d")       var carnumber4d: String,
    @ColumnInfo(name = "carnumberOnly")     var carnumberOnly: String,
    @ColumnInfo(name = "phone")             var phone: String,
    @ColumnInfo(name = "date")              var date: String,
    @Ignore var etc: String
) {
    @PrimaryKey(autoGenerate =true)         var id: Int = 0
}

@Entity(
    tableName = "smsmsg"
)
data class SmsMsg(
    @PrimaryKey(autoGenerate =true)         var id: Int = 0,
    @ColumnInfo(name = "type")              var type: Int,     //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "msg")               var carnumber: String
)

@Entity(
    tableName = "smssendlog"
)
data class SmsSendLog(
    @PrimaryKey(autoGenerate =true)         var id: Int = 0,
    @ColumnInfo(name = "carInfoId")         var carInfoId: Int,     //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "carnumber")         var carnumber: String,
    @ColumnInfo(name = "type")              var type: Int,          //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "msg")               var msg: String,
    @ColumnInfo(name = "date")              var date: String
)
package kr.co.toplink.pvms.database

import androidx.room.*

@Entity(
    tableName = "car-info",
    indices = [Index(value = ["carnumber"], unique = true)]
)
data class CarInfo(
    @PrimaryKey(autoGenerate =true)         var id: Int = 0,
    @ColumnInfo(name = "carnumber")         var carnumber: String,      //차량 번호 풀번호
    @ColumnInfo(name = "carnumber4d")       var carnumber4d: String,    //차량 번호 뒷 4자리
    @ColumnInfo(name = "carnumberOnly")     var carnumberOnly: String,  //차량 번호 숫자만
    @ColumnInfo(name = "phone")             var phone: String,
    @ColumnInfo(name = "date")              var date: String,
    @Ignore var etc: String
 )

@Entity(
    tableName = "car-search"
)
data class CarSearch(
    @PrimaryKey(autoGenerate =true)         var id: Int = 0,
    @ColumnInfo(name = "carInfoId")         var carInfoId: Int,     //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "carnumber")         var carnumber: String,
    @ColumnInfo(name = "carnumber4d")       var carnumber4d: String,
    @ColumnInfo(name = "carnumberOnly")     var carnumberOnly: String,
    @ColumnInfo(name = "phone")             var phone: String,
    @ColumnInfo(name = "date")              var date: String,
    @Ignore var etc: String
)

@Entity(
    tableName = "sms-msg"
)
data class SmsMsg(
    @PrimaryKey(autoGenerate =true)         var id: Int = 0,
    @ColumnInfo(name = "type")              var type: Int,     //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "msg")               var carnumber: String
)

@Entity(
    tableName = "sms-send-log"
)
data class SmsSendLog(
    @PrimaryKey(autoGenerate =true)         var id: Int = 0,
    @ColumnInfo(name = "carInfoId")         var carInfoId: Int,     //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "carnumber")         var carnumber: String,
    @ColumnInfo(name = "type")              var type: Int,     //없으면 외부차량, 있으면 등록 차량
    @ColumnInfo(name = "msg")               var msg: String,
    @ColumnInfo(name = "date")              var date: String
)
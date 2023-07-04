package kr.co.toplink.pvms.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Singleton

@Database(entities = [CarInfo::class, CarInfoToday::class, SmsManager::class], version = 3)
@TypeConverters(Converters::class)
abstract class CarInfoDatabase: RoomDatabase() {
    abstract fun CarInfoDao(): CarInfoDao

    companion object {
        private var instance: CarInfoDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CarInfoDatabase? {
            if (instance == null) {
                synchronized(CarInfoDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CarInfoDatabase::class.java,
                        "carinfo-database"
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            val nowDate = Date()

                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(context)?.CarInfoDao()?.SmsMagInsert(
                                    SmsManager (
                                        id = 1,
                                        smstitle = "불법주차",
                                        smscontent = "입주민 차량이 불법 구역에 주차되어 있어 타 차량에 불편함이 있습니다. 즉시 다른 지역으로 이동 주차 바랍니다. 000 관리 사무소"
                                    )
                                )
                                getInstance(context)?.CarInfoDao()?.SmsMagInsert(
                                    SmsManager (
                                        id = 2,
                                        smstitle = "이동주차",
                                        smscontent = "등록되지 않은 차량입니다. 즉시 다른 주차장으로 이동 바라며 이의제기는 관리사무소에 문의 주시기 바랍니다.  000 관리 사무소"
                                    )
                                )
                                getInstance(context)?.CarInfoDao()?.SmsMagInsert(
                                    SmsManager (
                                        id = 3,
                                        smstitle = "차량이상",
                                        smscontent = "입주민 차량에 (문열림, 트렁크) 열림등으로 이상이 발생하여 문자 보내드립니다. 차량 확인 해보세요. 000 관리 사무소"
                                    )
                                )

                                getInstance(context)?.CarInfoDao()?.CarInfoInsert(
                                    CarInfo (
                                        id = 1,
                                        carnumber = "000가0000",
                                        carnumber4d = "0000",
                                        carnumberonly = "000 0000",
                                        phone = "",
                                        date = nowDate,
                                        etc = ""
                                    )
                                )

                            }

                        }
                    }).build()
                }
            }
            return instance
        }
    }
}
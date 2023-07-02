package kr.co.toplink.pvms.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
                    )
                        .build()
                }
            }
            return instance
        }
    }
}
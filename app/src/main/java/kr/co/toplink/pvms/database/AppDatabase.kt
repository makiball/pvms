package kr.co.toplink.pvms.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CarInfo::class, CarInfoToday::class, SmsManager::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carinfoDao(): CarInfoDao
}
package kr.co.toplink.pvms.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.co.toplink.pvms.camerax.GraphicOverlay

@Database(entities = [CarInfo::class], version = 1)
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
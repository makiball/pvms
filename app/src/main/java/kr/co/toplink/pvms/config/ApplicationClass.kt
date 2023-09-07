package kr.co.toplink.pvms.config

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.BuildConfig
import kr.co.toplink.pvms.database.AppDatabase
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoDatabase
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.repository.cam.CamLocalDataSource
import kr.co.toplink.pvms.repository.cam.CamRepository
import kr.co.toplink.pvms.repository.car.CarLocalDataSource
import kr.co.toplink.pvms.repository.car.CarRepository
import kr.co.toplink.pvms.repository.report.ReportLocalDataSource
import kr.co.toplink.pvms.repository.report.ReportRepository
import kr.co.toplink.pvms.repository.sms.SmsLocalDataSource
import kr.co.toplink.pvms.repository.sms.SmsRepository
import kr.co.toplink.pvms.util.SharedPreferencesUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {

    private val TAG = this.javaClass.simpleName

    companion object {
        const val SERVER_URL = BuildConfig.BASE_URL

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil

        lateinit var retrofit: Retrofit

        fun makeRetrofit(url : String) : Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(okHttpClient)
                .build()

            return retrofit
        }
    }

    private var database: AppDatabase? = null
    fun provideDatabase(applicaContext: Context): AppDatabase {
        return database ?: kotlin.run {
            Room.databaseBuilder(
                applicaContext,
                AppDatabase::class.java,
                "pvms-local"
            ).fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        CoroutineScope(Dispatchers.IO).launch {
                            provideDatabase(applicaContext).carinfoDao().SmsMagInsert(
                                SmsManager (
                                    id = 1,
                                    smstitle = "불법주차",
                                    smscontent = "입주민 차량이 불법 구역에 주차되어 있어 타 차량에 불편함이 있습니다. 즉시 다른 지역으로 이동 주차 바랍니다."
                                )
                            )

                            provideDatabase(applicaContext).carinfoDao().SmsMagInsert(
                                SmsManager (
                                    id = 2,
                                    smstitle = "이동주차",
                                    smscontent = "등록되지 않은 차량입니다. 즉시 다른 주차장으로 이동 바라며 이의제기는 관리사무소에 문의 주시기 바랍니다."
                                )
                            )

                            provideDatabase(applicaContext).carinfoDao().SmsMagInsert(
                                SmsManager (
                                    id = 3,
                                    smstitle = "차량이상",
                                    smscontent = "입주민 차량에 (문열림, 트렁크) 열림등으로 이상이 발생하여 문자 보내드립니다. 차량 확인 해보세요."
                                )
                            )
                        }

                    }
                }).build().also {
                    database = it
            }
        }
    }

    private var carRepository: CarRepository? = null
    fun provideCarRepository(context: Context): CarRepository {
        return carRepository ?: kotlin.run {
            val dao = provideDatabase(context.applicationContext).carinfoDao()
            CarRepository(CarLocalDataSource(dao)).also {
                carRepository = it
            }
        }
    }

    private var camRepository: CamRepository? = null
    fun provideCamRepository(context: Context): CamRepository {
        return camRepository ?: kotlin.run {
            val dao = provideDatabase(context.applicationContext).carinfoDao()
            CamRepository(CamLocalDataSource(dao)).also {
                camRepository = it
            }
        }
    }

    private var smsRepository: SmsRepository? = null
    fun provideSmsRepository(context: Context) : SmsRepository {
        return smsRepository ?: kotlin.run {
            val dao = provideDatabase(context.applicationContext).carinfoDao()
            SmsRepository(SmsLocalDataSource(dao)).also {
                smsRepository = it
            }
        }
    }

    private var reportRepository: ReportRepository? = null
    fun provideReportRepository(context: Context) : ReportRepository {
        return reportRepository ?: kotlin.run {
            val dao = provideDatabase(context.applicationContext).carinfoDao()
            ReportRepository(ReportLocalDataSource(dao)).also {
                reportRepository = it
            }
        }
    }

    override fun onCreate() {
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)
        super.onCreate()
        makeRetrofit(SERVER_URL)
    }
}
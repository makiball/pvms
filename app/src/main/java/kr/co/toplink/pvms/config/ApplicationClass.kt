package kr.co.toplink.pvms.config

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import kr.co.toplink.pvms.BuildConfig
import kr.co.toplink.pvms.database.AppDatabase
import kr.co.toplink.pvms.repository.car.CarLocalDataSource
import kr.co.toplink.pvms.repository.car.CarRepository
import kr.co.toplink.pvms.util.SharedPreferencesUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
                .build().also {
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

    override fun onCreate() {
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)
        super.onCreate()
        makeRetrofit(SERVER_URL)
    }
}
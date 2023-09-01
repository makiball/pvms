package kr.co.toplink.pvms.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.CamCarSearchActivity
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListToday
import kr.co.toplink.pvms.data.Option
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoDatabase
import kr.co.toplink.pvms.database.CarInfoToday

class CarNumberSearchViewModel   : BaseViewModel() {

    private val TAG = this.javaClass.simpleName
    private lateinit var db: CarInfoDatabase
    var carinfoList: MutableLiveData<List<CarInfoList>> = MutableLiveData()
    var carinfoListToday: LiveData<MutableList<CarInfoToday>> = MutableLiveData()
    var carMsgData: MutableLiveData<String> = MutableLiveData()

    private val list = ArrayList<CarInfoList>()
    private var listtoday = ArrayList<CarInfoListToday>()

    val selectedOption: MutableLiveData<Option> = MutableLiveData()

    /* 체크박스 */
    fun setSelectedOption(option: Option) {
        selectedOption.value = option
    }

    /* 차량번호 검색 */
    fun allDeteData(context: Context) {
        list.clear()
        launch {
            db.CarInfoDao().CarInfoDelete()
        }
        carinfoList.postValue(list)
    }

    /* 차량번호 검색 */
    fun allTodayDeteData(context: Context) {
        list.clear()
        launch {
            db.CarInfoDao().CarInfoTodayDelete()
        }
        carinfoList.postValue(list)
    }

    /* 금일차량검색 */
    fun searchCarnumToday(context: Context, searchtext: String) {
        db = CarInfoDatabase.getInstance(context)!!
        /* ROOM 실시간 라이브면 사용안해도됨 */
        carinfoListToday = db.CarInfoDao().CarInfoGetTdoday("")
    }

    /* 차량번호 검색 */
    fun searchCarnum(context: Context, searchtext: String) {
        list.clear()
        db = CarInfoDatabase.getInstance(context)!!
        try {
            launch {
                val carinfos = CoroutineScope(Dispatchers.IO).async {
                    db.CarInfoDao().CarInfoSearchLikeCarnumber(searchtext)
                }.await()

                for (carinfo in carinfos) {
                    list.add(
                        CarInfoList(
                            carinfo.id,
                            carinfo.carnumber,
                            carinfo.phone,
                            carinfo.date,
                            carinfo.etc
                        )
                    )
                }
                carinfoList.postValue(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            carMsgData.postValue(e.message.orEmpty())
        }
    }

    /* 휴대폰번호 검색 */
    fun searchPhone(context: Context, searchtext: String) {
        list.clear()
        db = CarInfoDatabase.getInstance(context)!!
        try {
            launch {
                val carinfos = CoroutineScope(Dispatchers.IO).async {
                    db.CarInfoDao().CarInfoSearchLikePhone(searchtext)
                }.await()
                for (carinfo in carinfos) {
                    list.add(
                        CarInfoList(
                            carinfo.id,
                            carinfo.carnumber,
                            carinfo.phone,
                            carinfo.date,
                            carinfo.etc,
                        )
                    )
                }
                carinfoList.postValue(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            carMsgData.postValue(e.message.orEmpty())
        }
    }

    /* 비고 검색 */
    fun searchEtc(context: Context, searchtext: String) {
        list.clear()
        db = CarInfoDatabase.getInstance(context)!!
        try {
            launch {
                val carinfos = CoroutineScope(Dispatchers.IO).async {
                    db.CarInfoDao().CarInfoSearchLikeEtc(searchtext)
                }.await()
                for (carinfo in carinfos) {
                    list.add(
                        CarInfoList(
                            carinfo.id,
                            carinfo.carnumber,
                            carinfo.phone,
                            carinfo.date,
                            carinfo.etc
                        )
                    )
                }
                carinfoList.postValue(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            carMsgData.postValue(e.message.orEmpty())
        }
    }

    /* 하나 삭제 */
    fun idDeteData(context: Context, id: Int) {
        /*
        db = CarInfoDatabase.getInstance(context)!!
        try {
            launch {
                //val dataToDelete = db.CarInfoDao().CarInfoDeletebyid(carinfo = CarInfo())
                db.CarInfoDao().CarInfoDeletebyid(id)
            }

            carinfoList.postValue(list)

        } catch (e: Exception) {
            e.printStackTrace()
            carMsgData.postValue(e.message.orEmpty())
        }

            val carinfos = CoroutineScope(Dispatchers.IO).async {
                db.CarInfoDao().CarInfoSearchLikeCarnumber("")
            }.await()
            for (carinfo in carinfos) {
                list.add(
                    CarInfoList(
                        carinfo.id,
                        carinfo.carnumber,
                        carinfo.phone,
                        carinfo.date,
                        carinfo.etc
                    )
                )
            }
 */
    }

    /* 차량검색 전부 삭제 */
    fun CarInfoTodayDelete() {
        try {
            launch {
                db.CarInfoDao().CarInfoTodayDelete()
            }
        }catch (e: Exception) {
            e.printStackTrace()
            carMsgData.postValue(e.message.orEmpty())
        }
    }


    /* 전체 검색 */
}
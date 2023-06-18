package kr.co.toplink.pvms.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.database.CarInfoDatabase

class CarNumberSearchViewModel : BaseViewModel() {

    private val TAG = this.javaClass.simpleName

    private lateinit var db: CarInfoDatabase

    var carinfoList: MutableLiveData<List<CarInfoList>> = MutableLiveData()
    var carMsgData: MutableLiveData<String> = MutableLiveData()
    private val list = ArrayList<CarInfoList>()

    /* 차량번호 검색 */
    fun allDeteData(context: Context) {
        list.clear()
        launch {
            db.CarInfoDao().CarInfoDelete()
        }
        carinfoList.postValue(list)
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
                            carinfo.etc,
                            carinfo.date
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
                            carinfo.etc,
                            carinfo.date
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
                            carinfo.etc,
                            carinfo.date
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
        list.clear()
        launch {
            db.CarInfoDao().CarInfoDeletebyid(id)

            val carinfos = CoroutineScope(Dispatchers.IO).async {
                db.CarInfoDao().CarInfoSearchLikeCarnumber("")
            }.await()
            for (carinfo in carinfos) {
                list.add(
                    CarInfoList(
                        carinfo.id,
                        carinfo.carnumber,
                        carinfo.phone,
                        carinfo.etc,
                        carinfo.date
                    )
                )
            }
            carinfoList.postValue(list)
        }
    }



    /* 전체 검색 */
}
package kr.co.toplink.pvms.model

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListToday
import kr.co.toplink.pvms.data.Option
import kr.co.toplink.pvms.database.CarInfoDatabase

class CamCarSearchViewModel : BaseViewModel() {
    val onShutterButtonEvent: MutableLiveData<Unit?> = MutableLiveData()
    private val TAG = this.javaClass.simpleName

    private lateinit var db: CarInfoDatabase

    var carinfoListToday: LiveData<MutableList<CarInfoListToday>> = MutableLiveData()

    var carMsgData: MutableLiveData<String> = MutableLiveData()
    private val list = ArrayList<CarInfoListToday>()

    /* 스위치박스 */
    fun setSelectedSwitch(option: Option) {
        //selectedOption.value = option
    }

    /* 차량번호 검색 */
    fun searchCarnum(context: Context, searchtext: String) {
        list.clear()
        db = CarInfoDatabase.getInstance(context)!!
        try {
            launch {
                val carinfos = CoroutineScope(Dispatchers.IO).async {
                    db.CarInfoDao().CarInfoGetTdoday()
                }.await()
                for (carinfo in carinfos) {
                    list.add(
                        CarInfoListToday(
                            carinfo.id,
                            carinfo.carnumber,
                            carinfo.phone,
                            carinfo.date,
                            carinfo.type
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
}
package kr.co.toplink.pvms.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.co.toplink.pvms.data.Option
import kr.co.toplink.pvms.database.CarInfoDatabase
import kr.co.toplink.pvms.database.CarInfoToday

class CamCarSearchViewModel(application: Application) : ViewModel() {

    private val TAG = this.javaClass.simpleName
    private lateinit var db: CarInfoDatabase
    var carinfoListToday: LiveData<MutableList<CarInfoToday>> = MutableLiveData()
    var carMsgData: MutableLiveData<String> = MutableLiveData()
    private val list = ArrayList<CarInfoToday>()

    init {
        db = CarInfoDatabase.getInstance(application)!!
        carinfoListToday = db.CarInfoDao().CarInfoGetTdoday("")
    }

    /* 스위치박스 */
    fun setSelectedSwitch(option: Option) {
        //selectedOption.value = option
    }

    /* 차량번호 검색 */
    fun searchCarnum(context: Context, searchtext: String) {

    }
}
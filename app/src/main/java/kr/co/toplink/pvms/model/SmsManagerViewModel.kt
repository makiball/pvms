package kr.co.toplink.pvms.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.SmsManagerList
import kr.co.toplink.pvms.database.CarInfoDatabase
import kr.co.toplink.pvms.database.SmsManager

class SmsManagerViewModel : BaseViewModel() {

    private val TAG = this.javaClass.simpleName
    private lateinit var db: CarInfoDatabase

    var smsManagerList: LiveData<MutableList<SmsManager>> = MutableLiveData()
    var smsMsgData: MutableLiveData<String> = MutableLiveData()
    private val list = ArrayList<SmsManagerList>()

    /* 문자 리스트 검색 */
    fun allListSms(context: Context) {
        db = CarInfoDatabase.getInstance(context)!!

        /* ROOM 실시간 라이브면 사용안해도됨 */
        //smsManagerList = db.CarInfoDao().SmsMagAll()
    }

    /* 문자 입력 */
    fun deletebyid(context: Context, id : Int) {
        db = CarInfoDatabase.getInstance(context)!!
        try {
            launch {
                db.CarInfoDao().SmsMagDeletebyid(id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            smsMsgData.postValue(e.message.orEmpty())
        }
    }

}
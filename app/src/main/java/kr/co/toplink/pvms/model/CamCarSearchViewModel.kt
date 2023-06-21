package kr.co.toplink.pvms.model

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CamCarSearchViewModel : ViewModel() {
    val onShutterButtonEvent: MutableLiveData<Unit?> = MutableLiveData()
    private val TAG = this.javaClass.simpleName

    fun onClickShutter(view: View) {

        Log.d(TAG,"=====> ")

        onShutterButtonEvent.postValue(Unit)
    }
}
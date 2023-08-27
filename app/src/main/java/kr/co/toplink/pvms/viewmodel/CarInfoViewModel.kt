package kr.co.toplink.pvms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.Event
import kr.co.toplink.pvms.data.Option
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.repository.car.CarRepository

class CarInfoViewModel(private val carRepository: CarRepository) : ViewModel() {

    private val _carinfos = MutableLiveData<List<CarInfo>>()
    val carinfos: LiveData<List<CarInfo>> = _carinfos

    private val _searchChk = MutableLiveData<Event<Option>>()
    var searchChk: LiveData<Event<Option>> = _searchChk

    /* 체크박스 */
    fun setSelectedOption(option: Option) {
        viewModelScope.launch {
            _searchChk.value = Event(option)
        }
    }

    fun addCarInfo(carinfo : CarInfo) {
        viewModelScope.launch {
            carRepository.addCarInfo(carinfo)
        }
    }

    fun getCarInfo() {
        viewModelScope.launch {
            val carinfo = carRepository.getCarInfo()
            _carinfos.value = carinfo
        }
    }
}
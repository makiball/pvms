package kr.co.toplink.pvms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.Event
import kr.co.toplink.pvms.data.regSwitch
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.repository.cam.CamRepository
import kr.co.toplink.pvms.repository.car.CarRepository
import java.util.*

class CamCarViewModel(private val camRepository: CamRepository) : ViewModel() {

    private val _camcarinfos = MutableLiveData<Event<List<CarInfoToday>>>()
    val camcarinfos: LiveData<Event<List<CarInfoToday>>> = _camcarinfos

    private val _camcarinfo = MutableLiveData<Event<CarInfoToday>>()
    val camcarinfo: LiveData<Event<CarInfoToday>> = _camcarinfo

    private val _carinfo = MutableLiveData<Event<CarInfo>>()
    val carinfo: LiveData<Event<CarInfo>> = _carinfo

    private val _regSwitch = MutableLiveData<regSwitch>()
    val regSwitch: LiveData<regSwitch> = _regSwitch

    fun regSwitch(regSwitch: regSwitch) {
        viewModelScope.launch {
            _regSwitch.value = regSwitch
        }
    }

    fun carInfoSearchByCarnumberOnly(carnum: String) {
        viewModelScope.launch {

            val carinfo = camRepository.carInfoSearchByCarnumberOnly(carnum)
            _carinfo.value = Event(carinfo)

            /*
            if(carinfo.carnumber != "") {
                val carinfotoday = CarInfoToday(
                    carnumber = carinfo.carnumber,
                    phone = carinfo.phone,
                    date = datepatterned,
                    etc = carinfo.etc,
                    type = 0
                )

                carInfoInsertToday(carinfotoday)


            }
             */
        }
    }


    fun CarInfoSearchByCarnumber(carnum: String) {
        viewModelScope.launch {

            val datepatterned = Date()

            val carinfo = camRepository.carInfoSearchByCarnumber(carnum)
            _carinfo.value = Event(carinfo)
            /*
            if(carinfo.carnumber != "") {
                val carinfotoday = CarInfoToday(
                    carnumber = carinfo.carnumber,
                    phone = carinfo.phone,
                    date = datepatterned,
                    etc = carinfo.etc,
                    type = 0
                )

                carInfoInsertToday(carinfotoday)

                _carinfo.value = Event(carinfo)
            }
             */
        }
    }


    fun carInfoTodayList() {
        viewModelScope.launch {
            val camcarinfos = camRepository.carInfoTodayList()
            _camcarinfos.value = Event(camcarinfos)
        }
    }

    fun carInfoToday(carnum: String) {
        viewModelScope.launch {
            val camcarinfo = camRepository.carInfoToday(carnum)
            _camcarinfo.value = Event(camcarinfo)
        }
    }

    fun carInfoInsertToday(carinfotoday: CarInfoToday) {
        viewModelScope.launch {
           camRepository.carInfoInsertToday(carinfotoday)
            carInfoTodayList()
        }
    }

    fun carInfoTodayDelete() {
        viewModelScope.launch {
            camRepository.carInfoTodayDelete()
            carInfoTodayList()
        }
    }

    fun carInfoTodayUpdateById(carinfotoday: CarInfoToday) {
        viewModelScope.launch {
            camRepository.carInfoTodayUpdateById(carinfotoday)
        }
    }

    fun carInfoTodayDeleteById(id: Int) {
        viewModelScope.launch {
            camRepository.carInfoTodayDeleteById(id)
        }
    }
}
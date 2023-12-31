package kr.co.toplink.pvms.viewmodel

import android.telephony.SmsMessage
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.repository.sms.SmsRepository
import kr.co.toplink.pvms.Event

class SmsMngViewModel(private val smsRepository: SmsRepository) : ViewModel() {

    private val _smsinfos = MutableLiveData<List<SmsManager>>()
    val smsinfos: LiveData<List<SmsManager>> = _smsinfos

    private val _smsinfo = MutableLiveData<List<SmsManager>>()
    val smsinfo: LiveData<List<SmsManager>> = _smsinfo

    private val _smsManager = MutableLiveData<Event<SmsManager>>()
    val smsManager: LiveData<Event<SmsManager>> = _smsManager

    fun smsMagAll() {
        viewModelScope.launch {
            val smsinfo = smsRepository.smsMagAll()
            _smsinfos.value = smsinfo
        }
    }

    fun smsManager(id: Int) {
        viewModelScope.launch {
            val smsManager = smsRepository.smsManager(id)
            _smsManager.value = Event(smsManager)
        }
    }

    fun smsMagDeletebyid(id: Int) {
        viewModelScope.launch {
            smsRepository.smsMagDeletebyid(id)
            smsMagAll()
        }
    }

    fun smsMagUpdatebyid(smsmanager: SmsManager) {
        viewModelScope.launch {
            smsRepository.smsMagUpdatebyid(smsmanager)
            smsMagAll()
        }
    }

    fun smsMagInsert(smsmanager: SmsManager) {
        viewModelScope.launch {
            smsRepository.smsMagInsert(smsmanager)
            smsMagAll()
        }
    }
}
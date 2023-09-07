package kr.co.toplink.pvms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.Event
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.database.Report
import kr.co.toplink.pvms.repository.cam.CamRepository
import kr.co.toplink.pvms.repository.report.ReportRepository

class ReportCarViewModel(private val reportRepository: ReportRepository) : ViewModel() {

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>> = _reports

    private val _report = MutableLiveData<Event<Report>>()
    val report: LiveData<Event<Report>> = _report

    private val _carInfoTotals = MutableLiveData<Event<List<CarInfoTotal>>>()
    val carInfoTotals: LiveData<Event<List<CarInfoTotal>>> = _carInfoTotals

    private val _carInfoTotal = MutableLiveData<Event<CarInfoTotal>>()
    val carInfoTotal: LiveData<Event<CarInfoTotal>> = _carInfoTotal

    private val _lastid = MutableLiveData<Event<Int>>()
    val lastid: LiveData<Event<Int>> = _lastid

    fun reportList() {
       viewModelScope.launch {
           val reports = reportRepository.reportList()
           _reports.value = reports
        }
    }

    fun reportLastId() {
        viewModelScope.launch {
            val lastid = reportRepository.reportLastId()
            _lastid.value = Event(lastid)
        }
    }

    fun report(id: Int) {
        viewModelScope.launch {
            val report = reportRepository.report(id)
            _report.value = Event(report)
        }
    }

    fun reportInsert(report: Report) {
        viewModelScope.launch {
            reportRepository.reportInsert(report)
        }
    }

    fun ReportDelete() {
        viewModelScope.launch {
            reportRepository.reportDelete()
        }
    }

    fun reportUpdateById(report: Report)  {
        viewModelScope.launch {
            reportRepository.reportUpdateById(report)
        }
    }

    fun reportDeleteById(id: Int)  {
        viewModelScope.launch {
            reportRepository.reportDeleteById(id)
        }
    }

    fun carInfoTotalList()  {
        viewModelScope.launch {
            reportRepository.carInfoTotalList()
        }
    }

    fun carInfoTotalInsert(carInfoTotal: CarInfoTotal)  {
        viewModelScope.launch {
            reportRepository.carInfoTotalInsert(carInfoTotal)
        }
    }

}
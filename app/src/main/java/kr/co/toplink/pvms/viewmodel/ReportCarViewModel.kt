package kr.co.toplink.pvms.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _carInfoTotals = MutableLiveData<List<CarInfoTotal>>()
    val carInfoTotals: LiveData<List<CarInfoTotal>> = _carInfoTotals

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
            reportList()
        }
    }

    fun carInfoTotalDelteReportId(reportid: Int)  {
        viewModelScope.launch {
            reportRepository.carInfoTotalDelteReportId(reportid)
        }
    }

    fun carInfoTotalList()  {
        viewModelScope.launch {
            val carInfoTotals = reportRepository.carInfoTotalList()
            _carInfoTotals.value = carInfoTotals
        }
    }

    fun carInfoTotalListCarnum(carnum: String) {
        viewModelScope.launch {
            val carInfoTotals = reportRepository.carInfoTotalListCarnum(carnum)
            _carInfoTotals.value = carInfoTotals
        }
    }

    // 전체 자동차 번호 조회
    fun carInfoTotalListId(reportid: Int, carnum: String) {
        viewModelScope.launch {
            val carInfoTotals = reportRepository.carInfoTotalListId(reportid, carnum)
            _carInfoTotals.value = carInfoTotals
        }
    }

    // 등록, 미등록 자동차 번호 조회
    fun carInfoTotalListType(reportid: Int, type : Int, carnum: String) {
        viewModelScope.launch {
            val carInfoTotals = reportRepository.carInfoTotalListType(reportid, type, carnum)
            _carInfoTotals.value = carInfoTotals
        }
    }

    // 불법주차 번호 조회
    fun carInfoTotalListLaw(reportid: Int, carnum: String) {
        viewModelScope.launch {
            val carInfoTotals = reportRepository.carInfoTotalListLaw(reportid, carnum)
            _carInfoTotals.value = carInfoTotals
        }
    }

    fun carInfoTotalInsert(carInfoTotal: CarInfoTotal)  {
        viewModelScope.launch {
            reportRepository.carInfoTotalInsert(carInfoTotal)
        }
    }

    fun carInfoTotalById(id: Int) {
        viewModelScope.launch {
            val carInfoTotal = reportRepository.carInfoTotalById(id)
            _carInfoTotal.value = Event(carInfoTotal)
        }
    }

    fun reportType_0_Increase(id: Int, type_0: Int) {
        viewModelScope.launch {
            reportRepository.reportType_0_Increase(id, type_0)
        }
    }

    fun reportType_1_Increase(id: Int, type_1: Int) {
        viewModelScope.launch {
            reportRepository.reportType_1_Increase(id, type_1)
        }
    }

    fun reportLawStopIncrease(id: Int, lawstop: Int) {
        viewModelScope.launch {
            reportRepository.reportLawStopIncrease(id, lawstop)
        }
    }


    fun carInfoTotalDeleteById(carInfoTotal : CarInfoTotal) {
        viewModelScope.launch {
            reportRepository.carInfoTotalDeleteById(carInfoTotal)
        }
    }

}
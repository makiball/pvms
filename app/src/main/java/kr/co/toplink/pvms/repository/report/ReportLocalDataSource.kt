package kr.co.toplink.pvms.repository.report

import kr.co.toplink.pvms.database.CarInfoDao
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.database.Report
import kr.co.toplink.pvms.repository.car.CarDataSource

class ReportLocalDataSource(
    private val dao : CarInfoDao
) : ReportDataSource {
    override suspend fun reportList(): List<Report> {
       return dao.ReportList()
    }

    override suspend fun reportLastId(): Int {
        return dao.ReportLastId()
    }

    override suspend fun report(id: Int): Report {
        return dao.Report(id)
    }

    override suspend fun reportInsert(report: Report) {
        dao.ReportInsert(report)
    }

    override suspend fun reportDelete() {
        dao.ReportDelete()
    }

    override suspend fun reportUpdateById(report: Report) {
        dao.ReportUpdateById(report)
    }

    override suspend fun reportDeleteById(id: Int) {
        dao.ReportDeleteById(id)
    }

    override suspend fun carInfoTotalDelteReportId(reportid: Int){
        dao.ReportDeleteById(reportid)
    }

    override suspend fun carInfoTotalList(): List<CarInfoTotal> {
        return dao.CarInfoTotalList()
    }

    override suspend fun carInfoTotalListCarnum(carnum: String):  List<CarInfoTotal> {
        return dao.CarInfoTotalListCarnum(carnum)
    }

    override suspend fun carInfoTotalListId(reportid: Int, carnum: String):  List<CarInfoTotal> {
        return dao.CarInfoTotalListId(reportid, carnum)
    }


    override suspend fun carInfoTotalListType(reportid: Int, type: Int, carnum: String):  List<CarInfoTotal> {
        return dao.CarInfoTotalListType(reportid, type, carnum)
    }

    override suspend fun carInfoTotalListLaw(reportid: Int, carnum: String):  List<CarInfoTotal> {
        return dao.CarInfoTotalListLaw(reportid, carnum)
    }

    override suspend fun carInfoTotalInsert(carInfoTotal: CarInfoTotal) {
        dao.CarInfoTotalInsert(carInfoTotal)
    }
}
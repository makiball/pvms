package kr.co.toplink.pvms.repository.report

import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.database.Report

interface ReportDataSource {

    suspend fun reportList():  List<Report>

    suspend fun reportLastId(): Int

    suspend fun report(id: Int):  Report

    suspend fun reportInsert(report: Report)

    suspend fun reportDelete()

    suspend fun reportUpdateById(report: Report)

    suspend fun reportDeleteById(id: Int)

    suspend fun carInfoTotalList():  List<CarInfoTotal>

    suspend fun carInfoTotalInsert(carInfoTotal: CarInfoTotal)
}
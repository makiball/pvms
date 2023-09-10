package kr.co.toplink.pvms.repository.report

import androidx.room.Query
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

    suspend fun carInfoTotalDelteReportId(reportid: Int)

    suspend fun carInfoTotalList():  List<CarInfoTotal>

    suspend fun carInfoTotalListCarnum(carnum: String):  List<CarInfoTotal>

    suspend fun carInfoTotalListId(reportid: Int, carnum: String):  List<CarInfoTotal>

    suspend fun carInfoTotalListType(reportid: Int, type : Int, carnum: String):  List<CarInfoTotal>

    suspend fun carInfoTotalListLaw(reportid: Int, carnum: String):  List<CarInfoTotal>

    suspend fun carInfoTotalInsert(carInfoTotal: CarInfoTotal)

    suspend fun carInfoTotalById(id: Int): CarInfoTotal

    suspend fun reportType_0_Increase(id: Int, type_0: Int)

    suspend fun reportType_1_Increase(id: Int, type_1: Int)

    suspend fun reportLawStopIncrease(id: Int, lawstop: Int)

    suspend fun carInfoTotalDeleteById(carInfoTotal: CarInfoTotal)

}
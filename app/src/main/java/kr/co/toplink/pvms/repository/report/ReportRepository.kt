package kr.co.toplink.pvms.repository.report

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.database.Report
import kr.co.toplink.pvms.repository.car.CarLocalDataSource

class ReportRepository(
    private val localDataSource: ReportDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun reportList():  List<Report> {
        return withContext(ioDispatcher) {
            localDataSource.reportList()
        }
    }

    suspend fun reportLastId(): Int {
        return withContext(ioDispatcher) {
            localDataSource.reportLastId()
        }
    }


    suspend fun report(id: Int): Report {
        return   withContext(ioDispatcher) {
            localDataSource.report(id)
        }
    }

    suspend fun reportInsert(report: Report) {
        withContext(ioDispatcher) {
            val reportData = Report(
                date = report.date,
                total_type_0 = report.total_type_0,
                total_type_1 = report.total_type_1,
                total_lawstop = report.total_lawstop
            )
            localDataSource.reportInsert(reportData)
        }
    }

    suspend fun reportDelete() {
        withContext(ioDispatcher) {
            localDataSource.reportDelete()
        }
    }

    suspend fun reportUpdateById(report: Report)  {
        withContext(ioDispatcher) {
            val reportData = Report(
                id  = report.id,
                date = report.date,
                total_type_0 = report.total_type_0,
                total_type_1 = report.total_type_1,
                total_lawstop = report.total_lawstop
            )
            localDataSource.reportInsert(reportData)
        }
    }

    suspend fun reportDeleteById(id: Int) {
        withContext(ioDispatcher) {
            localDataSource.reportDeleteById(id)
        }
    }

    suspend fun carInfoTotalDelteReportId(reportid: Int) {
        withContext(ioDispatcher) {
            localDataSource.reportDeleteById(reportid)
        }
    }

    suspend fun carInfoTotalList():  List<CarInfoTotal>  {
        return withContext(ioDispatcher) {
            localDataSource.carInfoTotalList()
        }
    }

    suspend fun carInfoTotalListCarnum(carnum: String):  List<CarInfoTotal> {
        return withContext(ioDispatcher) {
            localDataSource.carInfoTotalListCarnum(carnum)
        }
    }

    suspend fun carInfoTotalListId(reportid: Int, carnum: String):  List<CarInfoTotal> {
        return withContext(ioDispatcher) {
            localDataSource.carInfoTotalListId(reportid, carnum)
        }
    }


    suspend fun carInfoTotalListType(reportid: Int, type: Int, carnum: String):  List<CarInfoTotal> {
        return withContext(ioDispatcher) {
            localDataSource.carInfoTotalListType(reportid, type, carnum)
        }
    }

    suspend fun carInfoTotalListLaw(reportid: Int, carnum: String):  List<CarInfoTotal> {
        return withContext(ioDispatcher) {
            localDataSource.carInfoTotalListLaw(reportid, carnum)
        }
    }

    suspend fun carInfoTotalInsert(carInfoTotal: CarInfoTotal)  {
        withContext(ioDispatcher) {
            localDataSource.carInfoTotalInsert(carInfoTotal)
        }
    }

    suspend fun carInfoTotalById(id: Int): CarInfoTotal {
       return withContext(ioDispatcher) {
            localDataSource.carInfoTotalById(id)
        }
    }

    suspend fun reportType_0_Increase(id: Int, type_0: Int) {
        withContext(ioDispatcher) {
            localDataSource.reportType_0_Increase(id, type_0)
        }
    }

    suspend fun reportType_1_Increase(id: Int, type_1: Int) {
        withContext(ioDispatcher) {
            localDataSource.reportType_1_Increase(id, type_1)
        }
    }

    suspend fun reportLawStopIncrease(id: Int, lawstop: Int) {
        withContext(ioDispatcher) {
            localDataSource.reportType_1_Increase(id, lawstop)
        }
    }

    suspend fun carInfoTotalDeleteById(carInfoTotal : CarInfoTotal) {
        withContext(ioDispatcher) {
            localDataSource.carInfoTotalDeleteById(carInfoTotal)
        }
    }
}
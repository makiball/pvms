package kr.co.toplink.pvms.model

data class ListItems(
    var singleRowList: MutableList<SingleRow> = arrayListOf(),
    var filterItem: String? = ""
)

data class SingleRow(
    var name: String,
    var value: String? = ""
)

data class ExcellFile(
    var name: String,
    var path: String
)
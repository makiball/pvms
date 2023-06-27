package kr.co.toplink.pvms.data

import kotlinx.android.parcel.Parcelize

enum class Option (val text: String) {
    carnumber("차량번호"),
    phone("휴대폰번호"),
    etc("비고")
}

enum class  Type {
    REG,
    NOT,
}
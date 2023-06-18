package kr.co.toplink.pvms.util

class InputCheck {

    private var carnum: String = ""
    private var phone: String = ""

    init {
        //this.carnum = carnum.replace("\\s+".toRegex(), "").toString()
    }

    /* 공백제거 */
    fun blankDelte(carnum: String): String {
        return carnum.replace("\\s+".toRegex(), "")
    }

    /* 휴대폰번호 정규식 */
    fun getIsPhone(phone: String) : Boolean {
        val regex =  Regex("\\d{3}-\\d{3,4}-\\d{4}")
        return regex.matches(phone)
    }

    /* 차량 번호판 정규식 */
    fun getIsNumber(carnuminput: String): Boolean {
        val regex =  Regex("\\d{2,3}[가-힣]{1}\\d{4}")
        return regex.matches(carnuminput)
    }

    /* 차량 번호 뒷 4자리 가져오기 */
    fun getCarNumber4d(carnuminput: String): String {
        return carnuminput.substring(carnuminput.length - 4 until carnuminput.length)
    }

    /* 차량번호를 숫자만 가져오기                                                                                                                                                                                                                                                                                                                       */
    fun getCarNumberDigit(carnuminput: String): String {
        return carnuminput.replace("[^0-9]".toRegex(), " ")
    }
}
package kr.co.toplink.pvms.model

data class ActivityClassModel(val clazz: Class<*>, val description: String = clazz.name)

package kr.co.toplink.pvms.util

import android.content.Context
import android.os.Environment
import java.io.File

val rootFolder =
    File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
        "pvms${File.separator}"
    ).apply {
        if (!exists())
            mkdirs()
    }

fun makeTempFile(): File = File.createTempFile("${System.currentTimeMillis()}", ".png", rootFolder)
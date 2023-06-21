package kr.co.toplink.pvms.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.media.tv.TvContract.AUTHORITY
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import org.apache.poi.hssf.usermodel.HeaderFooter.file
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


fun Bitmap.rotateFlipImage(degree: Float, isFrontMode: Boolean): Bitmap? {
    val realRotation = when (degree) {
        0f -> 90f
        90f -> 0f
        180f -> 270f
        else -> 180f
    }
    val matrix = Matrix().apply {
        if (isFrontMode) {
            preScale(-1.0f, 1.0f)
        }
        postRotate(realRotation)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap.scaleImage(view: View, isHorizontalRotation: Boolean): Bitmap? {
    val ratio = view.width.toFloat() / view.height.toFloat()
    val newHeight = (view.width * ratio).toInt()

    return when (isHorizontalRotation) {
        true -> Bitmap.createScaledBitmap(this, view.width, newHeight, false)
        false -> Bitmap.createScaledBitmap(this, view.width, view.height, false)
    }
}

fun Bitmap.getBaseYByView(view: View, isHorizontalRotation: Boolean): Float {
    return when (isHorizontalRotation) {
        true -> (view.height.toFloat() / 2) - (this.height.toFloat() / 2)
        false -> 0f
    }
}

fun Bitmap.saveToGallery(context: Context) {
    // 비트맵을 저장할 경로와 파일명 설정
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val displayName = "pmvs_$timeStamp.png"
    val mimeType = "image/png"

    // 이미지를 저장할 내부 저장소 URI 생성
    val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        TODO("VERSION.SDK_INT < Q")
    }

    // ContentResolver를 사용하여 이미지를 저장할 ContentValues 객체 생성
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        put(MediaStore.Images.Media.MIME_TYPE, mimeType)
    }

    // ContentResolver를 사용하여 이미지를 저장
    val resolver: ContentResolver = context.contentResolver
    val imageUriResult = resolver.insert(imageUri, contentValues)

    // 이미지 데이터를 OutputStream으로 가져와서 저장
    imageUriResult?.let { uri ->
        val outputStream = resolver.openOutputStream(uri)
        outputStream?.use { stream ->
            this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
    }

/*
// Scan the temporary file to make it available in the media library
//scanMediaFile(context, imageFile)


val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
val imageFileName = "IMG_$timeStamp"
val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

try {
    val imageFile = File.createTempFile(imageFileName, ".png", storageDir)
    val outputStream = FileOutputStream(imageFile)
    this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()

    // Scan the temporary file to make it available in the media library
    scanMediaFile(context, imageFile)
} catch (e: IOException) {
    e.printStackTrace()
}

makeTempFile().apply {
    FileOutputStream(this).run {
        this@saveToGallery.compress(Bitmap.CompressFormat.JPEG, 100, this)
        flush()
        close()
    }

    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also {
        it.data = Uri.fromFile(this)
        context.sendBroadcast(it)
    }
}
*/

}
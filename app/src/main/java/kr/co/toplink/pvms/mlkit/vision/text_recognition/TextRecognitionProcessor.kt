package kr.co.toplink.pvms.mlkit.vision.text_recognition

import android.graphics.Rect
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.camerax.BaseImageAnalyzer
import kr.co.toplink.pvms.camerax.GraphicOverlay
import kr.co.toplink.pvms.database.CarInfoDatabase
import java.io.IOException
import java.util.regex.Pattern


@ExperimentalGetImage
class TextRecognitionProcessor(private val view: GraphicOverlay) : BaseImageAnalyzer<Text>() {

    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    override val graphicOverlay: GraphicOverlay
        get() = view
    // val regex =  Regex("\\d{2,3}[가-힣]{1}\\d{4}")
    val regex =  Regex("\\d{2,3}[^\\d]+\\d{4}")
    private var db: CarInfoDatabase
    init {
        db = CarInfoDatabase.getInstance(view.context)!!
    }
    private var isStart = 0

    override fun detectInImage(image: InputImage): Task<Text> {
        return recognizer.process(image)
    }

    override fun stop() {
        try {
            recognizer.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Text Recognition: $e")
        }
    }

    override fun onSuccess(
        results: Text,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    ) {
        graphicOverlay.clear()
        results.textBlocks.forEach {
            val textGraphic = TextRecognitionGraphic(graphicOverlay, it, rect)

            val textcarnum = it.text.replace("\\s+".toRegex(), "")
            var carnum = ""
            if(regex.matches(textcarnum)) {

                carnum = extractCarNumber(textcarnum)
                if (carnum != "") {
                    Log.d(TAG, "=====> $carnum ")
                    searchDataBase(carnum)
                    graphicOverlay.add(textGraphic)
                }

                /*
                isStart = 1
                matches.forEach {
                    carnum = it.toString()


                }
                 */
            }
        }
        graphicOverlay.postInvalidate()
    }

    /* 자동차 번호 추출하기 */
    fun extractCarNumber(text: String): String {
        val matches = regex.findAll(text)
        var carnum = matches.map { it.value }.joinToString("")
        carnum = Regex("[\\D]+").replace(carnum, " ")
        return carnum
    }

    /* 등록 차량 조회 */
    fun searchDataBase(textcarnum: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val carinfos = CoroutineScope(Dispatchers.IO).async {
                db.CarInfoDao().CarInfoSearchByCarnumber(textcarnum)
            }.await()
            carinfos.forEach {
                if(it.carnumber != "") {

                    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
                    tone.startTone(ToneGenerator.TONE_DTMF_S, 500)
                }
            }
        }
        isStart = 0
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Text Recognition failed.$e")
    }

    companion object {
        private const val TAG = "TextProcessor"
    }

}
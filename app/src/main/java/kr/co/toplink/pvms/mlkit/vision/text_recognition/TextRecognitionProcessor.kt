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


@ExperimentalGetImage
class TextRecognitionProcessor(private val view: GraphicOverlay) : BaseImageAnalyzer<Text>() {

    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    override val graphicOverlay: GraphicOverlay
        get() = view

    val regex =  Regex("\\d{2,3}[가-힣]\\d{4}")  //완전한 번호판
    //val regex =  Regex("\\d{2,3}[^\\d]+\\d{4}")
    var regex01 =  Regex("\\d{2,3}.\\d{4}")     //불완전 번호판
    var regex02 =  Regex("^\\d{4}$")               //옛날 번호판을 위해 4자리 숫자만 가져오기
    var regex03 =  Regex("^\\d{2,3}[가-힣]")        //옛날 번호판을 위해 앞자리 가져오기
    var oldCarNum : String = ""

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
            var carnum = it.text.replace("\\s+".toRegex(), "")
            var carnum_prefect = ""
            var carnum_imperfection = ""

            //옛날번호판 저장
            if(regex03.matches(carnum)) {
                oldCarNum = carnum
            }

            if(regex02.matches(carnum)) {
//                Log.d(TAG, "=====> $oldCarNum $carnum")
                carnum = "$oldCarNum $carnum"
                oldCarNum = ""
            }

            //완전한 번호판
            if(regex.matches(carnum)) {
                //searchDataBase(carnum)
                carnum_prefect = extractCarNumber(carnum, regex)

                graphicOverlay.add(textGraphic)
            }

            //불완전한 번호판
            if(regex01.matches(carnum)) {
                carnum_imperfection = extractCarNumber(carnum, regex01)
                carnum_imperfection = carnum_imperfection.substring(0, carnum_imperfection.length - 5) + " " + carnum_imperfection.substring(carnum_imperfection.length - 4)

                graphicOverlay.add(textGraphic)
            }



            //graphicOverlay.add(textGraphic)

            /*
            val textcarnum = it.text.replace("\\s+".toRegex(), "")
            var carnum = ""
            if(regex.matches(textcarnum)) {

                carnum = extractCarNumber(textcarnum)
                if (carnum != "") {
                    Log.d(TAG, "=====> $carnum ")
                    searchDataBase(carnum)
                    graphicOverlay.add(textGraphic)
                }


                isStart = 1
                matches.forEach {
                    carnum = it.toString()
                }

            }
            */
        }
        graphicOverlay.postInvalidate()
    }

    /* 자동차 번호 추출하기 */
    fun extractCarNumber(text: String, regex : Regex): String {
        val matches = regex.findAll(text)
        val carnum = matches.map { it.value }.joinToString("")
        //carnum = carnum.replace("\\s+".toRegex(), "")
        //carnum = Regex("[\\D]+").replace(carnum, " ")
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
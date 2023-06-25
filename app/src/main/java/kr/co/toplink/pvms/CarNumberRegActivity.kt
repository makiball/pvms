package kr.co.toplink.pvms

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoDatabase
import kr.co.toplink.pvms.databinding.ActivityCarnumberregBinding
import kr.co.toplink.pvms.model.ExcellReaderViewModel
import kr.co.toplink.pvms.model.ListItems
import kr.co.toplink.pvms.util.InputCheck
import kr.co.toplink.pvms.util.OpenDialog
import org.joda.time.format.DateTimeFormat
import java.io.File
import java.io.FileOutputStream
import java.lang.Math.ceil
import java.lang.Math.floor
import java.util.Date
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CarNumberRegActivity : AppCompatActivity() {

    private var document :Uri? = null

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumberregBinding

    private lateinit var db: CarInfoDatabase

    private lateinit var viewModel: ExcellReaderViewModel
    private var file: File? = null
    private var fileUri: Uri? = null
    private var url: String? = null

    private var inputcheck = InputCheck()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarnumberregBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = CarInfoDatabase.getInstance(this)!!


        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{  finish()  }
        binding.fileUploadBt.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                val mimetypes = arrayOf(
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            }
            resultLauncher.launch(intent)
        }


        /* 등록처리 */
        binding.regBt.setOnClickListener {

            val carnuminput = binding.carnumInpt.text.toString()
            var phoneinput = binding.phoneInpt.text.toString().replace("\\s+".toRegex(), "")
            val etcinpt = binding.etcInpt.text.toString()

            if(carnuminput.isEmpty() == true) {
                Toast.makeText(this, "차량 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* 차량 번호 정규식 */
            val carnumcheck = inputcheck.getIsNumber(carnuminput)
            if(!carnumcheck) {
                Toast.makeText(this, "조회 할수 없는 차량 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* 휴대폰 번호 정규식 */
            if(phoneinput.isNotEmpty() == true) {

                if(Regex("^010.*").matches(phoneinput)) {
                    phoneinput = "${phoneinput.substring(0, 3)}-${phoneinput.substring(3, 7)}-${phoneinput.substring(7)}"
                } else {
                    phoneinput = "${phoneinput.substring(0, 3)}-${phoneinput.substring(3, 6)}-${phoneinput.substring(6)}"
                }

                /*
                val phoneinput = inputcheck.getIsPhone(phoneinput)
                if(!phoneinput) {
                    Toast.makeText(this, "휴대폰번호를 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                 */
            }

            insertDatabase(carnuminput, phoneinput, etcinpt)
            Toast.makeText(this, "입력 성공 차량 번호 조회 가능!", Toast.LENGTH_SHORT).show()
        }

        init()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.also { Uri ->
                val uri = Uri.data!!
                Log.e(TAG, "ApachPOI Selected file Uri : " + uri)
                val mimeTypeExtension = uri.getExtention(this)
                Log.e(TAG, "ApachPOI Selected file mimeTypeExtension : " + mimeTypeExtension)
                if (mimeTypeExtension != null && mimeTypeExtension.isNotEmpty() == true) {

                    if (mimeTypeExtension.contentEquals("xlsx") == true
                        || mimeTypeExtension.contentEquals("xls") == true
                    ) {
                        Log.e(
                            TAG,
                            "ApachPOI Selected file mimeTypeExtension valid : " + mimeTypeExtension
                        )
                    } else {
                        Toast.makeText(this, "invalid file selected", Toast.LENGTH_SHORT).show()
                        return@also
                    }
                }
                //copyFileAndExtract(uri, mimeTypeExtension.orEmpty())
                copyFileAndExtract(uri)
            }
        }
    }

    private fun copyFileAndExtract(uri: Uri) {

        val dir = File(this.filesDir, "doc")
        dir.mkdirs()
        val fileName = getFileName(uri).toString()
        file = File(dir, fileName)
        file?.createNewFile()
        val fout = FileOutputStream(file)
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                fout.use { output ->
                    inputStream.copyTo(output)
                    output.flush()
                }
            }
            fileUri = FileProvider.getUriForFile(this, packageName + ".provider", file!!)
        } catch (e: Exception) {
            fileUri = uri
            e.printStackTrace()
        }
        fileUri?.apply {
            file?.apply {
                Log.e(TAG, this.absolutePath)
                if (viewModel.isEncrypt(this.absolutePath)) {
                    Log.e(TAG, "Document encrypted")
                    openDialog(this.absolutePath)
                } else {
                    Log.e(TAG, "Document not encrypted")
                    viewModel.readExcelFileFromAssets(this.absolutePath)
                }
            }
        }
    }

    fun getFileName(uri: Uri): String? = when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
        else -> uri.path?.let(::File)?.name
    }

    private fun getContentFileName(uri: Uri): String? = runCatching {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                .let(cursor::getString)
        }
    }.getOrNull()

    fun Uri.getExtention(context: Context): String? {
        val extension = if (this.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.getContentResolver().getType(this))
        } else {
            MimeTypeMap.getFileExtensionFromUrl(
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    File(this.path.toString())
                )
                    .toString()
            )
        }
        return extension
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(ExcellReaderViewModel::class.java)
        viewModel.fileDir = File(this.filesDir, AppConstant.doc)
        if (intent.extras?.containsKey("excellPath") == true) {
            val filePath = intent.extras?.getString("excellPath").orEmpty()
            if (viewModel.isEncrypt(filePath)) {
                Log.e(TAG, "Document encrypted")
                openDialog(filePath)
            } else {
                Log.e(TAG, "Document not encrypted")
                viewModel.readExcelFileFromAssets(filePath)
            }
        }

        attachObserver()
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    private fun openDialog(path: String) {
        val dlg = OpenDialog(this)
        dlg.setOnOKClickedListener{ password ->

            // text.text = content
            viewModel.readExcelFileFromAssets(path, password)
        }
        dlg.show("패스워드를 입력하세요.")
    }

    private fun attachObserver() {
        viewModel.excelExceptionListData.observe(this, androidx.lifecycle.Observer {
            it.apply {
                /* 엑셀 읽는것 실패 */
                checkForNoData(this.isEmpty())
            }
        })
        viewModel.excelDataListLiveData.observe(this, androidx.lifecycle.Observer {
            it?.apply {

                var total = 0

                /* 엑셀 읽는것 성공 */
                this.forEach{
//                    Log.d(TAG,"=====> ${it.singleRowList.get(0).name} ${it.singleRowList.get(1).name} ${it.singleRowList.get(2).name}");

                    var carnum = ""
                    var phone = ""
                    var etc = ""

                    it.singleRowList.get(0).value?.let { it1 ->  carnum = inputcheck.blankDelte(it1) }
                    it.singleRowList.get(1).value?.let { it2 ->  phone = it2  }
                    it.singleRowList.get(2).value?.let { it3 ->  etc = it3  }

                    if(inputcheck.getIsNumber(carnum)) {
                        insertDatabase(
                            carnum,
                            phone,
                            etc
                        )
                        total += 1
                    }

                  //  Log.d(TAG, "carnum -> $carnum")
                }
                progress(total)
            }
        })
    }

    private fun checkForNoData(check: Boolean) {
        if(check == false) {
            Toast.makeText(this, "엑셀값이 비어있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun progress(total: Int) {
        var progress = 0
        var progress_size = 0.00
        var div =  (total.toDouble() / 100)

        Thread(Runnable {
            while (progress < 100) {
                progress += 1
                progress_size += div

                /** Update UI */
                runOnUiThread {
                    //Log.d(TAG, "number ---> $progress, $progress_size, $div, $total")

                    binding.fileUploadProgressbar.progress = progress
                    binding.fileUploadText.text = floor(progress_size).toInt().toString()
                }
                Thread.sleep(50)
            }
        }).start()
    }

    // 엑셀 값이 넘어오면 데이터베이스 처리를 한다.
    private fun insertDatabase(carnuminput: String, phoneinput: String, etcinpt: String) {

        //우측 4개 번호판을 자르기
        val carnumber4d = inputcheck.getCarNumber4d(carnuminput)
        val carnumberdigit = inputcheck.getCarNumberDigit(carnuminput)
        val datepatterned = Date()

            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val currentTime =  LocalDateTime.now()
                val datepattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                datepatterned = currentTime.format(datepattern)
            } else {
                val date = org.joda.time.LocalDateTime.now()
                val datepattern = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
                val jodatime = datepattern.parseDateTime(date.toString())
                datepatterned = datepattern.print(jodatime)
            }
             */

        CoroutineScope(Dispatchers.IO).launch {
            db.CarInfoDao().CarInfoInsert(CarInfo(
                carnumber = carnuminput,
                carnumber4d = carnumber4d,
                carnumberonly = carnumberdigit,
                phone = phoneinput,
                date = datepatterned,
                etc = etcinpt
            ))
        }

/*
        var carinfoList = "자동차 정보들 \n"
        CoroutineScope(Dispatchers.Main).launch {
            val carinfos = CoroutineScope(Dispatchers.IO).async {
                db.CarInfoDao().CarInfoAll()
            }.await()

            for(carinfo in carinfos){
                carinfoList += "${carinfo.carNumber} ${carinfo.phone}  ${carinfo.date}  \n"
            }

            Log.d(TAG, "=====> $carinfoList")
        }
 */
        //Log.d(TAG, "$carnuminput $carnumber4d $carnumberdigit  $phoneinput $datepatterned $etcinpt")
    }
}
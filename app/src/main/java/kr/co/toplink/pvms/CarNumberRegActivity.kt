package kr.co.toplink.pvms

import android.app.Activity
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.DocumentsContract.EXTRA_INITIAL_URI
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.databinding.ActivityCarnumberregBinding
import kr.co.toplink.pvms.model.ExcellReaderViewModel
import kr.co.toplink.pvms.util.OpenDialog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CarNumberRegActivity : AppCompatActivity() {

    private var document :Uri? = null

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumberregBinding
    private var pickerinitialuri: Uri? = null

    private lateinit var viewModel: ExcellReaderViewModel
    private var file: File? = null
    private var fileUri: Uri? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarnumberregBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        init()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            var mimeTypeExtension: String? = ""
            result.data?.also { Uri ->
                val uri = Uri.data!!
                Log.e(TAG, "ApachPOI Selected file Uri : " + uri)
                mimeTypeExtension = uri.getExtention(this)
                Log.e(TAG, "ApachPOI Selected file mimeTypeExtension : " + mimeTypeExtension)
                if (mimeTypeExtension != null && mimeTypeExtension?.isNotEmpty() == true) {

                    if (mimeTypeExtension?.contentEquals("xlsx") == true
                        || mimeTypeExtension?.contentEquals("xls") == true
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
                copyFileAndExtract(uri, mimeTypeExtension.orEmpty())
            }
        }
    }

    private fun copyFileAndExtract(uri: Uri, extension: String) {

        val dir = File(this.filesDir, "doc")
        dir.mkdirs()
        val fileName = getFileName(uri)
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
        var extension: String? = ""
        extension = if (this.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.getContentResolver().getType(this))
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                MimeTypeMap.getFileExtensionFromUrl(
                    FileProvider.getUriForFile(
                        context,
                        context.packageName + ".provider",
                        File(this.path)
                    )
                        .toString()
                )
            } else {
                MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(this.path)).toString())
            }
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
            }
        })
        viewModel.excelDataListLiveData.observe(this, androidx.lifecycle.Observer {
            it?.apply {
                /* 엑셀 읽는것 성공 */
            }
        })
    }

}
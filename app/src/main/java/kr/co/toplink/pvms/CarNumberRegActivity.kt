package kr.co.toplink.pvms


import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.toplink.pvms.adapter.ListItemAdapter

import kr.co.toplink.pvms.databinding.ActivityCarnumberregBinding
import kr.co.toplink.pvms.model.ExcellReaderViewModel
import kr.co.toplink.pvms.model.ListItems
import kr.co.toplink.pvms.util.OpenDialog
import java.io.File
import java.io.FileOutputStream

class CarNumberRegActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumberregBinding
    private lateinit var viewModel: ExcellReaderViewModel
    private var file: File? = null
    private var fileUri: Uri? = null
    private var url: String? = null
    private var adapter: ListItemAdapter? = null

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

        binding.backBt.setOnClickListener{
            finish()
        }

        init()

        binding.fileUploadBt.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                addCategory(Intent.CATEGORY_OPENABLE)
                // mime types for MS Word documents
                val mimetypes = arrayOf(
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            }
            activityResultLauncher.launch(intent)
        }
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


        setUpAdapter()
    }

    private fun openDialog(path: String) {
        val dlg = OpenDialog(this)
        dlg.setOnOKClickedListener{ password ->
            val result: Boolean = viewModel.checkPassword(password, path)
            if (!result) {
                Log.e(TAG, "Password is incorrect")
                Toast.makeText(this, "Password is incorrect.", Toast.LENGTH_SHORT).show()
                openDialog(path)
                //dialog.dismiss()
                //hideProgress()
            } else {
                Log.e(TAG, "Password is correct")
                Toast.makeText(this, "Password is Correct.", Toast.LENGTH_SHORT).show()
                viewModel.readExcelFileFromAssets(path, password)
            }
        }
        dlg.show("엑셀 파일이 잠겨 있습니다. 패스워드를 입력해주세요!")
    }

    /* 학장자 반환 */
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


    //파일 첨주 결과 받기
    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ data ->
        if(data.resultCode == RESULT_OK) {
            var mimeTypeExtension: String? = ""
            data?.data?.also { result  ->

                Log.e(TAG, "ApachPOI Selected file Uri : " + result.data)
                val uri = result.data
                mimeTypeExtension = uri?.getExtention(this)
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
                    copyFileAndExtract(uri!!, mimeTypeExtension.orEmpty())
                }
            }
        }
    }

    /* 엑셀 실행 */
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

    /* 파일명 반환 */
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


    private fun setUpAdapter() {
        adapter = ListItemAdapter(arrayListOf(), object : ExcelRowClickListener {
            override fun onRowClick(position: Int) {
                viewModel.setPositionCompleted(position)
            }
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun attachObserver() {
        viewModel.excelExceptionListData.observe(this, androidx.lifecycle.Observer {
            it.apply {
                checkForNoData()
                hideProgress()
                binding.llNoDataFound.tvNoDataFound.text = this.orEmpty()
            }
        })
        viewModel.excelDataListLiveData.observe(this, androidx.lifecycle.Observer {
            it?.apply {
                adapter?.clear()
                adapter?.setData(this)
                checkForNoData()
                hideProgress()
            }
        })
    }
}
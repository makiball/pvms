package kr.co.toplink.pvms

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.DocumentsContract.EXTRA_INITIAL_URI
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kr.co.toplink.pvms.databinding.ActivityCarnumberregBinding
import java.io.FileNotFoundException
import java.io.IOException


class CarNumberRegActivity : AppCompatActivity() {

    private var document :Uri? = null

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumberregBinding
    private var pickerinitialuri: Uri? = null


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
                type = "*/*";
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.DIRECTORY_DOCUMENTS)
            }
            resultLauncher.launch(intent)
        }
    }


    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            result.data?.let { intent ->
                intent.data?.let { fileUri ->
                    // u have a valid uri now
                    Log.d(TAG,"======> $fileUri ")
                } ?: run {
                    // show some error Ui
                }
            }


        }
    }

}
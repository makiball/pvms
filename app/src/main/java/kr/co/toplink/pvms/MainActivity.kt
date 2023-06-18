package kr.co.toplink.pvms


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kr.co.toplink.pvms.databinding.ActivityMainBinding
import kr.co.toplink.pvms.model.ActivityClassModel


@ExperimentalGetImage class MainActivity : AppCompatActivity() {

    private val activityClassModels = ArrayList<ActivityClassModel>()

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addActivity()

        binding.carnumberregBt.setOnClickListener {
            activitygo(0)
        }

        binding.carnumbersearchBt.setOnClickListener {
            activitygo(1)
        }

        binding.camsearchBt.setOnClickListener {
            activitygo(2)
        }


    }

    private fun addActivity() {
        activityClassModels.add(
            ActivityClassModel(
                CarNumberRegActivity::class.java,
                getString(R.string.carnumberreg_bt)
            )
        )

        activityClassModels.add(
            ActivityClassModel(
                CarNumberSearchActivity::class.java,
                getString(R.string.carnumbersearch_bt)
            )
        )

        activityClassModels.add(
            ActivityClassModel(
                CamCarSearchActivity::class.java,
                getString(R.string.camsearch_bt)
            )
        )

    }

    /* 엑티비티 바로가기 */
    fun activitygo(position: Int) {
        Intent(this, activityClassModels[position].clazz).also {
            startActivity(it)
        }
    }


    companion object {
        private const val TAG = "EntryChoiceActivity"
        private const val PERMISSION_REQUESTS = 10

        private val REQUIRED_RUNTIME_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
    }
}
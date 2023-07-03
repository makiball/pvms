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

        binding.smsregBt.setOnClickListener {
            activitygo(3)
        }


        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }
    }

    private fun allRuntimePermissionsGranted(): Boolean {
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }


    private fun getRuntimePermissions() {
        val permissionsToRequest = java.util.ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    permissionsToRequest.add(permission)
                }
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
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
                getString(R.string.carnumberreg_bt)
            )
        )

        activityClassModels.add(
            ActivityClassModel(
                CamCarSearchActivity::class.java,
                getString(R.string.camsearch_bt)
            )
        )

        activityClassModels.add(
            ActivityClassModel(
                SmsManagerActivity::class.java,
                getString(R.string.smsreg_bt)
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
        private val TAG = this.javaClass.simpleName
        private const val PERMISSION_REQUESTS = 1

        private val REQUIRED_RUNTIME_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
    }
}
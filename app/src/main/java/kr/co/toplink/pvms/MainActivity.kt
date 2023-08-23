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
import com.google.firebase.messaging.FirebaseMessaging
import kr.co.toplink.pvms.databinding.ActivityMainBinding
import kr.co.toplink.pvms.model.ActivityClassModel
import kr.co.toplink.pvms.util.SharedPreferencesUtil


@ExperimentalGetImage
class MainActivity : AppCompatActivity() {

    private val activityClassModels = ArrayList<ActivityClassModel>()
    private lateinit var binding: ActivityMainBinding
    lateinit var sp: SharedPreferencesUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = SharedPreferencesUtil(this)

        addActivity()
        getFCMToken()

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

        binding.settingBt.setOnClickListener {
            activitygo(4)
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

        activityClassModels.add(
            ActivityClassModel(
                SettingActivity::class.java,
                getString(R.string.setting_bt)
            )
        )
    }

    /* 엑티비티 바로가기 */
    fun activitygo(position: Int) {
        Intent(this, activityClassModels[position].clazz).also {
            startActivity(it)
        }
    }

    // 토큰 생성
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            Log.d(TAG, "token 정보: ${task.result ?: "task.result is null"}")

            if (task.result != null) {
                sp.setFCMToken(task.result)
            }
        }
    }

    companion object {
        private val TAG = this.javaClass.simpleName

        const val channel_id = "pvms_user"
        private const val PERMISSION_REQUESTS = 1

        private val REQUIRED_RUNTIME_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
    }
}
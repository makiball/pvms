package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kr.co.toplink.pvms.config.ApplicationClass
import kr.co.toplink.pvms.databinding.ActivitySettingBinding
import kr.co.toplink.pvms.dto.User
import kr.co.toplink.pvms.dto.UserResponse
import kr.co.toplink.pvms.repository.auth.AuthRemoteDataSource
import kr.co.toplink.pvms.repository.auth.AuthRepository
import kr.co.toplink.pvms.util.RetrofitUtil
import kr.co.toplink.pvms.util.SharedPreferencesUtil

class SettingActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivitySettingBinding
    lateinit var user : User
    lateinit var sp: SharedPreferencesUtil
    lateinit var userResponse: UserResponse
    var fcm : String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = User("", "", "")
        sp = SharedPreferencesUtil(this)

        fcm = sp.getFCMToken()

        //로그인이 되어 있으면 자동 세팅
        if(sp.getUser().email != "") {
            user = User(sp.getUser().email, sp.getUser().passwd, sp.getUser().fcm)
            val authRepo = AuthRepository(AuthRemoteDataSource(RetrofitUtil.authService))
            val job = CoroutineScope(Dispatchers.IO).launch {
                userResponse = authRepo.signIn(user)
            }
            runBlocking {
                job.join()
                   binding.subtitle2.text = "남은 알림톡 : ${userResponse.smsPoint}개"
                   binding.alrimIdInpt.setText(sp.getUser().email)
                   binding.alrimPwInpt.setText(sp.getUser().passwd.toString())
            }
        }

        //최초 아이디 페스워드 세팅
        binding.alrimIdInpt.text

        binding.regBt.setOnClickListener {

            val id = binding.alrimIdInpt.text.toString()
            val pw = binding.alrimPwInpt.text.toString()

            if(id == "" ) {
                Toast.makeText(this, "아이디를 입력해주세요!", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            if(pw == "") {
                Toast.makeText(this, "패스워드를 입력해주세요!", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            user = User(id, pw, fcm)

            Log.d(TAG, "유저 가져오기 : $user")
            val authRepo = AuthRepository(AuthRemoteDataSource(RetrofitUtil.authService))
            val job = CoroutineScope(Dispatchers.IO).launch {
                userResponse = authRepo.signIn(user)
            }

            runBlocking {
                job.join()

                if(userResponse.result == "ok") {
                    //Log.d(TAG,"유저 가져오기 : $userResponse")
                    ApplicationClass.sharedPreferencesUtil.addUser(user)
                    Toast.makeText(this@SettingActivity, userResponse.msg, Toast.LENGTH_LONG).show();

                    Log.d(TAG,"유저 가져오기 : $userResponse")

                    binding.subtitle2.text = "남은 알림톡 : ${userResponse.smsPoint}개"
                } else {
                    //Log.d(TAG,"유저 가져오기 : $userResponse")
                    Toast.makeText(this@SettingActivity, userResponse.msg, Toast.LENGTH_LONG).show();
                }

            }

        }



        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        sp = SharedPreferencesUtil(this)
    }
}
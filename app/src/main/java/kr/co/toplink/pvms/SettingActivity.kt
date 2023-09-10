package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
import kr.co.toplink.pvms.util.ComfirmDialog
import kr.co.toplink.pvms.util.RetrofitUtil
import kr.co.toplink.pvms.util.SharedPreferencesUtil
import kr.co.toplink.pvms.viewmodel.UserViewModel
import kr.co.toplink.pvms.viewmodel.ViewModelFactory

class SettingActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivitySettingBinding
    lateinit var user : User
    lateinit var sp: SharedPreferencesUtil
    lateinit var userResponse: UserResponse
    private lateinit var userViewModel: UserViewModel
    var fcm : String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = User("", "", "")
        sp = SharedPreferencesUtil(this)

        fcm = sp.getFCMToken()

        init()

        //로그인이 되어 있으면 자동 세팅
        if(sp.getUser().email != "") {

            val id = sp.getUser().email
            val pw = sp.getUser().passwd

            user = User(id, pw, fcm)
            userViewModel.getPoint(user)

            userViewModel.userPo.observe(this, EventObserver{
                binding.subtitle2.text = "남은 알림톡 : ${it}개"
                binding.alrimIdInpt.setText(id)
                binding.alrimPwInpt.setText(pw)

            })
        }

        binding.regBt.setOnClickListener {


            val connectivityManager = getSystemService(ConnectivityManager::class.java)
            val currentNetwork = connectivityManager.getActiveNetwork()

            if(currentNetwork == null) {
                Toast.makeText(this, " 인터넷 연결을 확인 하세요.", Toast.LENGTH_SHORT).show()
                finish()
            }

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
            userViewModel.signIn(user)

            userViewModel.userResponse.observe(this, EventObserver {
                if(it.result == "ok") {
                    sp.addUser(user)
                    binding.subtitle2.text = "남은 알림톡 : ${it.smsPoint}개"
                } else {
                    user = User("", "", "")
                    sp.addUser(user)
                }

                val msg = it.msg
                val dlg = ComfirmDialog(this)
                dlg.setOnOKClickedListener{
                }
                dlg.show(msg)

                //Toast.makeText(this@SettingActivity, it.msg, Toast.LENGTH_LONG).show();
            })
/*
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
             */
        }


        binding.backBt.setOnClickListener{
            finish()
        }
        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun init() {
        userViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(UserViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

       // sp = SharedPreferencesUtil(this)
    }
}
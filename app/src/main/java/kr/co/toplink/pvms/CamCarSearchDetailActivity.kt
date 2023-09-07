package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.data.SendKakaoAlrim
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchDetailBinding
import kr.co.toplink.pvms.util.*
import kr.co.toplink.pvms.viewmodel.*
import java.util.*

class CamCarSearchDetailActivity: AppCompatActivity() {


    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchDetailBinding
    private lateinit var camCarViewModel: CamCarViewModel
    private lateinit var smsManagerviewModel: SmsMngViewModel
    private lateinit var userViewModel: UserViewModel
    lateinit var sp: SharedPreferencesUtil
    private lateinit var carnum : String
    private var lasteid : Int = 0
    private var id : Int = 0
    private var phone : String = ""
    private var etc : String = ""
    private var type : Int = 0
    private var smsid : Int = 99999
    private var smsMsgList = mutableListOf<SmsManager>()


    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarnumbersearchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = SharedPreferencesUtil(this)

        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{  finish()  }

        if (intent.hasExtra("carnum")) {
            carnum = intent.getStringExtra("carnum").toString()
        } else {
            finish()
        }

        init()

        /* 삭제 */
        binding.delete.setOnClickListener {

            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                //viewModel.idDeteData(this, id)
                camCarViewModel.carInfoTodayDeleteById(id)
                finish()
            }
            dlg.show(msg)
        }

        /* 전화 */
        binding.call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(intent)
        }

        /* 문자 */
        binding.sms.setOnClickListener {

            val  message  = ""

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phone")
                putExtra("sms_body", message)
            }
            startActivity(intent)
        }

        /* 알림톡 */
        binding.kakaotalk.setOnClickListener {

            val connectivityManager = getSystemService(ConnectivityManager::class.java)
            val currentNetwork = connectivityManager.getActiveNetwork()

            if(currentNetwork == null) {
                Toast.makeText(this, " 인터넷 연결을 확인 하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "currentNetwork=====> $currentNetwork")

            if(smsid == 99999) {
                Toast.makeText(this, " 문자 내용을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val  message  = smsMsgList[smsid].smscontent
            val id = sp.getUser().email

            if(id == "" || id == "비회원") {
                Toast.makeText(this, " 설정에서 알림톡 로그인을 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val kakaoalim = KakaoAlim(id, message, phone)

            userViewModel.kakaoAlimSend(kakaoalim)
            userViewModel.kakaoalrimresponse.observe(this, EventObserver {
                //Log.d(TAG, "======> $it $id")
                //Toast.makeText(this, " ${it.msg} ", Toast.LENGTH_SHORT).show()
                val msg = it.msg
                val dlg = ComfirmDialog(this)
                dlg.setOnOKClickedListener{
                }
                dlg.show(msg)
            })
        }

        /* 스피너 설정 */
        binding.textItem.setOnItemClickListener(OnItemClickListener { adapterView, view, position, id ->
            smsid = id.toInt()
        })

        /* 불법주차 */
        binding.lowstop.setOnCheckedChangeListener { buttonView, isChecked ->
            var lawstop = 0
            val datepatterned = Date()

            when(isChecked){
                true -> lawstop = 1
                false -> lawstop = 0
                else -> lawstop = 0
            }

            val carinfotoday = CarInfoToday(
                id = id,
                carnumber = carnum,
                phone = phone,
                date = datepatterned,
                etc = etc,
                type = type,
                lawstop = lawstop
            )

            camCarViewModel.carInfoTodayUpdateById(carinfotoday)
        }

    }

    private fun init() {

        camCarViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(CamCarViewModel::class.java)
        camCarViewModel.carInfoToday(carnum)
        camCarViewModel.camcarinfo.observe(this, EventObserver {

            val formattedDate = DateToYmdhis(it.date)
            binding.date.text = "등록일 : $formattedDate"

            phone = it.phone
            id = it.id
            etc = it.etc
            type = it.type

            binding.carnumberTxt.text = it.carnumber
            binding.phone.text = PhoneHidden(phone)
            binding.etc.text = it.etc

            if(it.lawstop == 1) {
                binding.lowstop.isChecked = true
            }

        })

        smsManagerviewModel = ViewModelProvider(this, ViewModelFactory(this)).get(SmsMngViewModel::class.java)
        smsManagerviewModel.smsMagAll()
        smsManagerviewModel.smsinfos.observe(this) {
            val smsTitleList = java.util.ArrayList<String>()
            it.forEach {
                //val smsMagList = SmsManagerList(it.id, it.smstitle, it.smscontent)
                //smsList.add(SmsMangerModel(smsMagList))
                smsMsgList.add(SmsManager(it.id, it.smstitle, it.smscontent))
                smsTitleList.add(it.smstitle)
            }

            val itemAdapter = ArrayAdapter(
                this@CamCarSearchDetailActivity,
                R.layout.sms_item_list_layout, smsTitleList
            )
            binding.textItem.setAdapter(itemAdapter)
        }

        userViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(UserViewModel::class.java)

    }
}
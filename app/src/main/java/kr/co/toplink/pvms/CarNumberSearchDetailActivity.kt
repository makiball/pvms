package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.data.*
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchDetailBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.model.SmsManagerViewModel
import kr.co.toplink.pvms.util.*
import kr.co.toplink.pvms.viewmodel.CarInfoViewModel
import kr.co.toplink.pvms.viewmodel.SmsMngViewModel
import kr.co.toplink.pvms.viewmodel.UserViewModel
import kr.co.toplink.pvms.viewmodel.ViewModelFactory
import org.json.JSONObject

class CarNumberSearchDetailActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchDetailBinding
    private lateinit var carInfoviewModel: CarInfoViewModel
    private lateinit var smsManagerviewModel: SmsMngViewModel
    private lateinit var userViewModel: UserViewModel
    lateinit var sp: SharedPreferencesUtil
    private lateinit var carnum : String
    private var id : Int = 0
    private var phone : String = ""
    private var smsid : Int = 99999
    private lateinit var sendkakaoalrim : SendKakaoAlrim
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

        //불법주차 가릭
        binding.lowstop.visibility = View.GONE

        init()

        /* 삭제 */
        binding.delete.setOnClickListener {

            //Toast.makeText(this, " $id ", Toast.LENGTH_SHORT).show()

            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                //viewModel.idDeteData(this, id)
                carInfoviewModel.carInfoDeletebyid(carnum)
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

            //val message = "차좀 빼줘!!!"
            /*
            if(smsid == 99999) {
                Toast.makeText(this, " 문자 내용을 선택해주세요. ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
             */

            //val  message  = smsMsgList[smsid].smscontent
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


        /*
        val carinfolistmodel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(KEY_CARINFOLIST_MODEL, CarInfoListModel::class.java)
        } else {
            intent.getParcelableExtra<CarInfoListModel>(KEY_CARINFOLIST_MODEL)
        }

        carinfolistmodel?.let {
            val formattedDate = DateToYmdhis(it.carinfolist.date)
            phone = it.carinfolist.phone.toString()
            binding.carnumberTxt.text = it.carinfolist.carnumber
            binding.phone.text = phone?.let { it -> PhoneHidden(it) }
            binding.etc.text = it.carinfolist.etc
            binding.date.text = "등록일 : $formattedDate"
            id = it.carinfolist.id.toInt()
        }

        /* 알림톡 */
        binding.kakaotalk.setOnClickListener {
            val url = "https://ggulzem.site/pvms/sendMessage.php" // JSON을 받을 API의 URL을 입력하세요
            val  message  = smsMsgList[smsid].smscontent

            val jsonReceiver = JsonReceiver()

            //sendkakaoalrim.id = "edu"
            //sendkakaoalrim.phone = phone
            //sendkakaoalrim.phone = message

            Log.d(TAG, "json => 카카오 전송요청")
            jsonReceiver.fetchJson(url, object : JsonCallback {
                override fun onSuccess(json: JSONObject) {
                    // JSON 파싱에 성공한 경우 처리할 로직을 작성하세요
                    Log.d(TAG, "json => $json")
                }

                override fun onFailure(errorMessage: String?) {
                    // JSON 파싱에 실패하거나 응답이 실패한 경우 처리할 로직을 작성하세요
                }
            })
        }
         */
    }

    private fun init() {

        carInfoviewModel = ViewModelProvider(this, ViewModelFactory(this)).get(CarInfoViewModel::class.java)
        carInfoviewModel.carInfoSearchByCarnumber(carnum)
        carInfoviewModel.carinfo.observe(this, EventObserver {

            val formattedDate = DateToYmdhis(it.date)
            binding.date.text = "등록일 : $formattedDate"

            phone = it.phone.toString()
            binding.carnumberTxt.text = it.carnumber
            binding.phone.text = PhoneHidden(phone)
            binding.etc.text = it.etc

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
                this@CarNumberSearchDetailActivity,
                kr.co.toplink.pvms.R.layout.sms_item_list_layout, smsTitleList
            )
            binding.textItem.setAdapter(itemAdapter)
        }

        userViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(UserViewModel::class.java)


        /*
        viewModel2 = ViewModelProvider(this).get(SmsManagerViewModel::class.java)
        viewModel2.allListSms(this)
        viewModel2.smsManagerList.observe(this, androidx.lifecycle.Observer {
            it.apply {
               // listAdapter.submitList(generateMockCarinfo(this))
                val itemAdapter = ArrayAdapter(
                    this@CarNumberSearchDetailActivity,
                    kr.co.toplink.pvms.R.layout.sms_item_list_layout, generateMockCarinfo(this)
                )
                binding.textItem.setAdapter(itemAdapter)
            }
        })
        */
    }

    private fun generateMockCarinfo(smsmaglist: MutableList<SmsManager>): List<String> {
        val smsList = java.util.ArrayList<String>()
        smsmaglist.forEach{
            //val smsMagList = SmsManagerList(it.id, it.smstitle, it.smscontent)
            //smsList.add(SmsMangerModel(smsMagList))
            smsMsgList.add(SmsManager(it.id, it.smstitle, it.smscontent))
            smsList.add(it.smstitle)
        }
        return smsList
    }

    /*
    companion object {

    }
     */
}
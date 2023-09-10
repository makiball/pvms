package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.adapter.CarInfoTotalAdapter
import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.data.SendKakaoAlrim
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchDetailBinding
import kr.co.toplink.pvms.util.*
import kr.co.toplink.pvms.viewmodel.*

class CarInfoTotalDetailActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchDetailBinding
    //private lateinit var carInfoviewModel: CarInfoViewModel
    private lateinit var smsManagerviewModel: SmsMngViewModel
    private lateinit var userViewModel: UserViewModel
    lateinit var sp: SharedPreferencesUtil
    private var reportid : Int = 0
    private var carnum : String = ""
    private var id : Int = 0
    private var phone : String = ""
    private var smsid : Int = 99999
    private var smsMsgList = mutableListOf<SmsManager>()

    private var type : Int = 0
    private var lawstop : Int = 0

    private lateinit var carInfoTotal : CarInfoTotal

    private lateinit var reportCarViewModel: ReportCarViewModel
    private lateinit var carInfoTotalAdapter: CarInfoTotalAdapter


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

        if (intent.hasExtra("id")) {
            id = intent.getIntExtra("id",0)
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
                //reportCarViewModel.reportDeleteById(id)

                when(type) {
                    0 -> reportCarViewModel.reportType_0_Increase(reportid, 1)
                    1 -> reportCarViewModel.reportType_1_Increase(reportid, 1)
                }

                reportCarViewModel.reportLawStopIncrease(reportid, lawstop)

                reportCarViewModel.carInfoTotalDeleteById(carInfoTotal)

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

            val  message  = "차량번호 $carnum \n" + smsMsgList[smsid].smscontent
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
        binding.textItem.setOnItemClickListener { adapterView, view, position, id ->
            smsid = id.toInt()
        }
    }

    private fun init() {

        /*
        carInfoviewModel = ViewModelProvider(this, ViewModelFactory(this)).get(CarInfoViewModel::class.java)
        carInfoviewModel.carInfoSearchByCarnumber(carnum)
        carInfoviewModel.carinfo.observe(this, EventObserver {

            val formattedDate = DateToYmdhis(it.date)
            binding.date.text = "등록일 : $formattedDate"

            phone = it.phone
            binding.carnumberTxt.text = it.carnumber
            binding.phone.text = PhoneHidden(phone)
            binding.etc.text = it.etc

        })
         */

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
                this@CarInfoTotalDetailActivity,
                R.layout.sms_item_list_layout, smsTitleList
            )
            binding.textItem.setAdapter(itemAdapter)
        }

        userViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(UserViewModel::class.java)


        reportCarViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(ReportCarViewModel::class.java)
        reportCarViewModel.carInfoTotalById(id)
        reportCarViewModel.carInfoTotal.observe(this, EventObserver {

            carInfoTotal = CarInfoTotal(
                id = it.id,
                carnumber = it.carnumber,
                phone = it.phone,
                date = it.date,
                etc = it.etc,
                type = it.type,
                lawstop = it.lawstop,
                reportnum = it.reportnum
            )

            carnum = it.carnumber.toString()
            phone = it.phone
            id = it.id
            reportid = it.reportnum

            type = it.type
            lawstop = it.lawstop

            val formattedDate = DateToYmdhis(it.date)
            binding.date.text = getString(R.string.CamCarSearchDetail_date_txt).format(formattedDate)

            binding.carnumberTxt.text = it.carnumber
            binding.phone.text = PhoneHidden(phone)
            binding.etc.text = it.etc
        })

        reportCarViewModel.carInfoTotalListCarnum(carnum)
        carInfoTotalAdapter = CarInfoTotalAdapter(reportCarViewModel)
        reportCarViewModel.carInfoTotals.observe(this) {
            binding.recyclerView.apply {
                adapter = carInfoTotalAdapter
                layoutManager = GridLayoutManager(this@CarInfoTotalDetailActivity, 1)
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            carInfoTotalAdapter.apply {
                submitList(it)
            }
        }

    }
}
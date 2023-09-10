package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.adapter.CarInfoTotalSearchAdapter
import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.databinding.ActivityCarinfoTotalBinding
import kr.co.toplink.pvms.util.*
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel
import kr.co.toplink.pvms.viewmodel.UserViewModel
import kr.co.toplink.pvms.viewmodel.ViewModelFactory

class CarInfoTotalActivity: AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarinfoTotalBinding
    lateinit var sp: SharedPreferencesUtil
    private lateinit var id : String

    private var reportid : Int = 0
    private lateinit var reportCarViewModel: ReportCarViewModel
    private lateinit var carInfoTotalSearchAdapter : CarInfoTotalSearchAdapter

    private lateinit var userViewModel: UserViewModel

    private  var total: Int = 0
    private  var regSu: Int = 0
    private  var regSuNo: Int = 0
    private  var lowStop: Int = 0
    private  var formattedDate: String = ""

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarinfoTotalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = SharedPreferencesUtil(this)
        id = sp.getUser().email


        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener { finish() }

        if (intent.hasExtra("reportid")) {
            reportid = intent.getIntExtra("reportid", 0)
        } else {
            finish()
        }

        init()

        binding.carinfototalCheck1.isChecked = true
        binding.searchBt.setOnClickListener {
            val carnum = binding.searchInptText.text.toString()
            val checkedId = binding.searchRdg.checkedRadioButtonId
            when(checkedId) {
                R.id.carinfototal_check_1 -> reportCarViewModel.carInfoTotalListId(reportid, carnum)
                R.id.carinfototal_check_2 -> reportCarViewModel.carInfoTotalListType(reportid, 0, carnum)
                R.id.carinfototal_check_3 -> reportCarViewModel.carInfoTotalListType(reportid, 1, carnum)
                R.id.carinfototal_check_4 -> reportCarViewModel.carInfoTotalListLaw(reportid, carnum)
                else -> reportCarViewModel.carInfoTotalListId(reportid, "")
            }
        }

        binding.searchRdg.setOnCheckedChangeListener { group, checkedId ->

            val carnum = binding.searchInptText.text.toString()

            when(checkedId) {
                R.id.carinfototal_check_1 -> reportCarViewModel.carInfoTotalListId(reportid, carnum)
                R.id.carinfototal_check_2 -> reportCarViewModel.carInfoTotalListType(reportid, 0, carnum)
                R.id.carinfototal_check_3 -> reportCarViewModel.carInfoTotalListType(reportid, 1, carnum)
                R.id.carinfototal_check_4 -> reportCarViewModel.carInfoTotalListLaw(reportid, carnum)
                else -> reportCarViewModel.carInfoTotalListId(reportid, "")
            }
           // attachObserver()
        }

        binding.alldelte.setOnClickListener {

            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{

                reportCarViewModel.reportDeleteById(reportid)
                reportCarViewModel.carInfoTotalDelteReportId(reportid)
                finish()
            }
            dlg.show(msg)
        }

        binding.kakaotalk.setOnClickListener {

            val msg_cont = binding.msgCont.text

            if(id == "" || id == "비회원") {
                Toast.makeText(this, " 설정에서 알림톡 로그인을 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cont = formattedDate + "|" + total + "|" + regSu + "|" + regSuNo + "|" + "불법주차 : ${lowStop}대\n" + msg_cont

            val kakaoalim = KakaoAlim(id, cont, "01099999999")
            userViewModel.kakaoReportSend(kakaoalim)

        }
    }

    private fun init() {
        reportCarViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(ReportCarViewModel::class.java)
        userViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(UserViewModel::class.java)
        reportCarViewModel.carInfoTotalListId(reportid, "")
        reportCarViewModel.report(reportid)
        attachObserver()
    }

    private fun attachObserver() {

        reportCarViewModel.report.observe(this, EventObserver{

            //미등록 갯수, 등록 갯수
            total = it.total_type_0 + it.total_type_1
            regSu = it.total_type_0
            regSuNo = it.total_type_1
            lowStop = it.total_lawstop
            formattedDate = DateToYmdhis(it.date)

            /*
            binding.total.text = getString(R.string.camcarsearch_total_txt).format(it.size)
            binding.totalreg.text = getString(R.string.camcarsearch_totalreg_txt).format(regSu)
            binding.totalnotreg.text = getString(R.string.camcarsearch_totalnotreg_txt).format(regSuNo)
            binding.lawstop.text = getString(R.string.camcarsearch_lawstop_txt).format(lowStop)
            */

            binding.total.text = getString(R.string.camcarsearch_total_txt).format(regSu)
            binding.totalreg.text = getString(R.string.camcarsearch_totalreg_txt).format(regSu)
            binding.totalnotreg.text = getString(R.string.camcarsearch_totalnotreg_txt).format(regSuNo)
            binding.lawstop.text = getString(R.string.camcarsearch_lawstop_txt).format(lowStop)

        })

        carInfoTotalSearchAdapter = CarInfoTotalSearchAdapter(reportCarViewModel)
        reportCarViewModel.carInfoTotals.observe(this) {

            binding.recyclerView.apply {
                adapter = carInfoTotalSearchAdapter
                layoutManager = GridLayoutManager(this@CarInfoTotalActivity, 1)
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            carInfoTotalSearchAdapter.apply {
                submitList(it)
            }
        }

        userViewModel.kakaoalrimresponse.observe(this, EventObserver {

            Log.d(TAG,"=====> $it.msg")

            val msg = it.msg
            val dlg = ComfirmDialog(this)
            dlg.setOnOKClickedListener{
            }
            dlg.show(msg)
        })
    }

    override fun onResume() {
        super.onResume()
        init()
    }
}
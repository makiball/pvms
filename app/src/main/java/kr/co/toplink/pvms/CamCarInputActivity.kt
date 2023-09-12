package kr.co.toplink.pvms

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.databinding.ActivityCamCarInputBinding
import kr.co.toplink.pvms.util.InputCheck
import kr.co.toplink.pvms.viewmodel.CamCarViewModel
import kr.co.toplink.pvms.viewmodel.ViewModelFactory
import java.util.*

class CamCarInputActivity: AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCamCarInputBinding
    private lateinit var camCarViewModel: CamCarViewModel
    private var inputcheck = InputCheck()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamCarInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG,"차량입력 실행!!!")


        val value = intent.getStringExtra("carnum").toString()
        // db = CarInfoDatabase.getInstance(this)!!


        binding.apply {
            binding.carnumInpt.setText(value)
            binding.carnumInpt.requestFocus()
        }

        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                RegSwitch.setRegClose()
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{
            RegSwitch.setRegClose()
            finish()
        }

        /* 미등록 차량 데이터베이스 등록 */
        binding.regBt.setOnClickListener {

            val datepatterned = Date()

            val carnuminput = binding.carnumInpt.text.toString().replace("\\s+".toRegex(), "")
            var phoneinput = binding.phoneInpt.text.toString().replace("\\s+".toRegex(), "")
            val etcinpt = binding.etcInpt.text.toString()


            if(carnuminput.isEmpty() == true) {
                Toast.makeText(this, "차량 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* 차량 번호 정규식 */
            val carnumcheck = inputcheck.getIsNumber(carnuminput)
            if(!carnumcheck) {
                Toast.makeText(this, "조회 할수 없는 차량 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /* 휴대폰 번호 정규식 */
            if(phoneinput.isNotEmpty() == true) {
                if(Regex("^010.*").matches(phoneinput)) {
                    phoneinput = "${phoneinput.substring(0, 3)}-${phoneinput.substring(3, 7)}-${phoneinput.substring(7)}"
                } else {
                    phoneinput = "${phoneinput.substring(0, 3)}-${phoneinput.substring(3, 6)}-${phoneinput.substring(6)}"
                }
            }

            val carInfoToday = CarInfoToday(
                carnumber = carnuminput,
                phone  = phoneinput,
                date  = datepatterned,
                etc  = etcinpt,
                type = 1,
                lawstop = 0
            )

            camCarViewModel.carInfoInsertToday(carInfoToday)

            /*
            insertDatabase(carnuminput, phoneinput, etcinpt)
             */
            camCarViewModel.carInfoToday(carnuminput)
            camCarViewModel.camcarinfo.observe(this, EventObserver {
                if(it.carnumber == carnuminput) {
                    finish()
                }
            })

        }

        init()
    }

    fun init() {
        camCarViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(CamCarViewModel::class.java)
    }

    // 엑셀 값이 넘어오면 데이터베이스 처리를 한다.
    /*
    private fun insertDatabase(carnuminput: String, phoneinput: String, etcinpt: String) {
        val datepatterned = Date()
        CoroutineScope(Dispatchers.IO).launch {
            db.CarInfoDao().CarInfoInsertToday(
                CarInfoToday(
                    carnumber = carnuminput,
                    phone = phoneinput,
                    date = datepatterned,
                    etc = etcinpt,
                    type = 1
                )
            )
        }
    }
    */

}
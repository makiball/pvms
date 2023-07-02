package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.CarInfoDatabase
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.databinding.ActivityCamCarInputBinding
import kr.co.toplink.pvms.databinding.ActivityCamCarSearchBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.util.InputCheck
import java.util.*

class CamCarInputActivity: AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCamCarInputBinding
    private lateinit var viewModel: CarNumberSearchViewModel
    private lateinit var db: CarInfoDatabase
    private var inputcheck = InputCheck()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamCarInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val value = intent.getStringExtra("carnum").toString()
        db = CarInfoDatabase.getInstance(this)!!

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
            val carnuminput = binding.carnumInpt.text.toString()
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

            insertDatabase(carnuminput, phoneinput, etcinpt)
            Toast.makeText(this, "입력 성공 차량 번호 조회 가능!", Toast.LENGTH_SHORT).show()
        }
    }

    // 엑셀 값이 넘어오면 데이터베이스 처리를 한다.
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

}
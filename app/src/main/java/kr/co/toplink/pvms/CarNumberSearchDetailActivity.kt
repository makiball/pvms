package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.data.CarInfoListModel
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchDetailBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.util.DateToYmdhis
import kr.co.toplink.pvms.util.DeleteDialog
import kr.co.toplink.pvms.util.PhoneHidden
import java.text.SimpleDateFormat
import java.util.*

class CarNumberSearchDetailActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchDetailBinding
    private lateinit var viewModel: CarNumberSearchViewModel
    private var id : Int = 0
    private var phone : String? = ""

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarnumbersearchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val carinfolistmodel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(KEY_CARINFOLIST_MODEL, CarInfoListModel::class.java)
        } else {
            intent.getParcelableExtra<CarInfoListModel>(KEY_CARINFOLIST_MODEL)
        }

        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{  finish()  }

        carinfolistmodel?.let {

            val formattedDate = DateToYmdhis(it.carinfolist.date)
            phone = it.carinfolist.phone
            binding.carnumberTxt.text = it.carinfolist.carnumber
            binding.phone.text = phone?.let { it -> PhoneHidden(it) }
            binding.etc.text = it.carinfolist.etc
            binding.date.text = "등록일 : $formattedDate"

            id = it.carinfolist.id.toInt()
        }

        /* 전화 */
        binding.call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startActivity(intent)
        }

        binding.sms.setOnClickListener {

            val message = "차좀 빼줘!!!"

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phone")
                putExtra("sms_body", message)
            }
            startActivity(intent)
        }


        /* 삭제 */
        binding.delete.setOnClickListener {

            //Toast.makeText(this, " $id ", Toast.LENGTH_SHORT).show()

            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                viewModel.idDeteData(this, id)
                finish()
            }
            dlg.show(msg)
        }

        init()
    }



    private fun init() {
        viewModel = ViewModelProvider(this).get(CarNumberSearchViewModel::class.java)
    }
}
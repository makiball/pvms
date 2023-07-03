package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.data.CarInfoListModel
import kr.co.toplink.pvms.data.SmsManagerList
import kr.co.toplink.pvms.data.SmsMangerModel
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchDetailBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.model.SmsManagerViewModel
import kr.co.toplink.pvms.util.DateToYmdhis
import kr.co.toplink.pvms.util.DeleteDialog
import kr.co.toplink.pvms.util.PhoneHidden


class CarNumberSearchDetailActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchDetailBinding
    private lateinit var viewModel: CarNumberSearchViewModel
    private lateinit var viewModel2: SmsManagerViewModel
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


        /* 스피너 설정 */
        binding.textItem.setOnItemClickListener(OnItemClickListener { adapterView, view, position, id ->
            //binding.textShowItem.setText(adapterView.getItemAtPosition(position) as String)
        })


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
    }

    private fun generateMockCarinfo(smsmaglist: MutableList<SmsManager>): List<String> {
        val smsList = java.util.ArrayList<String>()
        smsmaglist.forEach{
            //val smsMagList = SmsManagerList(it.id, it.smstitle, it.smscontent)
            //smsList.add(SmsMangerModel(smsMagList))
            smsList.add(it.smstitle)
        }
        return smsList
    }
}
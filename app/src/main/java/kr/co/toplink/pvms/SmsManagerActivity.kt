package kr.co.toplink.pvms

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.toplink.pvms.adapter.SingleViewBinderListAdapter
import kr.co.toplink.pvms.data.SmsManagerList
import kr.co.toplink.pvms.data.SmsMangerModel
import kr.co.toplink.pvms.database.CarInfoDatabase
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.databinding.ActivitySmsManagerBinding
import kr.co.toplink.pvms.model.SmsManagerViewModel
import kr.co.toplink.pvms.viewholder.ItemBinder
import kr.co.toplink.pvms.viewholder.SmsManagerViewBinder
import java.util.ArrayList

class SmsManagerActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivitySmsManagerBinding
    private lateinit var listAdapter: SingleViewBinderListAdapter
    private lateinit var viewModel: SmsManagerViewModel
    private lateinit var db: CarInfoDatabase

    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = CarInfoDatabase.getInstance(this)!!

        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{  finish()  }

        val postCardViewBinder = SmsManagerViewBinder { binding, SmsMangerModel, String ->
//            gotoDetailWithTransition(SmsMangerModel, binding)

            //viewModel.deletebyid(this, Int)

            /* 삭제시 */
            if(String == "delete") { viewModel.deletebyid(this, SmsMangerModel.smsmanager.id) }

            /* 수정시 */
            if(String == "modify") {

                Log.d(TAG, "=====> $String ${SmsMangerModel.smsmanager.smstitle}")

                id = SmsMangerModel.smsmanager.id

                this.binding.smstitleInpt.setText("${SmsMangerModel.smsmanager.smstitle}")
                this.binding.smscontInpt.setText("${SmsMangerModel.smsmanager.smscontent}")
                this.binding.regBt.visibility = View.GONE
                this.binding.modifyBt.visibility = View.VISIBLE
            }
        }

        listAdapter = SingleViewBinderListAdapter(postCardViewBinder as ItemBinder)
        binding.recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@SmsManagerActivity)
        }

        init()

        /* 문자 메세지 등록 */
        binding.regBt.setOnClickListener {

            var smstitleinpt = binding.smstitleInpt.text.toString()
            var smscontinpt = binding.smscontInpt.text.toString()

            if(smstitleinpt == "" || smscontinpt == "") {
                Toast.makeText(this, "문자 내용을 입력해주세요.!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                db.CarInfoDao().SmsMagInsert(
                    SmsManager(
                        smstitle = smstitleinpt,
                        smscontent = smscontinpt
                    )
                )
            }

            binding.smstitleInpt.setText("")
            binding.smscontInpt.setText("")
        }

        /* 수정 등록 */
        binding.modifyBt.visibility  = View.INVISIBLE
        binding.modifyBt.setOnClickListener {
            val smstitleinpt = binding.smstitleInpt.text.toString()
            val smscontinpt = binding.smscontInpt.text.toString()

            if(smstitleinpt == "" || smscontinpt == "") {
                Toast.makeText(this, "문자 내용을 입력해주세요.!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                db.CarInfoDao().SmsMagUpdatebyid(
                    SmsManager(
                        id = id,
                        smstitle = smstitleinpt,
                        smscontent = smscontinpt
                    )
                )
            }

            binding.smstitleInpt.setText("")
            binding.smscontInpt.setText("")

            binding.regBt.visibility = View.VISIBLE
            binding.modifyBt.visibility = View.GONE
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(SmsManagerViewModel::class.java)
        viewModel.allListSms(this)
        viewModel.smsManagerList.observe(this, androidx.lifecycle.Observer {
            it.apply {
                listAdapter.submitList(generateMockCarinfo(this))
            }
        })
    }

    private fun generateMockCarinfo(smsmaglist: MutableList<SmsManager>): List<SmsMangerModel> {
        val smsList = ArrayList<SmsMangerModel>()
        SmsMsgStore.setSmsMsgList(smsmaglist)
        smsmaglist.forEach{
            val smsMagList = SmsManagerList(it.id, it.smstitle, it.smscontent)
            smsList.add(SmsMangerModel(smsMagList))
        }
        return smsList
    }
}

/* 싱글톤 문자 메세지를 가져오기 */
object SmsMsgStore {
    private var smsMsgList = mutableListOf<SmsManager>()
    fun setSmsMsgList(smsmsglist : MutableList<SmsManager>) {
        this.smsMsgList.clear()
        this.smsMsgList = smsmsglist
    }
}
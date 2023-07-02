package kr.co.toplink.pvms

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.toplink.pvms.adapter.SingleViewBinderListAdapter
import kr.co.toplink.pvms.data.CarInfoListToday
import kr.co.toplink.pvms.data.CarInfoListTodayModel
import kr.co.toplink.pvms.data.SmsManagerList
import kr.co.toplink.pvms.data.SmsMangerModel
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.databinding.ActivitySmsManagerBinding
import kr.co.toplink.pvms.databinding.CarinfoItemTodayLayoutBinding
import kr.co.toplink.pvms.databinding.SmsMagItemLayoutBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.model.SmsManagerViewModel
import kr.co.toplink.pvms.viewholder.ItemBinder
import kr.co.toplink.pvms.viewholder.SmsManagerViewBinder
import java.util.ArrayList

class SmsManagerActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivitySmsManagerBinding
    private lateinit var listAdapter: SingleViewBinderListAdapter
    private lateinit var viewModel: SmsManagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{  finish()  }

        val postCardViewBinder = SmsManagerViewBinder { binding, SmsMangerModel ->
//            gotoDetailWithTransition(SmsMangerModel, binding)
        }

        listAdapter = SingleViewBinderListAdapter(postCardViewBinder as ItemBinder)
        binding.recyclerView.apply {
            this.adapter = listAdapter
            layoutManager = LinearLayoutManager(this@SmsManagerActivity)
        }

        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(SmsManagerViewModel::class.java)
        viewModel.allListSms(this)
        viewModel.smsManagerList

    }
}
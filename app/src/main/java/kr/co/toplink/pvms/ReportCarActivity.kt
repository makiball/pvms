package kr.co.toplink.pvms

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.adapter.CarNumberSearchAdapter
import kr.co.toplink.pvms.adapter.ReportCarAdapter
import kr.co.toplink.pvms.databinding.ActivityReportcarBinding
import kr.co.toplink.pvms.viewmodel.CarInfoViewModel
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel
import kr.co.toplink.pvms.viewmodel.UserViewModel
import kr.co.toplink.pvms.viewmodel.ViewModelFactory

class ReportCarActivity: AppCompatActivity() {

    private lateinit var binding: ActivityReportcarBinding
    private lateinit var reportCarViewModel: ReportCarViewModel
    private lateinit var reportCarAdapter: ReportCarAdapter

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportcarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기시 현재 엑티비티 닫기
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        binding.backBt.setOnClickListener{
            finish()
        }

        init()

    }

    private fun init() {

        reportCarViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(ReportCarViewModel::class.java)
        userViewModel =  ViewModelProvider(this, ViewModelFactory(this)).get(UserViewModel::class.java)

        reportCarViewModel.reportList()
        attachObserver()
    }


    private fun attachObserver() {
        reportCarAdapter = ReportCarAdapter(reportCarViewModel, userViewModel)
        reportCarViewModel.reports.observe(this)  {
            binding.recyclerView.apply {
                adapter = reportCarAdapter
                layoutManager = GridLayoutManager(this@ReportCarActivity, 1)
                adapter!!.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            reportCarAdapter.apply {
                submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }
}
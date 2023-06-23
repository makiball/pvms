package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.co.toplink.pvms.adapter.CarInfoAdapter
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.util.CarInfoDetailDialog
import kr.co.toplink.pvms.util.DeleteDialog
class CarNumberSearchActivity: AppCompatActivity(), CarInfoAdapter.CarInfoAdapterListener {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchBinding

    private lateinit var viewModel: CarNumberSearchViewModel

    private val carinfoadapter = CarInfoAdapter(this)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarnumbersearchBinding.inflate(layoutInflater)
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

        binding.recyclerView.apply {
            adapter = carinfoadapter
        }
        binding.recyclerView.adapter = carinfoadapter

        var isChecked = 1

        binding.searchRdg.setOnCheckedChangeListener { radioGroup, i ->
            Log.d(TAG, "=====> i")

            var msg = "차량번호"

            when (i) {
                R.id.check_1 -> { msg = "차량번호"; isChecked = 1 }
                R.id.check_2 -> { msg = "휴대폰번호" ; isChecked = 2 }
                R.id.check_3 -> { msg = "비고"; isChecked = 3 }
                else ->  { msg = "차량번호"; isChecked = 1 }
            }

            binding.searchInpt.hint = msg
        }

        binding.searchBt.apply{
            setOnClickListener {
                val searchtext = binding.searchInptText.text.toString()


                when(isChecked) {
                    1 -> {
                        viewModel.searchCarnum(this@CarNumberSearchActivity, searchtext)
                    }
                    2 -> {
                        viewModel.searchPhone(this@CarNumberSearchActivity, searchtext)
                    }
                    3 -> {
                        viewModel.searchEtc(this@CarNumberSearchActivity, searchtext)
                    }
                    else -> {
                        viewModel.searchCarnum(this@CarNumberSearchActivity, searchtext)
                    }
                }
            }
        }


       init()

        binding.alldelte.setOnClickListener {
            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                /* 모두 삭제 처리 */
                viewModel.allDeteData(this)
            }
            dlg.show(msg)
        }
    }


    private fun init() {
        viewModel = ViewModelProvider(this).get(CarNumberSearchViewModel::class.java)
        attachObserver()

        viewModel.searchCarnum(this@CarNumberSearchActivity, "")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun attachObserver() {
        viewModel.carinfoList.observe(this, androidx.lifecycle.Observer {
            it?.apply {
                binding.totalreg.text = "총 수량 : ${this.size}대"
                carinfoadapter.submitList(this)
                carinfoadapter.notifyDataSetChanged();
            }
        })
    }


    override fun onCarInfoListClicked(carNumView: View, carInfoList: CarInfoList) {

        val msg = "차량 등록 상세 정보!"
        val dlg = CarInfoDetailDialog(this)
        dlg.setOnOKClickedListener{

            viewModel.idDeteData(this@CarNumberSearchActivity, carInfoList.id)



        }
        dlg.show(carInfoList)

        Log.d(TAG,"=====> $carInfoList")
    }

}
package kr.co.toplink.pvms

import android.annotation.SuppressLint
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
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListModel
import kr.co.toplink.pvms.data.Option
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchBinding
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.util.DeleteDialog
import kr.co.toplink.pvms.viewholder.ItemBinder
import kr.co.toplink.pvms.viewholder.PostCardViewBinder

const val KEY_CARINFOLIST_MODEL = "car-info-model"

class CarNumberSearchActivity: AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchBinding
    private lateinit var viewModel: CarNumberSearchViewModel
    private lateinit var listAdapter: SingleViewBinderListAdapter
    private var selectedOption : Option = Option.carnumber

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

        val postCardViewBinder = PostCardViewBinder { binding, CarInfoListModel ->
            gotoDetailWithTransition(CarInfoListModel, binding)
        }
        listAdapter = SingleViewBinderListAdapter(postCardViewBinder as ItemBinder)
        binding.recyclerView.apply {
            this.adapter = listAdapter
            layoutManager = LinearLayoutManager(this@CarNumberSearchActivity)
        }

        binding.check1.isChecked = true
        binding.searchRdg.setOnCheckedChangeListener { group, checkedId  ->
            //var msg = "차량번호"
            selectedOption = when (checkedId) {
                R.id.check_1 -> Option.carnumber
                R.id.check_2 -> Option.phone
                R.id.check_3 -> Option.etc
                else -> Option.carnumber // 기본값 설정
            }
            viewModel.setSelectedOption(selectedOption)
        }

        binding.searchBt.apply{

            setOnClickListener {
                val searchtext = binding.searchInptText.text.toString()

                when(selectedOption) {
                    Option.carnumber -> {
                        viewModel.searchCarnum(this@CarNumberSearchActivity, searchtext)
                    }
                    Option.phone -> {
                        viewModel.searchPhone(this@CarNumberSearchActivity, searchtext)
                    }
                    Option.etc -> {
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

    private fun gotoDetailWithTransition (
        carInfoListModel: CarInfoListModel,
        binding : CarinfoItemLayoutBinding
    ) {
        val intent =
            Intent(this, CarNumberSearchDetailActivity::class.java)
        intent.putExtra(KEY_CARINFOLIST_MODEL, carInfoListModel)

        val pairIvCarnum = Pair<View, String>(binding.carnumberTxt, binding.carnumberTxt.transitionName)

        val options = ActivityOptions
            .makeSceneTransitionAnimation(
                this,
//                pairTvTitle,
                pairIvCarnum
            )

        // start the new activity
        startActivity(intent, options.toBundle())
    }


    override fun onResume() {
        super.onResume()
        //Toast.makeText(this, " 화면활성화 ", Toast.LENGTH_SHORT).show()
        init()
    }


    private fun init() {
        viewModel = ViewModelProvider(this).get(CarNumberSearchViewModel::class.java)
        viewModel.searchCarnum(this, "")
        attachObserver()
    }

    private fun attachObserver() {
        viewModel.carinfoList.observe(this, androidx.lifecycle.Observer {

            it?.apply {
                binding.totalreg.text = "총 수량 : ${this.size}대"
                listAdapter.submitList(generateMockCarinfo(this))
                //listAdapter.submitList(this)
            }
        })
        viewModel.selectedOption.observe(this) { selectedOption ->
            // 선택된 옵션에 대한 처리 작업
            /*
            when (selectedOption) {
                Option.carnumber -> {
                    // Option 1 선택 시 처리할 작업
                }
                Option.phone -> {
                    // Option 2 선택 시 처리할 작업
                }
                Option.etc -> {
                    // Option 3 선택 시 처리할 작업
                }
            }*/

            binding.searchInpt.hint = selectedOption.text
        }
    }

    private fun generateMockCarinfo(carInfo: List<CarInfoList>): List<CarInfoListModel> {
        val carInfoList = ArrayList<CarInfoListModel>()

        carInfo.forEach{
            val carinfolist = CarInfoList(it.id, it.carnumber, it.phone, it.date, it.etc)
            carInfoList.add(CarInfoListModel(carinfolist))
        }

        return carInfoList
    }
}
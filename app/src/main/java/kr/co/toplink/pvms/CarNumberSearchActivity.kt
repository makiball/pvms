package kr.co.toplink.pvms

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.adapter.CarNumberSearchAdapter
import kr.co.toplink.pvms.adapter.SingleViewBinderListAdapter
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListModel
import kr.co.toplink.pvms.data.Option
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchBinding
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding
import kr.co.toplink.pvms.model.CarNumberSearchViewModel
import kr.co.toplink.pvms.util.DeleteDialog
import kr.co.toplink.pvms.viewholder.ItemBinder
import kr.co.toplink.pvms.viewholder.PostCardViewBinder
import kr.co.toplink.pvms.viewmodel.CarInfoViewModel
import kr.co.toplink.pvms.viewmodel.ExcellViewModel
import kr.co.toplink.pvms.viewmodel.ViewModelFactory

const val KEY_CARINFOLIST_MODEL = "car-info-model"

class CarNumberSearchActivity: AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    private lateinit var binding: ActivityCarnumbersearchBinding
    private lateinit var viewModel: CarNumberSearchViewModel
    private lateinit var carinfviewModel: CarInfoViewModel

    private lateinit var listAdapter: SingleViewBinderListAdapter
    private var selectedOption : Option = Option.carnumber

    lateinit var carnumbersearchadapter : CarNumberSearchAdapter

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

        /*
        val postCardViewBinder = PostCardViewBinder { binding, CarInfoListModel ->
            gotoDetailWithTransition(CarInfoListModel, binding)
        }
        listAdapter = SingleViewBinderListAdapter(postCardViewBinder as ItemBinder)
        binding.recyclerView.apply {
            this.adapter = listAdapter
            layoutManager = LinearLayoutManager(this@CarNumberSearchActivity)
        }
         */

        binding.check1.isChecked = true
        binding.searchRdg.setOnCheckedChangeListener { group, checkedId  ->
            selectedOption = when (checkedId) {
                R.id.check_1 -> Option.carnumber
                R.id.check_2 -> Option.phone
                R.id.check_3 -> Option.etc
                else -> Option.carnumber // 기본값 설정
            }
            carinfviewModel.setSelectedOption(selectedOption)
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

        binding.alldelte.setOnClickListener {
            val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
            val dlg = DeleteDialog(this)
            dlg.setOnOKClickedListener{
                /* 모두 삭제 처리 */
                viewModel.allDeteData(this)
            }
            dlg.show(msg)
        }

       init()
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
        //init()
    }


    private fun init() {
        //viewModel = ViewModelProvider(this).get(CarNumberSearchViewModel::class.java)
        //viewModel.searchCarnum(this, "")

        carinfviewModel = ViewModelProvider(this, ViewModelFactory(this)).get(CarInfoViewModel::class.java)
        carinfviewModel.setSelectedOption(selectedOption)
        carinfviewModel.getCarInfo()
        attachObserver()
    }

    private fun attachObserver() {

        carinfviewModel.searchChk.observe(this, EventObserver {
            /*
            when (it.name) {
                "carnumber" -> binding.check1.isChecked = true
                "phone" -> binding.check2.isChecked = true
                "etc" -> binding.check3.isChecked = true
                else -> binding.check1.isChecked = true
            }
             */
            binding.searchInpt.hint = selectedOption.text
        })

        carnumbersearchadapter = CarNumberSearchAdapter(carinfviewModel)
        carinfviewModel.carinfos.observe(this) {
            binding.recyclerView.apply {
                adapter = carnumbersearchadapter
                layoutManager = GridLayoutManager(this@CarNumberSearchActivity, 1)
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            carnumbersearchadapter.apply {
                submitList(it)
            }
            binding.totalreg.text = "총 수량 : ${it.size}대"
        }

        /*
        carinfviewModel.carinfos.observe(this){
            binding.totalreg.text = "총 수량 : ${it.size}대"
            listAdapter.submitList(it)
        }


        viewModel.carinfoList.observe(this, androidx.lifecycle.Observer {
            it?.apply {
                binding.totalreg.text = "총 수량 : ${this.size}대"
                listAdapter.submitList(generateMockCarinfo(this))
                //listAdapter.submitList(this)
            }
        })

        viewModel.selectedOption.observe(this) { selectedOption ->
            binding.searchInpt.hint = selectedOption.text
        }
         */
    }

    /*
    private fun generateMockCarinfo(carInfo: List<CarInfo>): List<CarInfoListModel> {
        val carInfoList = ArrayList<CarInfoListModel>()
        carInfo.forEach{
            val carinfolist = CarInfoList(it.id, it.carnumber, it.phone, it.date, it.etc)
            carInfoList.add(CarInfoListModel(carinfolist))
        }
        return carInfoList
    }
     */
}
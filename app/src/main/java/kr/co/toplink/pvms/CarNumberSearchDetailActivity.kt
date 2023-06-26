package kr.co.toplink.pvms

import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import kr.co.toplink.pvms.data.CarInfoListModel
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchBinding
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchDetailBinding

class CarNumberSearchDetailActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchDetailBinding

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




    }

}
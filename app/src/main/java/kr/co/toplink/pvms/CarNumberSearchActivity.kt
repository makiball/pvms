package kr.co.toplink.pvms

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import kr.co.toplink.pvms.databinding.ActivityCarnumbersearchBinding

class CarNumberSearchActivity: AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumbersearchBinding

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
    }

}
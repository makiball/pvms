package kr.co.toplink.pvms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kr.co.toplink.pvms.databinding.ActivityMainBinding
import kr.co.toplink.pvms.model.ActivityClassModel
import java.util.ArrayList
import kr.co.toplink.pvms.R

class MainActivity : AppCompatActivity() {

    private val activityClassModels = ArrayList<ActivityClassModel>()

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addActivity()

        binding.carnumberregBt.setOnClickListener {
            activitygo(0)
        }
    }

    private fun addActivity() {
        activityClassModels.add(
            ActivityClassModel(
                CarNumberRegActivity::class.java,
                getString(R.string.carnumberreg_bt)
            )
        )
    }

    /* 엑티비티 바로가기 */
    fun activitygo(position: Int) {
        Intent(this, activityClassModels[position].clazz).also {
            startActivity(it)
        }
    }
}
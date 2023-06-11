package kr.co.toplink.pvms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import kr.co.toplink.pvms.databinding.ActivityMainBinding
import kr.co.toplink.pvms.model.ActivityClassModel
import java.util.ArrayList

class CarNumberRegActivity : AppCompatActivity() {

    private val activityClassModels = ArrayList<ActivityClassModel>()

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.carnumberregBt.apply {

        }

    }

    /* 엑티비티 바로가기 */
    fun activitygo(view: View, position: Int) {
        Intent(this, activityClassModels[position].clazz).also {
            startActivity(it)
        }
    }
}
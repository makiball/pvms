package kr.co.toplink.pvms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.toplink.pvms.databinding.ActivityCarnumberregBinding
import java.util.ArrayList

class CarNumberRegActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityCarnumberregBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarnumberregBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
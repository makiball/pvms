package kr.co.toplink.pvms.util

import android.app.Dialog
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.databinding.CarinfoDetailDialogBinding

class CarInfoDetailDialog(private val context : AppCompatActivity) {

    private lateinit var binding : CarinfoDetailDialogBinding
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감

    private lateinit var listener : MyDialogOKClickedListener

    fun show(carInfoList: CarInfoList) {
        binding = CarinfoDetailDialogBinding.inflate(context.layoutInflater)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        //dlg.setTitle("패스워드 입력")

        dlg.setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함


        binding.carnumInpt.setText(carInfoList.carnumber)
        binding.phoneInpt.setText(carInfoList.phone)
        binding.etcInpt.setText(carInfoList.etc)
        //binding.dateInpt.setText(carInfoList.date.toString())

        binding.ok.setOnClickListener {
            listener.onOKClicked("")
            dlg.dismiss()
        }


        //cancel 버튼 동작
        binding.cancel.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
        dlg.window?.setLayout(1000, 1200)

    }

    fun setOnOKClickedListener(listener: (String) -> Unit) {
        this.listener = object: MyDialogOKClickedListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }

    interface MyDialogOKClickedListener {
        fun onOKClicked(content : String)
    }

}
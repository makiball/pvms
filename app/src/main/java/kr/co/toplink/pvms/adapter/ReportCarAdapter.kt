package kr.co.toplink.pvms.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings.System.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.CamCarSearchDetailActivity
import kr.co.toplink.pvms.CarInfoTotalActivity
import kr.co.toplink.pvms.EventObserver
import kr.co.toplink.pvms.R
import kr.co.toplink.pvms.data.KakaoAlim
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.database.Report
import kr.co.toplink.pvms.databinding.ReportCarListAdapterBinding
import kr.co.toplink.pvms.util.*
import kr.co.toplink.pvms.viewmodel.CamCarViewModel
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel
import kr.co.toplink.pvms.viewmodel.UserViewModel

class ReportCarAdapter(private val viewModel: ReportCarViewModel, private val userViewModel: UserViewModel) :
    ListAdapter<Report, ReportCarAdapter.ReportCarViewHolder>(ReportCarDiffCallback()) {


    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ReportCarListAdapterBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportCarViewHolder {

        binding =
            ReportCarListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportCarViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReportCarViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }

    inner class ReportCarViewHolder(private val binding: ReportCarListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(report: Report, context: Context) {

            val total = report.total_type_0 + report.total_type_1
            val formattedDate = DateToYmdhis(report.date)

            /*

            binding.totalTxt.text = "총 차량 수 : ${total.toString()}대 " + "${report.id}"
            binding.totalRegTxt.text = "등        록  : ${report.total_type_0.toString()}대"
            binding.totalRegNoTxt.text = "미  등  록  : ${report.total_type_1.toString()}대"
            binding.lawstopTxt.text = "불법 주차 : ${report.total_lawstop.toString()}대"
            binding.dateTxt.text = "작  성  일  : $formattedDate"
            */

            binding.totalTxt.text = context.getString(R.string.camcarsearch_total_txt).format(total)
            binding.totalRegTxt.text = context.getString(R.string.camcarsearch_totalreg_txt).format(report.total_type_0)
            binding.totalRegNoTxt.text = context.getString(R.string.camcarsearch_totalnotreg_txt).format(report.total_type_1)
            binding.lawstopTxt.text = context.getString(R.string.camcarsearch_lawstop_txt).format(report.total_lawstop)
            binding.dateTxt.text = context.getString(R.string.CamCarSearchDetail_date_txt).format(formattedDate)

            binding.constraintLayout.setOnClickListener {
                //Toast.makeText(context, " 테스트 ", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(context, CarInfoTotalActivity::class.java)
                intent.putExtra("reportid",report.id)
                ContextCompat.startActivity(context, intent, null)
            }
            /*
            binding.delete.setOnClickListener {

                val msg = "데이터를 삭제하시면 복구 하실수 없습니다. "
                val dlg = DeleteDialog(context as AppCompatActivity)
                dlg.setOnOKClickedListener{

                    viewModel.reportDeleteById(report.id)
                    viewModel.carInfoTotalDelteReportId(report.id)

                }
                dlg.show(msg)

            }

            binding.kakaotalk.setOnClickListener {

                if(id == "" || id == "비회원") {
                    Toast.makeText(context as AppCompatActivity, " 설정에서 알림톡 로그인을 해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val msg = "관제 번호로 전달 할 내용을 입력해주세요."
                val dlg = ReportDialog(context as AppCompatActivity)
                dlg.setOnOKClickedListener{
                    Log.d(TAG,"======> $it")

                    val cont = formattedDate + "|" + total + "|" + report.total_type_0 + "|" + report.total_type_1 + "|" + "불법주차 : ${report.total_lawstop}대\n" + it

                    val kakaoalim = KakaoAlim(id, cont, "01099999999")
                    userViewModel.kakaoReportSend(kakaoalim)
                    userViewModel.userResponse.observe(context, EventObserver{
                        //Toast.makeText(context, "${it.msg}", Toast.LENGTH_LONG).show()
                        //result
                    })
                }
                dlg.show(msg)
            }
            */

            binding.executePendingBindings()
        }
    }
}

class ReportCarDiffCallback : DiffUtil.ItemCallback<Report>() {
    override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
        return oldItem == newItem
    }
}
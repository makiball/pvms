package kr.co.toplink.pvms.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.CamCarSearchDetailActivity
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.databinding.CarinfoItemTodayLayoutBinding
import kr.co.toplink.pvms.util.DateToYmdhis
import kr.co.toplink.pvms.util.PhoneHidden
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel

class CarInfoTotalSearchAdapter(private val viewModel: ReportCarViewModel) :
    ListAdapter<CarInfoTotal, CarInfoTotalSearchAdapter.CarInfoTotalSearchViewHolder>(CarInfoTotalSearchDiffCallback()) {
    private lateinit var binding: CarinfoItemTodayLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarInfoTotalSearchViewHolder {
        binding =
            CarinfoItemTodayLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarInfoTotalSearchViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CarInfoTotalSearchViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }

    inner class CarInfoTotalSearchViewHolder(private val binding: CarinfoItemTodayLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(carInfoTotal: CarInfoTotal, context: Context) {

            val type_txt = when(carInfoTotal.type) {
                0 -> "등록"
                1 -> "미등록"
                else -> "등록"
            }

            if(carInfoTotal.type == 1) {
                binding.constraintLayout.setBackgroundColor(Color.rgb(231, 230, 230))
            }

            val lawstopTxt = if(carInfoTotal.lawstop == 1) "불법주차" else ""
            binding.lawstopTxt.text = lawstopTxt

            binding.typeTxt.text = type_txt
            binding.carnumberTxt.text = carInfoTotal.carnumber
            binding.phoneTxt.text = PhoneHidden(carInfoTotal.phone)
            binding.etcTxt.text = PhoneHidden(carInfoTotal.etc)
            //binding.clickListener = clickListener

            binding.constraintLayout.setOnClickListener {

                //Toast.makeText(context, " 테스트 ", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(context, CamCarSearchDetailActivity::class.java)
                intent.putExtra("carnum",carInfoTotal.carnumber)
                ContextCompat.startActivity(context, intent, null)
            }

            binding.executePendingBindings()
        }
    }
}

class CarInfoTotalSearchDiffCallback : DiffUtil.ItemCallback<CarInfoTotal>() {
    override fun areItemsTheSame(oldItem: CarInfoTotal, newItem: CarInfoTotal): Boolean {
        return oldItem.carnumber == newItem.carnumber
    }

    override fun areContentsTheSame(oldItem: CarInfoTotal, newItem: CarInfoTotal): Boolean {
        return oldItem == newItem
    }
}
package kr.co.toplink.pvms.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.database.CarInfoTotal
import kr.co.toplink.pvms.databinding.CarinfototalListAdapterBinding
import kr.co.toplink.pvms.util.DateToYmdhis
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel

class CarInfoTotalAdapter(private val viewModel: ReportCarViewModel) :
    ListAdapter<CarInfoTotal, CarInfoTotalAdapter.CarInfoTotalViewHolder>(CarInfoTotalDiffCallback()) {
    private lateinit var binding: CarinfototalListAdapterBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarInfoTotalViewHolder {
        binding =
            CarinfototalListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarInfoTotalViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CarInfoTotalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarInfoTotalViewHolder(private val binding: CarinfototalListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(carInfoTotal: CarInfoTotal) {

            val formattedDate = DateToYmdhis(carInfoTotal.date)

            val type = if(carInfoTotal.type == 0) "등록" else "미등록"
            val lawstop = if(carInfoTotal.lawstop == 1) "불법주차" else ""

            binding.totalTxt.text = "$formattedDate $type $lawstop"

        }
    }
}

class CarInfoTotalDiffCallback : DiffUtil.ItemCallback<CarInfoTotal>() {
    override fun areItemsTheSame(oldItem: CarInfoTotal, newItem: CarInfoTotal): Boolean {
        return oldItem.carnumber == newItem.carnumber
    }

    override fun areContentsTheSame(oldItem: CarInfoTotal, newItem: CarInfoTotal): Boolean {
        return oldItem == newItem
    }
}
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
import kr.co.toplink.pvms.databinding.ReportCarListAdapterBinding
import kr.co.toplink.pvms.util.PhoneHidden
import kr.co.toplink.pvms.viewmodel.CamCarViewModel
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel

class ReportCarAdapter(private val viewModel: ReportCarViewModel) :
    ListAdapter<CarInfoTotal, ReportCarAdapter.ReportCarViewHolder>(ReportCarDiffCallback()) {
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
        fun bind(carInfoTotal: CarInfoTotal, context: Context) {

            binding.executePendingBindings()
        }
    }
}

class ReportCarDiffCallback : DiffUtil.ItemCallback<CarInfoTotal>() {
    override fun areItemsTheSame(oldItem: CarInfoTotal, newItem: CarInfoTotal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CarInfoTotal, newItem: CarInfoTotal): Boolean {
        return oldItem == newItem
    }
}
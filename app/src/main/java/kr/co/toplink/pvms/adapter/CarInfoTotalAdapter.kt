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
import kr.co.toplink.pvms.databinding.ActivityCamCarSearchBinding
import kr.co.toplink.pvms.util.DateToYmdhis
import kr.co.toplink.pvms.util.PhoneHidden
import kr.co.toplink.pvms.viewmodel.ReportCarViewModel

class CarInfoTotalAdapter(private val viewModel: ReportCarViewModel) :
    ListAdapter<CarInfoTotal, CarInfoTotalAdapter.CarInfoTotalViewHolder>(CarInfoTotalDiffCallback()) {
    private lateinit var binding: ActivityCamCarSearchBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarInfoTotalViewHolder {
        binding =
            ActivityCamCarSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarInfoTotalViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CarInfoTotalViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }

    inner class CarInfoTotalViewHolder(private val binding: ActivityCamCarSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(carInfoTotal: CarInfoTotal, context: Context) {


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
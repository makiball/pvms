package kr.co.toplink.pvms.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.viewmodel.CarInfoViewModel
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding

class CarNumberSearchAdapter(private val viewModel: CarInfoViewModel) :
    ListAdapter<CarInfo, CarNumberSearchAdapter.CarNumberViewHolder>(CarNumberSearchDiffCallback()) {
    private lateinit var binding: CarinfoItemLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarNumberViewHolder {
        binding =
            CarinfoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarNumberViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CarNumberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CarNumberViewHolder(private val binding: CarinfoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(carinfo: CarInfo) {

            binding.carnumberTxt.setText(carinfo.carnumber)
            binding.phoneTxt.setText(carinfo.phone)
            binding.etcTxt.setText(carinfo.etc)

            binding.executePendingBindings()
        }
    }
}

class CarNumberSearchDiffCallback : DiffUtil.ItemCallback<CarInfo>() {
    override fun areItemsTheSame(oldItem: CarInfo, newItem: CarInfo): Boolean {
        return oldItem.carnumber == newItem.carnumber
    }

    override fun areContentsTheSame(oldItem: CarInfo, newItem: CarInfo): Boolean {
        return oldItem == newItem
    }
}
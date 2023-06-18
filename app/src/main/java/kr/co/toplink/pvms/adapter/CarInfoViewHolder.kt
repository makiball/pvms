package kr.co.toplink.pvms.adapter

import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding

class CarInfoViewHolder (
    val binding : CarinfoItemLayoutBinding,
    val listener : CarInfoAdapter.CarInfoAdapterListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(carinfo: CarInfoList) {
        binding.run{
            carinfolist = carinfo
            carinfolistListener = listener
            executePendingBindings()
        }
    }
}
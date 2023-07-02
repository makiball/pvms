package kr.co.toplink.pvms.viewholder

import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.R
import kr.co.toplink.pvms.data.CarInfoListTodayModel

import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding
import kr.co.toplink.pvms.util.PhoneHidden

class CarInfoListTodayViewBinder(
    private val onItemClick : ((CarinfoItemLayoutBinding, CarInfoListTodayModel) -> Unit)? = null
) : MappableItemViewBinder<CarInfoListTodayModel, CarInfoViewHolder>(CarInfoListTodayModel::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return CarInfoViewHolder(
            parent.inflate(getItemLayoutResource()),
            onItemClick
        )
    }

    override fun bindViewHolder(model: CarInfoListTodayModel, viewHolder: CarInfoViewHolder) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource(): Int {
        return R.layout.carinfo_item_layout
    }

    override fun areItemsTheSame(oldItem: CarInfoListTodayModel, newItem: CarInfoListTodayModel): Boolean {
        return oldItem.carinfolisttoday.id == newItem.carinfolisttoday.id
    }

    override fun areContentsTheSame(oldItem: CarInfoListTodayModel, newItem: CarInfoListTodayModel): Boolean {
        return oldItem == newItem
    }
}

class CarInfoViewHolder(
    private val binding: CarinfoItemLayoutBinding,
    private val onItemClick: ((CarinfoItemLayoutBinding, CarInfoListTodayModel) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: CarInfoListTodayModel) {
        val carinfo = model.carinfolisttoday

        val type = when(carinfo.type) {
            0 -> "등록차량"
            1 -> "미등록차량"
            else -> "등록차량"
        }

        if(carinfo.type == 1) {
            binding.typeTxt.setTextColor(Color.RED)
        }

        binding.carnumberTxt.text = carinfo.carnumber
        binding.phoneTxt.text = carinfo.phone?.let { PhoneHidden(it) }
        binding.etcTxt.text = carinfo.etc
        binding.typeTxt.text = type

        binding.constraintLayout.setOnClickListener {
            onItemClick?.invoke(binding, model)
        }
    }
}
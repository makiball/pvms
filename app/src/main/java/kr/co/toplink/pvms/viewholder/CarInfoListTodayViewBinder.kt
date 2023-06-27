package kr.co.toplink.pvms.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.R
import kr.co.toplink.pvms.data.CarInfoListModel
import kr.co.toplink.pvms.data.CarInfoListTodayModel
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding

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

        binding.carnumberTxt.text = carinfo.carnumber
        binding.phoneTxt.text = carinfo.phone
        binding.etcTxt.text = carinfo.etc

        binding.constraintLayout.setOnClickListener {
            onItemClick?.invoke(binding, model)
        }
    }
}
package kr.co.toplink.pvms.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.R
import kr.co.toplink.pvms.data.SmsMangerModel
import kr.co.toplink.pvms.databinding.SmsMagItemLayoutBinding


class SmsManagerViewBinder (
    private val onItemClick : ((SmsMagItemLayoutBinding, SmsMangerModel) -> Unit)? = null
): MappableItemViewBinder<SmsMangerModel, SmsManagerViewHolder>(SmsMangerModel::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SmsManagerViewHolder(
            parent.inflate(getItemLayoutResource()),
            onItemClick
        )
    }

    override fun bindViewHolder(model: SmsMangerModel, viewHolder: SmsManagerViewHolder) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource(): Int {
        return R.layout.sms_mag_item_layout
    }

    override fun areItemsTheSame(oldItem: SmsMangerModel, newItem: SmsMangerModel): Boolean {
        return oldItem.smsmanager.id == newItem.smsmanager.id
    }

    override fun areContentsTheSame(oldItem: SmsMangerModel, newItem: SmsMangerModel): Boolean {
        return oldItem == newItem
    }
}

class SmsManagerViewHolder(
    private val binding: SmsMagItemLayoutBinding,
    private val onItemClick: ((SmsMagItemLayoutBinding, SmsMangerModel) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: SmsMangerModel) {
        val smsmag = model.smsmanager

        binding.constraintLayout.setOnClickListener {
            onItemClick?.invoke(binding, model)
        }
    }
}
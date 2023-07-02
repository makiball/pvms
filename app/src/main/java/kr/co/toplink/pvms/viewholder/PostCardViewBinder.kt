package kr.co.toplink.pvms.viewholder
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.data.CarInfoListModel
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding
import kr.co.toplink.pvms.R
import kr.co.toplink.pvms.util.PhoneHidden

class PostCardViewBinder(
    private val onItemClick: ((CarinfoItemLayoutBinding, CarInfoListModel) -> Unit)? = null
) : MappableItemViewBinder<CarInfoListModel, PostCardViewHolder>(CarInfoListModel::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return PostCardViewHolder(
            parent.inflate(getItemLayoutResource()),
            onItemClick
        )
    }

    override fun bindViewHolder(model: CarInfoListModel, viewHolder: PostCardViewHolder) {
        viewHolder.bind(model)
    }

    override fun getItemLayoutResource(): Int {
        return R.layout.carinfo_item_layout
    }

    override fun areItemsTheSame(oldItem: CarInfoListModel, newItem: CarInfoListModel): Boolean {
        return oldItem.carinfolist.id == newItem.carinfolist.id
    }

    override fun areContentsTheSame(oldItem: CarInfoListModel, newItem: CarInfoListModel): Boolean {
        return oldItem == newItem
    }
}

class PostCardViewHolder(
    private val binding: CarinfoItemLayoutBinding,
    private val onItemClick: ((CarinfoItemLayoutBinding, CarInfoListModel) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: CarInfoListModel) {
        val carinfo = model.carinfolist

        binding.carnumberTxt.text = carinfo.carnumber
        binding.phoneTxt.text = carinfo.phone?.let { PhoneHidden(it) }
        binding.etcTxt.text = carinfo.etc

        binding.constraintLayout.setOnClickListener {
            onItemClick?.invoke(binding, model)
        }
    }
}

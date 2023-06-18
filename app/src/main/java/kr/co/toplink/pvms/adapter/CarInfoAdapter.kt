package kr.co.toplink.pvms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kr.co.toplink.pvms.data.CarInfoList
import kr.co.toplink.pvms.data.CarInfoListDiffCallback
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding

class CarInfoAdapter (
    private val listener : CarInfoAdapterListener
) : ListAdapter<CarInfoList, CarInfoViewHolder>(CarInfoListDiffCallback) {

    interface CarInfoAdapterListener {
        fun onCarInfoListClicked(carInfoView: View, carInfoList: CarInfoList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarInfoViewHolder {
        return CarInfoViewHolder(
            CarinfoItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: CarInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
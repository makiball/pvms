package kr.co.toplink.pvms.adapter

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.CarNumberSearchDetailActivity
import kr.co.toplink.pvms.KEY_CARINFOLIST_MODEL
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
        holder.bind(getItem(position), holder.itemView.context)
    }

    inner class CarNumberViewHolder(private val binding: CarinfoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(carinfo: CarInfo, context: Context) {

            binding.carnumberTxt.setText(carinfo.carnumber)
            binding.phoneTxt.setText(carinfo.phone)
            binding.etcTxt.setText(carinfo.etc)
            //binding.clickListener = clickListener

            binding.constraintLayout.setOnClickListener {

                //Toast.makeText(context, " 테스트 ", Toast.LENGTH_SHORT).show()

                val intent =
                    Intent(context, CarNumberSearchDetailActivity::class.java)
                intent.putExtra("carnum",carinfo.carnumber)
                startActivity(context, intent, null)
            }

            binding.executePendingBindings()
        }
    }

    class CarNumberSearchListener(val clickListener: (carinfo : CarInfo) -> Unit) {
        fun onClick(carinfo: CarInfo) = clickListener(carinfo)
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
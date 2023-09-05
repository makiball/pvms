package kr.co.toplink.pvms.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.pvms.CarNumberSearchDetailActivity
import kr.co.toplink.pvms.database.CarInfoToday
import kr.co.toplink.pvms.databinding.CarinfoItemLayoutBinding
import kr.co.toplink.pvms.viewmodel.CamCarViewModel

class CamCarSearchAdapter(private val viewModel: CamCarViewModel) :
    ListAdapter<CarInfoToday, CamCarSearchAdapter.CamCarSearchViewHolder>(CamCarSearchDiffCallback()) {
    private lateinit var binding: CarinfoItemLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CamCarSearchViewHolder {
        binding =
            CarinfoItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CamCarSearchViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CamCarSearchViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }

    inner class CamCarSearchViewHolder(private val binding: CarinfoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(carInfoToday: CarInfoToday, context: Context) {

            binding.carnumberTxt.setText(carInfoToday.carnumber)
            binding.phoneTxt.setText(carInfoToday.phone)
            binding.etcTxt.setText(carInfoToday.etc)
            //binding.clickListener = clickListener

            binding.constraintLayout.setOnClickListener {

                //Toast.makeText(context, " 테스트 ", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(context, CarNumberSearchDetailActivity::class.java)
                intent.putExtra("carnum",carInfoToday.carnumber)
                ContextCompat.startActivity(context, intent, null)
            }

            binding.executePendingBindings()
        }
    }
}

class CamCarSearchDiffCallback : DiffUtil.ItemCallback<CarInfoToday>() {
    override fun areItemsTheSame(oldItem: CarInfoToday, newItem: CarInfoToday): Boolean {
        return oldItem.carnumber == newItem.carnumber
    }

    override fun areContentsTheSame(oldItem: CarInfoToday, newItem: CarInfoToday): Boolean {
        return oldItem == newItem
    }
}
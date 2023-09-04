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
import kr.co.toplink.pvms.database.CarInfo
import kr.co.toplink.pvms.database.SmsManager
import kr.co.toplink.pvms.databinding.SmsMagItemLayoutBinding
import kr.co.toplink.pvms.viewmodel.SmsMngViewModel

class SmsManagerAdapter(private val smsmngviewModel: SmsMngViewModel) :
    ListAdapter<SmsManager, SmsManagerAdapter.SmsManagerViewHolder>(SmsManagerDiffCallback()){
    private lateinit var binding: SmsMagItemLayoutBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsManagerViewHolder {
        binding =
            SmsMagItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsManagerViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SmsManagerAdapter.SmsManagerViewHolder, position: Int) {
        holder.bind(getItem(position), holder.itemView.context)
    }

    inner class SmsManagerViewHolder(private val binding: SmsMagItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(smsManager: SmsManager, context: Context) {

            binding.smsTitleTxt.text = smsManager.smstitle
            binding.smsContTxt.text = smsManager.smscontent

            smsmngviewModel.smsManager(smsManager.id)

            binding.executePendingBindings()
        }
    }
}

class SmsManagerDiffCallback : DiffUtil.ItemCallback<SmsManager>() {
    override fun areItemsTheSame(oldItem: SmsManager, newItem: SmsManager): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SmsManager, newItem: SmsManager): Boolean {
        return oldItem == newItem
    }
}
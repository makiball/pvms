package kr.co.toplink.pvms

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter : RecyclerView.Adapter<BaseAdapter.MyViewHolder>() {

    private var listener: OnRecyclerViewItemClickListener? = null

    inner class MyViewHolder : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        // TODO #3
        internal fun bind(obj: Any) {
            // Set click listener
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener?.run {
                onItemClicked(v, layoutPosition)
            }
        }

    }

    interface OnRecyclerViewItemClickListener {
        /**
         * This is a callback method that be overridden by the class that implements this
         * interface
         */
        fun onItemClicked(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }

}
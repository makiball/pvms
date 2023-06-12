package kr.co.toplink.pvms

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter  {

    private var listener: OnRecyclerViewItemClickListener? = null

    interface OnRecyclerViewItemClickListener {
        fun onItemClicked(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnRecyclerViewItemClickListener) {
        this.listener = listener
    }

}
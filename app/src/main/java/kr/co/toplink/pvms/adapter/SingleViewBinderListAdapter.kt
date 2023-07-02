package kr.co.toplink.pvms.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kr.co.toplink.pvms.viewholder.ItemBinder

class SingleViewBinderListAdapter (
    private val viewBinder: ItemBinder,
    stateRestorationPolicy: StateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY,
    private val recycleChildrenOnDetach: Boolean = false
    ): ListAdapter<Any, ViewHolder>(SingleTypeDiffCallback(viewBinder)) {

    init {
        this.stateRestorationPolicy = stateRestorationPolicy
    }

    override fun getItemViewType(position: Int): Int =
        viewBinder.getItemLayoutResource()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recycleChildrenOnDetach) {
            val layoutManager = recyclerView.layoutManager
            (layoutManager as? LinearLayoutManager)?.recycleChildrenOnDetach = true
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return viewBinder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewBinder.bindViewHolder(currentList[position], holder)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        viewBinder.onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        viewBinder.onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}
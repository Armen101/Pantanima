package com.example.pantanima.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.GroupRowBinding
import com.example.pantanima.ui.customviews.ViewBinderHelper
import com.example.pantanima.ui.listeners.AdapterOnItemClickListener

class GroupsAdapter(val listener: AdapterOnItemClickListener<String>, val data: MutableList<String>) :
    RecyclerView.Adapter<GroupsAdapter.GroupVH>() {

    private val viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    init {
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupVH {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_row, parent, false)
        return GroupVH(rootView)
    }

    override fun onBindViewHolder(holder: GroupVH, position: Int) {
        holder.bind(data[position])
    }

    inner class GroupVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: GroupRowBinding? = DataBindingUtil.bind(itemView)

        internal fun bind(item: String) {
            viewBinderHelper.bind(binding?.swipeLayout, adapterPosition.toString())
            binding?.setVariable(BR.listener, listener)
            binding?.setVariable(BR.item, item)
            binding?.setVariable(BR.holder, this)
            binding?.executePendingBindings()
        }

        fun onDeleteClick(item: String) {
            binding?.swipeLayout?.close(true)
            listener.onDeleteClick(item)
        }
    }
}
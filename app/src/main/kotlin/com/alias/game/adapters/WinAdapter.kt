package com.example.pantanima.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.databinding.WinGroupRowBinding
import com.example.pantanima.ui.models.Group

class WinAdapter : RecyclerView.Adapter<WinAdapter.GroupVH>() {

    var data: List<Group>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupVH {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.win_group_row, parent, false)
        return GroupVH(rootView)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: GroupVH, position: Int) {
        holder.bind(data?.get(position))
    }

    inner class GroupVH(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: WinGroupRowBinding? = DataBindingUtil.bind(itemView)

        internal fun bind(item: Group?) {
            binding?.setVariable(BR.item, item)
            binding?.setVariable(BR.holder, this)
            binding?.executePendingBindings()
        }

        fun getRowText(item: Group) = "${item.name} (${item.getAnsweredCount()})"

        fun getIcon() = ContextCompat.getDrawable(itemView.context, when (adapterPosition) {
                0 -> R.drawable.ic_gold_star
                1 -> R.drawable.ic_silver_star
                2 -> R.drawable.ic_bronze_star
                else -> R.drawable.ic_empty_star
            })
    }

}
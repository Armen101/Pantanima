package com.example.pantanima.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.BR
import com.example.pantanima.R
import com.example.pantanima.ui.database.entity.Noun

interface WordsAdapterListener {
    fun onItemClick(item: Noun)
}

class WordsAdapter(val listener: WordsAdapterListener) :
    RecyclerView.Adapter<WordsAdapter.WordsViewHolder>() {

    private var data: List<Noun>? = null

    fun setData(data: List<Noun>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.word_item_row, parent, false)
        return WordsViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        holder.bind(data!![position])
    }

    inner class WordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)

        internal fun bind(item: Noun) {
            binding?.setVariable(BR.listener, listener)
            binding?.setVariable(BR.item, item)
            binding?.executePendingBindings()
        }
    }
}
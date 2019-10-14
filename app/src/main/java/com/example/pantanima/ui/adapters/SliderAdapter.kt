package com.example.pantanima.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.BR
import com.example.pantanima.R
import android.media.ToneGenerator
import android.media.AudioManager


class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderItemViewHolder>() {

    var callback: Callback? = null
    private val data: ArrayList<String> = ArrayList()
    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderItemViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_slider_item, parent, false)
        return SliderItemViewHolder(rootView)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SliderItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        this.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val tg = ToneGenerator(AudioManager.ADJUST_LOWER, 50)
                tg.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, 50)
                tg.release()
            }
        })
    }

    interface Callback {
        fun onItemClicked(view: View)
    }

    inner class SliderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)

        fun bind(text: String) {
            binding?.setVariable(BR.holder, this)
            binding?.setVariable(BR.text, text)
            binding?.executePendingBindings()
        }

        fun onItemClick(v: View) {
            recyclerView?.let {
                it.smoothScrollToPosition(
                    it.getChildLayoutPosition(v)
                )
            }
            callback?.onItemClicked(v)
        }
    }
}
package com.example.pantanima.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.BR
import com.example.pantanima.R
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Build
import android.os.Vibrator
import androidx.recyclerview.widget.LinearSnapHelper


class SliderAdapter(val data: ArrayList<String>) :
    RecyclerView.Adapter<SliderAdapter.SliderItemViewHolder>() {

    private var clickPlayer: MediaPlayer? = null
    var callback: Callback? = null
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        AppLinearSnapHelper.attachSnapHelperWithListener(recyclerView, LinearSnapHelper(),
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL, object: OnSnapPositionChangeListener{
            override fun onSnapPositionChange(position: Int) {
                playScrollSound(recyclerView.context)
            }
        })
    }

    private fun playScrollSound(context: Context) {
        val resID = context.resources?.getIdentifier(
            "scroll_sound_effect",
            "raw", context.packageName
        )
        vibrate(context)
        resID?.let {
            clickPlayer = MediaPlayer.create(context, it)
            clickPlayer?.start()
        }
    }

    @Suppress("DEPRECATION")
    private fun vibrate(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= 26) {
            val effect: VibrationEffect = VibrationEffect.createOneShot(2, VibrationEffect.DEFAULT_AMPLITUDE)
            v?.vibrate(effect)
        } else {
            v?.vibrate(2)
        }
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
            recyclerView?.smoothScrollToPosition(adapterPosition)
            callback?.onItemClicked(v)
        }
    }
}
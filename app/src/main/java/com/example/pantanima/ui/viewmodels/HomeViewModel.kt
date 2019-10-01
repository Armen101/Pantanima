package com.example.pantanima.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.ui.GroupManager
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.adapters.WordsAdapter
import com.example.pantanima.ui.adapters.WordsAdapterListener
import com.example.pantanima.ui.database.entity.Noun
import com.example.pantanima.ui.database.repository.NounRepo
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.media.MediaPlayer
import android.media.AudioManager
import android.content.Context.AUDIO_SERVICE
import com.example.pantanima.ui.GamePrefs

class HomeViewModel(activity: WeakReference<NavActivity>) : BaseViewModel(activity),
    WordsAdapterListener {

    private var mediaPlayer: MediaPlayer? = null

    @Inject
    lateinit var groupManager: GroupManager

    private var currentWords: List<Noun>? = null
    var maxProgress = ObservableInt(GamePrefs.ROUND_TIME)
    var currentProgress = ObservableInt(0)
    var countDownTimerText =
        ObservableField<String>((GamePrefs.ROUND_TIME - currentProgress.get()).toString())
    var startButtonVisibility = ObservableBoolean(true)

    private val adapter = WordsAdapter(this)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)

    init {
        getApp().getComponent().injectHomeViewModel(this)
        updateAdapterData()
    }

    private fun allItemsIsInactive(): Boolean {
        for (noun in currentWords!!) {
            if (noun.isActive.get()) {
                return false
            }
        }
        return true
    }

    private fun updateAdapterData() {
        disposable.add(NounRepo.getNouns(GamePrefs.ASSORTMENT_WORDS_COUNT)
            .map { it.shuffled() }
            .map { it.drop( GamePrefs.ASSORTMENT_WORDS_COUNT - GamePrefs.WORDS_COUNT) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                currentWords = list
                adapter.setData(list)
                adapter.notifyDataSetChanged()
                compositeJob.add(GlobalScope.launch(Dispatchers.IO) {
                    NounRepo.updateLastUsedTime(list)
                })
            }
        )
    }

    private fun playTimerSound() {
        if(mediaPlayer != null && mediaPlayer!!.isPlaying){
            return
        }
        val resID = activity.get()?.resources?.getIdentifier(
            "tikc_tock", "raw",
            activity.get()?.packageName
        )
        mediaPlayer = MediaPlayer.create(activity.get(), resID!!)
        mediaPlayer?.setOnCompletionListener {
            playTimerSound()
        }
        mediaPlayer?.start()

        val audioManager = activity.get()?.getSystemService(AUDIO_SERVICE) as AudioManager?
        audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, 12, 0)
    }

    fun startGame() {
        startButtonVisibility.set(false)
        val disposable = Flowable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {

                if (maxProgress.get() - currentProgress.get() == 10 ) {
                    playTimerSound()
                }

                currentProgress.set(it.toInt())
                countDownTimerText.set((maxProgress.get() - currentProgress.get()).toString())
                if (it.toInt() == maxProgress.get()) {
                    switchGroup()
                    disposable.dispose()
                }
            }
        this.disposable.add(disposable)
    }

    private fun switchGroup() {
        mediaPlayer?.stop()
        currentProgress.set(0)
        startButtonVisibility.set(true)
//        groupManager.switchGroup()
    }

    override fun onItemClick(item: Noun) {
        val oldIsActiveValue = item.isActive.get()
        item.isActive.set(!oldIsActiveValue)
        if (oldIsActiveValue) {
//            groupManager.decAnsweredCount()
        } else {
//            groupManager.incAnsweredCount()
        }
        if (allItemsIsInactive()) {
            updateAdapterData()
        }
    }

}
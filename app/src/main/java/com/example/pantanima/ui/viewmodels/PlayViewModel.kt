package com.example.pantanima.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
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
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.models.Group
import java.lang.StringBuilder

class PlayViewModel(activity: WeakReference<NavActivity>, groupNames: ArrayList<String>) : BaseViewModel(activity),
    WordsAdapterListener {

    private var mediaPlayer: MediaPlayer? = null

    @Inject
    lateinit var groupManager: GroupManager

    private var currentWords: List<Noun>? = null
    var countDownTimerText = ObservableField<String>((GamePrefs.ROUND_TIME).toString())
    var startButtonVisibility = ObservableBoolean(true)
    var history  = ObservableField("")

    private val adapter = WordsAdapter(this)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)

    init {
        getApp().getComponent().injectHomeViewModel(this)
        initGroups(groupNames)
    }

    private fun initGroups(names: ArrayList<String>) {
        for (name in names){
            groupManager.groups.add(Group(name))
        }
        groupManager.setGroup()
    }

    private fun allItemsIsInactive(): Boolean {
        for (noun in currentWords!!) {
            if (noun.isActive.get()) {
                return false
            }
        }
        return true
    }

    private fun updateAdapterData(code: () -> Unit) {
        disposable.add(NounRepo.getNouns()
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
                code()
            }
        )
    }

    private fun playTimerSound() {
        if(mediaPlayer != null && mediaPlayer!!.isPlaying){
            return
        }
        val resID = resources?.getIdentifier("tikc_tock", "raw",
            activity?.packageName
        )
        mediaPlayer = MediaPlayer.create(activity, resID!!)
        mediaPlayer?.setOnCompletionListener {
            playTimerSound()
        }
        mediaPlayer?.start()

        val audioManager = activity?.getSystemService(AUDIO_SERVICE) as AudioManager?
        audioManager!!.setStreamVolume(AudioManager.STREAM_MUSIC, 12, 0)
    }

    fun startRound() {
        startButtonVisibility.set(false)
        updateAdapterData {
            val disposable = Flowable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map { s -> GamePrefs.ROUND_TIME - s }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    countDownTimerText.set(it.toString())
                    when (it) {
                        GamePrefs.SOUND_TIME -> playTimerSound()
                        0L -> {
                            finishRound()
                            disposable.clear()
                        }
                    }
                }
            this.disposable.add(disposable)
        }
    }

    private fun finishRound() {
        mediaPlayer?.stop()
        groupManager.switchGroup()
        showHistory()
        startButtonVisibility.set(true)
    }

    private fun showHistory() {
        val strBuilder = StringBuilder()
        for (group in groupManager.groups){
            strBuilder.append(group.name)
            var total = 0
            for (round in group.statistics){
                total += round
            }
            strBuilder.append(":\t").append(total)
            strBuilder.append("\n")
        }
        history.set(strBuilder.toString())
    }

    override fun onItemClick(item: Noun) {
        val oldIsActiveValue = item.isActive.get()
        item.isActive.set(!oldIsActiveValue)
        if (oldIsActiveValue) {
            groupManager.incAnsweredCount()
        } else {
            groupManager.decAnsweredCount()
        }
        if (allItemsIsInactive()) {
            updateAdapterData({})
        }
    }

}
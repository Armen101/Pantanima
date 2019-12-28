package com.example.pantanima.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.ui.GroupManager
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.adapters.WordsAdapter
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
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.listeners.AdapterOnItemClickListener
import com.example.pantanima.ui.models.Group
import java.lang.StringBuilder

class PlayVM(activity: WeakReference<NavActivity>, groupNames: ArrayList<String>) : BaseVM(activity),
    AdapterOnItemClickListener<Noun> {

    private var clickPlayer: MediaPlayer? = null
    private var tickTockPlayer: MediaPlayer? = null

    @Inject
    lateinit var groupManager: GroupManager

    private var currentWords: List<Noun>? = null
    var countDownTimerText = ObservableField<String>((GamePrefs.ROUND_TIME).toString())
    var roundStarted = ObservableBoolean(false)
    var history  = ObservableField("")

    private val adapter = WordsAdapter(this)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)

    init {
        getApp().getComponent().injectHomeViewModel(this)
        initGroups(groupNames)
    }

    override fun onItemClick(item: Noun) {
        val oldIsActiveValue = item.isActive.get()
        item.isActive.set(!oldIsActiveValue)
        if (oldIsActiveValue) {
            groupManager.incAnsweredCount()
        } else {
            groupManager.decAnsweredCount()
        }
        playClickSound(oldIsActiveValue)
        if (allItemsIsInactive()) {
            updateAdapterData {}
        }
    }

    fun startRound() {
        roundStarted.set(true)
        updateAdapterData {
            val disposable = Flowable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map { s -> GamePrefs.ROUND_TIME - s }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    countDownTimerText.set(it.toString())
                    when (it) {
                        GamePrefs.SOUND_TIME -> playTickTockSound()
                        0L -> {
                            finishRound()
                            disposable.clear()
                        }
                    }
                }
            this.disposable.add(disposable)
        }
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

    private fun playTickTockSound() {
        if(tickTockPlayer != null && tickTockPlayer!!.isPlaying){
            return
        }
        val resID = resources?.getIdentifier("tikc_tock", "raw",
            activity?.packageName
        )
        tickTockPlayer = MediaPlayer.create(activity, resID!!)
        tickTockPlayer?.setOnCompletionListener {
            playTickTockSound()
        }
        tickTockPlayer?.start()
    }

    private fun playClickSound(active: Boolean) {
        clickPlayer?.stop()

        val resName = if(active) {
            "button_click_on"
        } else {
            "button_click_off"
        }

        val resID = resources?.getIdentifier(resName, "raw", activity?.packageName)
        clickPlayer = MediaPlayer.create(activity, resID!!)
        clickPlayer?.start()
    }

    private fun finishRound() {
        tickTockPlayer?.stop()
        tickTockPlayer?.reset()

        groupManager.switchGroup()
        showHistory()
        roundStarted.set(false)
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

}
package com.example.pantanima.ui.viewmodels

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.ui.GroupManager
import com.example.pantanima.ui.adapters.WordsAdapter
import com.example.pantanima.ui.database.entity.Noun
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import android.media.MediaPlayer
import androidx.lifecycle.viewModelScope
import com.example.pantanima.ui.database.repository.NounRepo
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.listeners.AdapterOnItemClickListener
import java.lang.StringBuilder

class PlayVM(
    private val groupManager: GroupManager,
    private val nounRepo: NounRepo
) : BaseVM(), AdapterOnItemClickListener<Noun> {

    private var clickPlayer: MediaPlayer? = null
    private var tickTockPlayer: MediaPlayer? = null

    private var currentWords: List<Noun>? = null
    var countDownTimerText = ObservableField<String>((GamePrefs.ROUND_TIME).toString())
    var roundStarted = ObservableBoolean(false)
    var history = ObservableField("")

    private val adapter = WordsAdapter(this)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)

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

    private fun allItemsIsInactive(): Boolean {
        currentWords?.forEach {
            if (it.isActive.get()) {
                return false
            }
        }
        return true
    }

    private fun updateAdapterData(code: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val nouns = nounRepo.getNouns().subList(0, GamePrefs.WORDS_COUNT)
            nounRepo.updateLastUsedTime(nouns)
            viewModelScope.launch(Dispatchers.Main) {
                currentWords = nouns
                adapter.setData(nouns)
                adapter.notifyDataSetChanged()
                code()
            }
        }
    }

    private fun playTickTockSound() {
        if (tickTockPlayer?.isPlaying == true) {
            return
        }
        val resID = resources.value.getIdentifier(
            "tikc_tock", "raw",
            getApplication<Application>().packageName
        )
        tickTockPlayer = MediaPlayer.create(getApplication(), resID)
        tickTockPlayer?.setOnCompletionListener {
            playTickTockSound()
        }
        tickTockPlayer?.start()
    }

    private fun playClickSound(active: Boolean) {
        clickPlayer?.stop()

        val resName = if (active) {
            "button_click_on"
        } else {
            "button_click_off"
        }

        val resID = resources.value.getIdentifier(resName, "raw", packageName.value)
        clickPlayer = MediaPlayer.create(getApplication(), resID)
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
        for (group in groupManager.groups) {
            strBuilder.append(group.name)
            var total = 0
            for (round in group.statistics) {
                total += round
            }
            strBuilder.append(":\t").append(total)
            strBuilder.append("\n")
        }
        history.set(strBuilder.toString())
    }

}
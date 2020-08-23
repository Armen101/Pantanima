package com.example.pantanima.ui.viewmodels

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.ui.GroupManager
import com.example.pantanima.ui.adapters.WordsAdapter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import com.example.pantanima.R
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.database.entity.Word
import com.example.pantanima.ui.database.repository.WordRepo
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.listeners.AdapterOnItemClickListener
import java.lang.StringBuilder
import java.util.ArrayList

class PlayVM(
    app: Application,
    private val groupManager: GroupManager,
    private val wordRepo: WordRepo
) : BaseVM(app), AdapterOnItemClickListener<Word> {

    private var clickPlayer: MediaPlayer? = null
    private var tickTockPlayer: MediaPlayer? = null

    private var currentWords: List<Word>? = null
    var countDownTimerText = ObservableField((GamePrefs.ROUND_TIME).toString())
    var roundStarted = ObservableBoolean(false)
    var history = ObservableField("")

    private val adapter = WordsAdapter(this)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)

    override fun onItemClick(item: Word) {
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
        history.set("")
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
            val words = wordRepo.getWords().subList(0, GamePrefs.WORDS_COUNT)
            wordRepo.updateLastUsedTime(words)
            viewModelScope.launch(Dispatchers.Main) {
                currentWords = words
                adapter.setData(words)
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
        roundStarted.set(false)
        tickTockPlayer?.stop()
        tickTockPlayer?.reset()

        groupManager.switchGroup()
        if (isFinished()) {
            goToWin()
            groupManager.resetState()
        } else {
            showHistory()
        }
    }

    private fun isFinished() = isEqualityRounds() && isReachedGolPoints()

    private fun isEqualityRounds(): Boolean {
        for (i in 1 until groupManager.groups.size) {
            val current = groupManager.groups[i].statistics.size
            val prev = groupManager.groups[i - 1].statistics.size
            if (current != prev) {
                return false
            }
        }
        return true
    }

    private fun isReachedGolPoints(): Boolean {
        for (group in groupManager.groups) {
            if (group.getAnsweredCount() >= GamePrefs.GOL_POINTS) {
                return true
            }
        }
        return false
    }

    private fun showHistory() {
        val strBuilder = StringBuilder()
        for (group in groupManager.groups) {
            strBuilder.append(group.name)
            strBuilder.append(":\t").append(group.getAnsweredCount())
            strBuilder.append("\n")
        }
        history.set(strBuilder.toString())
    }

    private fun goToWin() {
        val bundle = Bundle()
        val copyList = groupManager.groups.toList()
        val param = copyList as ArrayList<out Parcelable>
        bundle.putParcelableArrayList(Constants.BUNDLE_GROUPS, param)
        setNewDestination(R.id.navigateToWin, bundle)
    }

}
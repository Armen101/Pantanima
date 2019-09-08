package com.example.pantanima.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.ui.Constants
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

class HomeViewModel(activity: WeakReference<NavActivity>) : BaseViewModel(activity),
    WordsAdapterListener {

    private var currentWords: List<Noun>? = null
    var maxProgress = ObservableInt(40)
    var currentProgress = ObservableInt(0)
    var countDownTimerText = ObservableField<String>((maxProgress.get() - currentProgress.get()).toString())
    var startButtonVisibility = ObservableBoolean(true)

    private val adapter = WordsAdapter(this)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)

    init {
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
        disposable.add(NounRepo.getNouns(Constants.LANGUAGE_AM, 30)
            .map { it.shuffled() }
            .map { it.dropLast(23) }
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

    fun startGame() {
        startButtonVisibility.set(false)
        val disposable = Flowable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                currentProgress.set(it.toInt())
                countDownTimerText.set((maxProgress.get() - currentProgress.get()).toString())
                if (it.toInt() == maxProgress.get()) {
                    switchGroup()
                    disposable.dispose()
                }
            }
        this.disposable.add(disposable)
    }

    private fun switchGroup(){
        currentProgress.set(0)
        startButtonVisibility.set(true)
        GroupManager.switchGroup()
    }

    override fun onItemClick(item: Noun) {
        val oldIsActiveValue = item.isActive.get()
        item.isActive.set(!oldIsActiveValue)
        if (oldIsActiveValue) {
            GroupManager.decAnsweredCount()
        } else {
            GroupManager.incAnsweredCount()
        }
        if (allItemsIsInactive()) {
            updateAdapterData()
        }
    }

}
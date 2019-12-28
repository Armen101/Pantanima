package com.example.pantanima.ui.viewmodels

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.R
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.adapters.GroupsAdapter
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.listeners.AdapterOnItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.ArrayList
import kotlin.random.Random

class GroupsVM(activity: WeakReference<NavActivity>) : BaseVM(activity),
    AdapterOnItemClickListener<String> {

    private var groups: MutableList<String> = arrayListOf()
    private val adapter = GroupsAdapter(this, groups)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)

    init {
        updateAdapterData {

        }
    }

    private fun updateAdapterData(code: () -> Unit) {
        disposable.add(GroupRepo.getGroups()
            .map { it.shuffled() }
            .map { it.drop(GamePrefs.ASSORTMENT_GROUPS_COUNT - GamePrefs.INITIAL_GROUPS_COUNT) }
            .doOnSuccess { GroupRepo.updateLastUsedTime(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                groups.addAll(GroupRepo.getNamesOfGroups(list))
                adapter.notifyDataSetChanged()
                code()
            }
        )
    }

    fun onStartClick() {
        val bundle = Bundle()
        bundle.putStringArrayList(Constants.BUNDLE_GROUPS, groups as ArrayList<String>)
        setNewDestination(R.id.navigateToPlay, bundle)
    }

    fun goToSettings() {
        setNewDestination(R.id.navigateToSettings)
    }

    override fun onItemClick(item: String) {
        disposable.add(GroupRepo.getGroups(groups)
            .map { it.shuffled() }
            .map { it[Random.nextInt(it.size)] }
            .doOnSuccess { GroupRepo.updateLastUsedTime(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { group ->
                val index = groups.indexOf(item)
                groups.removeAt(index)
                groups.add(index, group.value)
                adapter.notifyDataSetChanged()
            }
        )
    }
}
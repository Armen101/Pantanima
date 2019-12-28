package com.example.pantanima.ui.viewmodels

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.R
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.adapters.GroupsAdapter
import com.example.pantanima.ui.database.entity.Group
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.listeners.AdapterOnItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference

class GroupsVM(activity: WeakReference<NavActivity>) : BaseVM(activity),
    AdapterOnItemClickListener<Group> {

    private var groups: MutableList<Group> = arrayListOf()
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                groups.addAll(list)
                adapter.notifyDataSetChanged()
                compositeJob.add(GlobalScope.launch(Dispatchers.IO) {
                    GroupRepo.updateLastUsedTime(list)
                })
                code()
            }
        )
    }

    fun onStartClick() {
        val bundle = Bundle()
        bundle.putStringArrayList(Constants.BUNDLE_GROUPS, GroupRepo.getNamesOfGroups(groups))
        setNewDestination(R.id.navigateToPlay, bundle)
    }

    fun goToSettings() {
        setNewDestination(R.id.navigateToSettings)
    }

    override fun onItemClick(item: Group) {
        Timber.tag("onItemClick")
    }
}
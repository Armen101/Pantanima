package com.example.pantanima.ui.viewmodels

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pantanima.R
import com.example.pantanima.ui.Constants
import com.example.pantanima.ui.adapters.GroupsAdapter
import com.example.pantanima.ui.database.repository.GroupRepo
import com.example.pantanima.ui.helpers.GamePrefs
import com.example.pantanima.ui.listeners.AdapterOnItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GroupsVM(private val repo : GroupRepo) : BaseVM(), AdapterOnItemClickListener<String> {

    private var groupNames: MutableList<String> = arrayListOf()
    private val adapter = GroupsAdapter(this, groupNames)
    val adapterObservable = ObservableField<RecyclerView.Adapter<*>>(adapter)
    var addGroupIsAvailable = ObservableBoolean(true)

    init {
        updateAdapterData()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        adapter.notifyDataSetChanged(closeAllSwipedItems = true)
    }

    private fun updateAdapterData() {
        viewModelScope.launch(Dispatchers.IO) {
            val groups = repo.getGroups()
                .shuffled()
                .subList(0, GamePrefs.INITIAL_GROUPS_COUNT)

            repo.updateLastUsedTime(groups)
            groupNames.addAll(repo.getNamesOfGroups(groups))

            viewModelScope.launch(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun onStartClick() {
        val bundle = Bundle()
        bundle.putStringArrayList(Constants.BUNDLE_GROUPS, groupNames as ArrayList<String>)
        setNewDestination(R.id.navigateToPlay, bundle)
    }

    fun goToSettings() {
        setNewDestination(R.id.navigateToSettings)
    }

    fun addGroup() {
        viewModelScope.launch {
            val groupName = getNewGroup()
            groupNames.add(groupName)
            adapter.notifyDataSetChanged()
            if (groupNames.size >= Constants.PREF_MAX_GROUPS) {
                addGroupIsAvailable.set(false)
            }
        }
    }

    private suspend fun getNewGroup(): String {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            val group = repo.getGroups(groupNames).shuffled().random()
            repo.updateLastUsedTime(group)
            group.value
        }
    }

    override fun onItemClick(item: String) {
        viewModelScope.launch {
            val groupName = getNewGroup()
            val index = groupNames.indexOf(item)
            groupNames.removeAt(index)
            groupNames.add(index, groupName)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDeleteClick(item: String) {
        super.onDeleteClick(item)
        groupNames.remove(item)
        adapter.notifyDataSetChanged()
        addGroupIsAvailable.set(true)
    }
}
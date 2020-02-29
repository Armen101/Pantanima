package com.example.pantanima.ui.viewmodels

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.LiveData
import com.example.pantanima.ui.Event
import androidx.lifecycle.MutableLiveData
import com.example.pantanima.ui.PantanimaApplication

abstract class BaseVM : AndroidViewModel(PantanimaApplication.instance) {

    protected var disposable: CompositeDisposable = CompositeDisposable()

    val resources = lazy { getApplication<Application>().resources }
    val packageName = lazy { getApplication<Application>().packageName }

    private val newDestination = MutableLiveData<Event<Pair<Int, Bundle?>>>()

    fun setNewDestination(destinationId: Int, bundle: Bundle? = null) {
        newDestination.value = Event(Pair(destinationId, bundle))
    }

    fun getNewDestination(): LiveData<Event<Pair<Int, Bundle?>>> {
        return newDestination
    }

    fun getString(@StringRes resource: Int): String? = resources.value.getString(resource)

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
}
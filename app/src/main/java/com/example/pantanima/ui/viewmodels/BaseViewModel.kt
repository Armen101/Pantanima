package com.example.pantanima.ui.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.LiveData
import com.example.pantanima.ui.Event
import androidx.lifecycle.MutableLiveData
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

open class BaseViewModel(var activity: WeakReference<NavActivity>) : ViewModel() {

    private var disposable: CompositeDisposable = CompositeDisposable()

    private val newDestination = MutableLiveData<Event<Int>>()

    fun setNewDestination(destinationId: Int) {
        newDestination.value = Event(destinationId)
    }

    fun getNewDestination(): LiveData<Event<Int>> {
        return newDestination
    }

    fun getApplication() = activity.get()?.application

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
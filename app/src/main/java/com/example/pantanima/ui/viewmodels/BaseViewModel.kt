package com.example.pantanima.ui.viewmodels

import android.content.Intent
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.LiveData
import com.example.pantanima.ui.Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pantanima.ui.activities.NavActivity
import java.lang.ref.WeakReference

abstract class BaseViewModel(var activity: WeakReference<NavActivity>) : ViewModel() {

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

    open fun onCreate() {}

    open fun onStart() {}

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {}

    open fun onDestroy() {}

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

}
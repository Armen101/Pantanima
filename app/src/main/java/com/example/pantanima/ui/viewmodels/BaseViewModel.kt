package com.example.pantanima.ui.viewmodels

import android.content.Intent
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.LiveData
import com.example.pantanima.ui.Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pantanima.ui.PantanimaApplication
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.asynchronous.CompositeJob
import java.lang.ref.WeakReference

abstract class BaseViewModel(var activity: WeakReference<NavActivity>) : ViewModel() {

    protected var disposable: CompositeDisposable = CompositeDisposable()
    protected var compositeJob: CompositeJob = CompositeJob()

    private val newDestination = MutableLiveData<Event<Int>>()

    fun setNewDestination(destinationId: Int) {
        newDestination.value = Event(destinationId)
    }

    fun getNewDestination(): LiveData<Event<Int>> {
        return newDestination
    }

    fun getApp() = activity.get()?.application as PantanimaApplication

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        compositeJob.cancel()
    }

    open fun onCreate() {}

    open fun onStart() {}

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {}

    open fun onDestroy() {}

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

}
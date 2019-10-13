package com.example.pantanima.ui.viewmodels

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.LiveData
import com.example.pantanima.ui.Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pantanima.ui.PantanimaApplication
import com.example.pantanima.ui.activities.NavActivity
import com.example.pantanima.ui.asynchronous.CompositeJob
import java.lang.ref.WeakReference

abstract class BaseVM(activityRef: WeakReference<NavActivity>) : ViewModel() {

    protected var disposable: CompositeDisposable = CompositeDisposable()
    protected var compositeJob: CompositeJob = CompositeJob()
    val resources = activityRef.get()?.resources
    val activity = activityRef.get()

    private val newDestination = MutableLiveData<Event<Pair<Int, Bundle?>>>()

    fun setNewDestination(destinationId: Int, bundle: Bundle? = null) {
        newDestination.value = Event(Pair(destinationId, bundle))
    }

    fun getNewDestination(): LiveData<Event<Pair<Int, Bundle?>>> {
        return newDestination
    }

    fun getApp() = activity?.application as PantanimaApplication

    fun getString(@StringRes resource: Int): String? = activity?.getString(resource)

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
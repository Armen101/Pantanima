package com.example.pantanima.ui.asynchronous

import kotlinx.coroutines.Job

class CompositeJob {

    private val map = hashMapOf<String, Job>()

    fun add(job: Job, key: String = job.hashCode().toString()) = map.put(key, job)?.cancel()

    fun cancel() {
        val iterator = map.iterator()
        while (iterator.hasNext()) {
            iterator.next().value.cancel()
            iterator.remove()
        }
    }

}
package com.example.pantanima.ui.asynchronous

import kotlinx.coroutines.Job

class CompositeJob {

    private val map = hashMapOf<String, Job>()

    fun add(job: Job, key: String = job.hashCode().toString()) = map.put(key, job)?.cancel()

    fun cancel(job: Job?) = cancel(job?.hashCode()?.toString())

    fun cancel(key: String?) {
        map[key]?.cancel()
        map.remove(key)
    }

    fun cancel() {
        val iterator = map.iterator()
        while (iterator.hasNext()) {
            iterator.next().value.cancel()
            iterator.remove()
        }
    }

}
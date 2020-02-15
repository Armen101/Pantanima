package com.example.pantanima.ui.fragments

import java.util.concurrent.locks.ReentrantLock

class ReentrantLockDemonstration {

    fun demonstrate() {
        val commonResource = CommonResource()
        val locker = ReentrantLock()
        for (i in 1..5) {
            val t = Thread(CountThread(commonResource, locker))
            t.name = "Thread $i"
            t.start()
        }
    }

    internal inner class CommonResource {
        var x = 0
    }

    internal inner class CountThread(var res: CommonResource, var locker: ReentrantLock) :
        Runnable {
        override fun run() {
            locker.lock()
            try {
                res.x = 1
                for (i in 1..4) {
                    System.out.printf("%s %d \n", Thread.currentThread().name, res.x)
                    System.out.printf("holdCount = %d, queueLength = %d \n", locker.holdCount, locker.queueLength)
                    res.x++
                    Thread.sleep(1000)
                }
            } catch (e: InterruptedException) {
                println(e.message)
            } finally {
                locker.unlock()
            }
        }
    }
}
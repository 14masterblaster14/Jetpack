package com.example.coroutines

import kotlinx.coroutines.*

class UserDataManager {
    var count = 0
    lateinit var deferred: Deferred<Int>
    suspend fun getTotalUserCount(): Int {

        coroutineScope {            // Its a suspend function which will create child scope and
            // gives the guarantee to complete all the task of child scope before returning the value

            launch(Dispatchers.IO) {
                delay(1000)
                count = 50
            }

            deferred = async(Dispatchers.IO) {
                delay(3000)
                return@async 70
            }
        }

        return count + deferred.await()
    }
}
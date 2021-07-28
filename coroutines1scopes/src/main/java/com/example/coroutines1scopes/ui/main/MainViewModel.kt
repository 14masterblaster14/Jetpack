package com.example.coroutines1scopes.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.coroutines1scopes.model.UserRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel : ViewModel() {

    /*
     // W/o View Model KTX library

     private val myJob = Job()
     private val myScope = CoroutineScope(Dispatchers.IO + myJob)

     fun getUserData(){
         myScope.launch {
             // write ur code
         }
     }

     override fun onCleared() {
         super.onCleared()
         myJob.cancel()
     }

     */


    // With View Model KTX library i.e. viewModelScope

    private var usersRepository = UserRepository()
    /*
    var users : MutableLiveData<List<User>> = MutableLiveData()

    fun getUserData(){
        viewModelScope.launch {
            var result: List<User>? = null
            withContext(Dispatchers.IO){
                result = usersRepository.getUsers()
            }

            users.value = result
        }
    }
         */

    // Preferred, Efficient Way 2 : With Live KTX library i.e. Live data builder
    var users = liveData(Dispatchers.IO) {

        val result = usersRepository.getUsers()
        emit(result)
    }
}
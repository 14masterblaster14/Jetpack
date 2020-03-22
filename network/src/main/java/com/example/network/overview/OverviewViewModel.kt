package com.example.network.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.network.network.MarsApi
import com.example.network.network.MarsApiFilter
import com.example.network.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class MarsApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<MarsApiStatus>()
    val status : LiveData<MarsApiStatus>
        get() = _status

    private val _nevigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val nevigateToSelectedProperty : LiveData<MarsProperty>
        get() = _nevigateToSelectedProperty

    /*
        private val _response = MutableLiveData<String>()
        val response: LiveData<String>
            get() = _response

        private val _property = MutableLiveData<MarsProperty>()
        val property : LiveData<MarsProperty>
            get() = _property
    */
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties


    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    private fun getMarsRealEstateProperties(filter:MarsApiFilter) {

        coroutineScope.launch {


            var getPropertiesDeferred = MarsApi.retrofitService.getProperties(filter.value)

            try {
                _status.value = MarsApiStatus.LOADING
                var listResult = getPropertiesDeferred.await()
                _status.value = MarsApiStatus.DONE
                _properties.value = listResult
            }catch (e:Exception){
                _status.value = MarsApiStatus.ERROR
                // setting _properties LiveData to an empty list to clear the RecyclerView
                _properties.value = ArrayList()
            }


            /*try {
                var listResult = getPropertiesDeferred.await()

                //Calling await() on the Deferred object returns the result from the network call
                // when the value is ready. The await() method is non-blocking, so the Mars API
                // service retrieves the data from the network without blocking the current
                // thread—which is important because we're in the scope of the UI thread.
                // Once the task is done, your code continues executing from where it left off.
                // This is inside the try {} so that you can catch exceptions.

                _response.value =
                    "Success: ${listResult.size} Mars properties retrieved"

                if (listResult.size > 0) {
                    // _property.value = listResult[0]
                    _properties.value = listResult
                }
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }*/
        }

        /*MarsApi.retrofitService.getProperties().enqueue(
            object : Callback<List<MarsProperty>> {

                override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
                    _response.value = "Failure : " + t.message
                }

                override fun onResponse(
                    call: Call<List<MarsProperty>>,
                    response: Response<List<MarsProperty>>
                ) {
                    _response.value = "Success : ${response.body()?.size} Mars properties retrived"
                }
            }
        )*/
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    fun displayPropertyDetails(marsProperty: MarsProperty){
        _nevigateToSelectedProperty.value = marsProperty
    }
e
    fun displayPropertyDetailsComplete() {
        //  You need this to mark the navigation state to complete, and to avoid the navigation
        //  being triggered again when the user returns from the detail view.
        _nevigateToSelectedProperty.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()       //Loading data should stop when the ViewModel is destroyed
    }
}
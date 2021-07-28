package com.example.navigations22.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.navigations22.extension.Event
import com.example.navigations22.model.Letter

class LettersViewModel {

    val sentLettersLiveData: MediatorLiveData<List<Letter>> = MediatorLiveData()
    val inboxLettersLiveData: MediatorLiveData<List<Letter>> = MediatorLiveData()
    val toastLiveData: MutableLiveData<Event<String>> = MutableLiveData()


    var loading = ObservableField(View.GONE)

    var recipient = ObservableField("")
    var title = ObservableField("")
    var description = ObservableField("")
    var ps = ObservableField("")

    var profileName = ObservableField("")
    var profileEmail = ObservableField("")

    /* private val gson by lazy { Gson() }
     private val notificationHelper by lazy { NotificationHelper(app) }
     private val sharedPreferenceHelper by lazy { SharedPreferenceHelper(app) }
     private val letterRepository by lazy { LetterRepository(app) }*/
}
package com.example.viewmodellivedata

import android.app.Application
import android.os.Debug
import timber.log.Timber

class GuessTheWordApplicatiom : Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}
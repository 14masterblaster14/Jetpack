package com.example.repositoryworkmanager

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.repositoryworkmanager.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Best Practice: The onCreate() method runs in the main thread. Performing a long-running operation
 * in onCreate() might block the UI thread and cause a delay in loading the app.
 * To avoid this problem, run tasks such as initializing Timber and scheduling WorkManager off the
 * main thread, inside a coroutine.
 *
 */

class DevByteApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {

            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
            setupRecurringWork()
        }
    }

    /**
     * Setup WorkManager background job to 'fetch' new network data daily.
     *
     */

    private fun setupRecurringWork() {

        /**
         * When defining the WorkRequest, you can specify constraints for when the Worker should run.
         * For example, you might want to specify that the work should only run when the device is
         * idle, or only when the device is plugged in and connected to Wi-Fi. You can also specify
         * a backoff policy for retrying work.
         *
         */
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)  //the work request will only run when the device is on an unmetered network.
            .setRequiresBatteryNotLow(true) //the work request should run only if the battery is not low.
            .setRequiresCharging(true)      //it runs only when the device is charging.
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true) // it runs only when the device is idle.
                }
            }
            .build()

        //  repeat interval of 1 with a time unit of TimeUnit.DAYS.
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        //  repeat interval of 1 with a time unit of TimeUnit.DAYS.
        //val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(15, TimeUnit.MINUTES)
        //    .setConstraints(constraints)
        //    .build()

        Timber.d("Periodic Work request for sync is scheduled")

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
            //  If pending (uncompleted) work exists with the same name, the ExistingPeriodicWorkPolicy.
            //  KEEP parameter makes the WorkManager keep the previous periodic work and discard the new work request.
        )
    }
}
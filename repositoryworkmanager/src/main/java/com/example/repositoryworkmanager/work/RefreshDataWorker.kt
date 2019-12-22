package com.example.repositoryworkmanager.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.repositoryworkmanager.database.getDatabase
import com.example.repositoryworkmanager.repository.VideosRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext : Context, params: WorkerParameters) : CoroutineWorker(appContext,params) {

    /**
     *
     * A suspending function is a function that can be paused and resumed later.
     *  A suspending function can execute a long running operation and wait for it to complete
     *  without blocking the main thread.
     *
     *  The doWork() method inside the Worker class is called on a background thread.
     *  The method performs work synchronously, and should return a "ListenableWorker.Result" object.
     *  The Android system gives a Worker a maximum of 10 minutes to finish its execution and
     *  return a ListenableWorker.Result object. After this time has expired,
     *  the system forcefully stops the Worker.
     *
     *      Result.success()—work completed successfully.
     *      Result.failure()—work completed with a permanent failure.
     *      Result.retry()—work encountered a transient failure and should be retried.
     *
     *   A Worker defines a unit of work and the WorkRequest defines how and when work should be run.
     *   There are two concrete implementations of the WorkRequest class:
     *
     *   The OneTimeWorkRequest class is for one-off tasks. (A one-off task happens only once.)
     *
     *   The PeriodicWorkRequest class is for periodic work, work that repeats at intervals.
     *   Note:  The minimum interval for periodic work is 15 minutes.
     *          Periodic work can't have an initial delay as one of its constraints.
     *
     * */

    companion object {
        // Define a work name to uniquely identify this worker.
        const val WORK_NAME = "com.example.repositoryworkmanager.work.RefreshDataWorker"
    }

    override suspend fun doWork(): Result {

        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)

        try {
            repository.refreshVideos( )
            Timber.d("Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }
}
package com.example.repositoryworkmanager.work

/**
 *                  *-----  WorkManager ----*
WorkManager is for background work that's deferrable and requires guaranteed execution:

->  Deferrable means that the work is not required to run immediately.
    For example, sending analytical data to the server or syncing the database in the background
    is work that can be deferred.
->  Guaranteed execution means that the task will run even if the app exits or the device restarts.


While WorkManager runs background work, it takes care of compatibility issues and best practices
for battery and system health. WorkManager offers compatibility back to API level 14.
WorkManager chooses an appropriate way to schedule a background task, depending on the device
API level. It might use JobScheduler (on API 23 and higher) or
a combination of AlarmManager and BroadcastReceiver.

 WorkFlow:->
                                Determine the Background Schedular
                                ==================================
                                                ||
                                                ||
                                                ||
                                       Yes      ||        No
                                    ========= API 23+ ==========
                                    ||                         ||
                                    ||                         ||
                                    ||                         ||
                                    ||                         ||
                                JobSchedular            AlarmManager and
                                                        BroadcastReceiver


Note:

->  WorkManager is not intended for in-process background work that can be terminated safely
    if the app process is killed.
->  WorkManager is not intended for tasks that require immediate execution.

WorkManager library:

#   Worker :-
            This class is where you define the actual work (the task) to run in the background.
            You extend this class and override the doWork() method. The doWork() method is where
            you put code to be performed in the background, such as syncing data with the server
            or processing images. You implement the Worker in this task.

#   WorkRequest :-
            This class represents a request to run the worker in background. Use WorkRequest to
            configure how and when to run the worker task, with the help of Constraints such as
            device plugged in or Wi-Fi connected. You implement the WorkRequest in a later task.

#   WorkManager :-
            This class schedules and runs your WorkRequest. WorkManager schedules work requests
            in a way that spreads out the load on system resources, while honoring the constraints
            that you specify. You implement the WorkManager in a later task.

In this codelab, you schedule a task to pre-fetch the DevBytes video playlist from the network
once a day. To schedule this task, you use the WorkManager library.

 */

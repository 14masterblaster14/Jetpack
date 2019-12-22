package com.example.repositoryworkmanager.database


/**
                        *-----  Concept: Caching    ----*

After an app fetches data from the network, the app can cache the data by storing the data
in a device's storage.You cache data so that you can access it later when the device is offline,
or if you want to access the same data again.

The following table shows several ways to implement network caching in Android.

Caching technique :
                    1)  Retrofit :
                            It is a networking library used to implement a type-safe REST client
                            for Android.You can configure Retrofit to store a copy of every
                            network result locally.

                        Uses :
                            Good solution for simple requests and responses, infrequent network calls,
                            or small datasets.

                    2) SharedPreferences :
                            You can use SharedPreferences to store key-value pairs.

                        Uses :
                            Good solution for a small number of keys and simple values.
                            You can't use this technique to store large amounts of structured data.

                    3) Internal storage :
                            You can access the app's internal storage directory and save data files
                            in it.Your app's package name specifies the app's internal storage
                            directory, which is in a special location in the Android file system.
                            This directory is private to your app, and it is cleared when your app
                            is uninstalled.
                        Uses :
                            Good solution if you have specific needs that a file system can solve—
                            for example, if you need to save media files or data files and you have
                            to manage the files yourself. You can't use this technique to store
                            complex and structured data.

                    4) Room :
                            You can cache data using Room, which is an SQLite object-mapping library
                            that provides an abstraction layer over SQLite.


                        Uses :
                            Recommended solution for complex and structured data, because the best
                            way to store structured data on a device's file system is in a
                            local SQLite database.

In this codelab, you use Room, because it's the recommended way to store structured data on a device file system.

        **/
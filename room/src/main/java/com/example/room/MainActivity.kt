package com.example.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// Refer SleepDatabaseTest.kt in androidTest
// For more coroutines :
//  https://codelabs.developers.google.com/codelabs/kotlin-android-training-coroutines-and-room/index.html?index=..%2F..android-kotlin-fundamentals#8
// Study Util.kt file


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

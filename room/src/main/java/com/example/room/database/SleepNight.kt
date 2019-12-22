package com.example.room.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//An entity class defines a table, and each instance of that class represents a row in the table.
// Each property defines a column.

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
                        @PrimaryKey(autoGenerate = true)
                        var nightId: Long = 0L,

                        @ColumnInfo(name = "start_time_milli")
                        val startTimeMilli: Long = System.currentTimeMillis(),

                        @ColumnInfo(name = "end_time_milli")
                        var endTimeMilli: Long = startTimeMilli,

                        @ColumnInfo(name = "quality_rating")
                        var sleepQuality: Int = -1  )
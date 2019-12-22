package com.example.viewmodellivedata.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber

/*
Next Hint Assignment
https://codelabs.developers.google.com/codelabs/kotlin-android-training-live-data-transformations/index.html?index=..%2F..android-kotlin-fundamentals#6

 */

class GameViewModel : ViewModel() {

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> //  backing property
        get() = _currentTime

    // The String version of the current time
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
        // DateUtils.formatElapsedTime()  utility method, which takes a long number of milliseconds
        // and formats it to "MM:SS" string format.
    }

    private val timer : CountDownTimer

    // The current word
    //var word = ""
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>  //  backing property
        get() = _word

    // The current score
    //var score = 0
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Event which triggers the end of the game
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    // The list of words - the front of the list is the next word to guess
    lateinit var wordList: MutableList<String>

    companion object {

        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L

    }

    init {

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){    //Pass in the total time, COUNTDOWN_TIME. For the time interval, use ONE_SECOND
            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }

            //The millisUntilFinished is the amount of time until the timer is finished in milliseconds.
            // Convert millisUntilFinished to seconds and assign it to _currentTime.
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND

            }

        }

        timer.start()

        Timber.i("GameViewModel created...")

        _word.value = ""
        _score.value = 0

        resetList()
        nextWord()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }


    /** Methods for updating the UI **/
    /** Methods for buttons presses **/
    fun onSkip() {
        if (!wordList.isEmpty()) {
            //score--
            _score.value = (score.value)?.minus(1)
        }
        nextWord()
    }

    fun onCorrect() {
        if (!wordList.isEmpty()) {
            //score++
            _score.value = (score.value)?.plus(1)
        }
        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        // Shuffle the word list, if the list is empty
        if (wordList.isEmpty()) {
            //onGameFinish()
            resetList()

        } else {
            //Select and remove a word from the list
            // word = wordList.removeAt(0)
            _word.value = wordList.removeAt(0)
        }
    }

    /** Method for the game completed event **/
    fun onGameFinish() {
        _eventGameFinished.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed!...")
        // Cancel the timer
        timer.cancel()
    }
}


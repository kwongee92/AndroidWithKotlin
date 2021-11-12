package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.text.method.TransformationMethod
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.time.format.DateTimeFormatter

class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

    // The current word
    var word =  MutableLiveData<String>()

    // The current score
    private var _score = MutableLiveData<Int>()
    //캡슐
    val score : LiveData<Int>
        get()=_score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private var _eventGameFinish = MutableLiveData<Boolean>()

    val eventGameFinish : LiveData<Boolean> get() = _eventGameFinish

    val timer : CountDownTimer

    private var _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long> get() = _currentTime
    val currentTimeString = Transformations.map(currentTime, { time ->
        DateUtils.formatElapsedTime(time)
    })

    init {
        Log.i("GameViewModel", "init called")
        resetList()
        nextWord()
        word.value = ""
        _score.value = 0
        _eventGameFinish.value = false;


        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                Log.i("GameViewModel","timer onTick called - "+millisUntilFinished)
                // TODO implement what should happen each tick of the timer
                _currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                // TODO implement what should happen when the timer finishes
                Log.i("GameViewModel", "timer finish")
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "onCleared called")
        timer.cancel()
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

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        word.value = wordList.removeAt(0)


    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        nextWord()
    }

    fun onEventGameFinishedComplete(){
        _eventGameFinish.value = false
    }


}
package com.example.foreverfreedictionary.ui.dialog

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foreverfreedictionary.ui.baseMVVM.BaseViewModel
import javax.inject.Inject
import kotlin.math.abs

class VoiceRecognizerDialogViewModel @Inject constructor(application: Application) : BaseViewModel(application),
    RecognitionListener {

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    val viewState: LiveData<ViewState> = _viewState
    private var previousRmsdB = 0f
    val isListening
        get() = _viewState.value?.isListening ?: false

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(
        application.applicationContext).apply {
        setRecognitionListener(this@VoiceRecognizerDialogViewModel)
    }
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, application.packageName)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    fun startListening() {
        notifyListening(isRecording = true)
        speechRecognizer.startListening(recognizerIntent)
    }

    fun stopListening() {
        speechRecognizer.stopListening()
        notifyListening(isRecording = false)
    }

    private fun notifyListening(isRecording: Boolean) {
        _viewState.value = ViewState(isListening = isRecording)
    }

    fun checkAudioRecordingPermission(context: Application) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    //  recognizer listeners
    override fun onPartialResults(results: Bundle?) {
        val userSaid = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        _viewState.value = _viewState.value?.copy(spokenText = userSaid?.get(0) ?: "", rmsDbChanged = false)
        Log.d("onPartialResults", userSaid.toString())
    }
    override fun onResults(results: Bundle?){
        val userSaid = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        _viewState.value = _viewState.value?.copy(spokenText = userSaid?.get(0) ?: "", isFinalResult = true, rmsDbChanged = false)
        Log.d("RESULTS_RECOGNITION", userSaid.toString())
    }
    override fun onEndOfSpeech() {
        notifyListening(false)
        Log.d("onEndOfSpeech", "onEndOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        if (rmsdB > 4 && diffRms(newRms = rmsdB, previousRms = previousRmsdB) > 1) {
            previousRmsdB = rmsdB
            _viewState.value = _viewState.value?.copy(rmsDbChanged = true)
        }
    }

    private fun diffRms(newRms: Float, previousRms: Float): Int = abs(previousRms - newRms).toInt()

    override fun onError(errorCode: Int) {
        _viewState.value = _viewState.value?.copy(isListening = false, error = errorCode, rmsDbChanged = false)

    }

    override fun onReadyForSpeech(p0: Bundle?) {}
    override fun onBufferReceived(p0: ByteArray?) {}
    override fun onEvent(p0: Int, p1: Bundle?) {}
    override fun onBeginningOfSpeech() {}

    data class ViewState(
        val spokenText: String = "",
        val isListening: Boolean,
        val error: Int = - 1,
        val rmsDbChanged: Boolean = false,
        /**if true the {@link RecognitionListener#onResults(Bundle results)} called
         * otherwise the {@link RecognitionListener#onPartialResults(Bundle partialResults)} called */
        val isFinalResult: Boolean = false
    )
}
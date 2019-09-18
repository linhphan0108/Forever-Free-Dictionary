package com.example.foreverfreedictionary.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.di.injector
import com.example.foreverfreedictionary.di.viewModel
import kotlinx.android.synthetic.main.dialog_voice_recognizer.*


/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    VoiceRecognizerDialog.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [VoiceRecognizerDialog.Listener].
 */

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
class VoiceRecognizerDialog : BottomSheetDialogFragment() {

    private val viewModel: VoiceRecognizerDialogViewModel by viewModel(this){
        injector.voiceRecognizerDialogViewModel}
    private var mListener: Listener? = null

    companion object {
        fun newInstance(): VoiceRecognizerDialog =
            VoiceRecognizerDialog().apply {
                arguments = Bundle().apply {
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_voice_recognizer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRippleView()
        registerViewModelListeners()
        startListening()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        mListener = if (parent != null) {
            parent as Listener
        } else {
            context as Listener
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startListening()
            }
//            else{
                // users have refused the request
//            }
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }


    //  inner methods
    private fun registerViewModelListeners(){
        viewModel.viewState.observe(this, Observer { viewState ->
            if (viewState.error > -1){
                rippleView.stopAnimation()
                onVoiceRecognitionError(viewState.error)
            }else{
                onVoiceRecognitionResults(viewState)
                if(!viewState.isListening){
                    rippleView.stopAnimation()
                }
            }
        })
    }

    private fun setupRippleView(){
        rippleView.listener = View.OnClickListener {
            startListening()
        }
    }

    /**
     * @return true if the permission has been granted otherwise return false
     */
    private fun checkOrRequestAudioPermission() : Boolean{
        if (activity == null) return false
        return if (!viewModel.checkAudioRecordingPermission(activity!!.application)) {
            ActivityCompat.requestPermissions(activity as Activity, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            false
        }else{
            true
        }
    }

    private fun startListening(){
        if (!checkOrRequestAudioPermission()) return

        if (viewModel.isListening) {
            viewModel.stopListening()
        } else {
            rippleView.startAnimation()
            viewModel.startListening()
        }
    }

    override fun onStop() {
        viewModel.stopListening()
        super.onStop()
    }

    override fun onDismiss(dialog: DialogInterface) {
        rippleView.clear()
        super.onDismiss(dialog)

    }

    private fun onVoiceRecognitionResults(uiOutput: VoiceRecognizerDialogViewModel.ViewState) {
        Log.d("output", uiOutput.spokenText)
        Log.d("output rmsDbChanged", uiOutput.rmsDbChanged.toString())
        Log.d("output isFinalResult", uiOutput.isFinalResult.toString())
        if (uiOutput.spokenText.isEmpty()) {
            txtResult.text = "..."
        }else {
            txtResult.text = uiOutput.spokenText
        }
        if (uiOutput.isFinalResult) {
            mListener?.onVoiceRecognizeResult(uiOutput.spokenText)
            dismiss()
        }
    }

    private fun onVoiceRecognitionError(errorCode: Int){
        Log.d("output error", errorCode.toString())
        val message = when (errorCode){
            SpeechRecognizer.ERROR_AUDIO -> {
                getString(R.string.voice_recognizer_error_msg_audio_error)
            }
            SpeechRecognizer.ERROR_CLIENT -> {
                getString(R.string.voice_recognizer_error_msg_client_error)
            }
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                getString(R.string.voice_recognizer_error_msg_permissions)
            }
            SpeechRecognizer.ERROR_NETWORK -> {
                getString(R.string.voice_recognizer_error_msg_network)
            }
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> {
                getString(R.string.voice_recognizer_error_msg_network_timeout)
            }
            SpeechRecognizer.ERROR_NO_MATCH -> {
                getString(R.string.voice_recognizer_error_msg_no_match)
            }
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                getString(R.string.voice_recognizer_error_msg_recognition_service_busy)
            }
            SpeechRecognizer.ERROR_SERVER -> {
                getString(R.string.voice_recognizer_error_msg_server)
            }
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                getString(R.string.voice_recognizer_error_msg_speech_timeout)
            }
            else -> {
                getString(R.string.voice_recognizer_error_msg)
            }
        }
        txtResult.text = message
    }

    //  inner classes
    interface Listener {
        fun onVoiceRecognizeResult(detectedSpeech: String)
    }
}

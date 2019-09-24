/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.foreverfreedictionary.ui.screen.ocr_text_recognizer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Rect
import android.hardware.Camera
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.extensions.getDimensionPixelSize
import com.example.foreverfreedictionary.extensions.trimFirstSpecialChar
import com.example.foreverfreedictionary.extensions.trimLastSpecialChar
import com.example.foreverfreedictionary.ui.baseMVVM.BaseActivity
import com.example.foreverfreedictionary.ui.screen.ocr_text_recognizer.camera.CameraSource
import com.example.foreverfreedictionary.ui.screen.ocr_text_recognizer.camera.GraphicOverlay
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_ocr_capture.*

import java.io.IOException
import java.util.Locale

import timber.log.Timber

/**
 * Activity for the Ocr Detecting app.  This app detects text and displays the value with the
 * rear facing camera. During detection overlay graphics are drawn to indicate the position,
 * size, and contents of each TextBlock.
 */
class OcrCaptureActivity : BaseActivity() {
    private var cameraSource: CameraSource? = null
    private lateinit var graphicOverlay: GraphicOverlay<OcrGraphic>

    // Helper objects for detecting taps and pinches.
    private var gestureDetector: GestureDetector? = null
    private lateinit var ocrDetectorProcessor: OcrDetectorProcessor

    private var autoFocus = true
    private var useFlash = false

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_ocr_capture)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("UNCHECKED_CAST")
        graphicOverlay = graphicOverlayView as GraphicOverlay<OcrGraphic>

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        val rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash)
        } else {
            requestCameraPermission()
        }

        gestureDetector = GestureDetector(this, CaptureGestureListener())

        setupCameraController()
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private fun requestCameraPermission() {
        Timber.w("Camera permission is not granted. Requesting permission")
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM)
            return
        }

        val thisActivity = this
        Snackbar.make(
            graphicOverlay, R.string.permission_camera_rationale,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.ok) {
                ActivityCompat.requestPermissions(
                    thisActivity, permissions,
                    RC_HANDLE_CAMERA_PERM
                )
            }
            .show()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val c = gestureDetector!!.onTouchEvent(e)

        return c || super.onTouchEvent(e)
    }

    private fun setupCameraController(){
        swDetect.isChecked = true
        swAutoFocus.isChecked = autoFocus
        swFlash.isChecked = useFlash

        swDetect.setOnCheckedChangeListener { _, isChecked ->
            ocrDetectorProcessor.processEnable = isChecked
        }
        swAutoFocus.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                cameraSource?.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)
            }else{
                cameraSource?.cancelAutoFocus()
            }
        }
        swFlash.setOnCheckedChangeListener { _, isChecked ->
            cameraSource?.setFlashMode(if (isChecked) {
                Camera.Parameters.FLASH_MODE_TORCH
            }else{
                Camera.Parameters.FLASH_MODE_OFF
            })
        }
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val context = applicationContext

        // A text recognizer is created to find text.  An associated multi-processor instance
        // is set to receive the text recognition results, track the text, and maintain
        // graphics for each text block on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each text block.
        val textRecognizer = TextRecognizer.Builder(context).build()
        val detectableArea = Rect(0, 0, 0, getDimensionPixelSize(R.dimen.ocr_detection_area_height))
        ocrDetectorProcessor = OcrDetectorProcessor(graphicOverlay, detectableArea)
        @Suppress("UNCHECKED_CAST")
        textRecognizer.setProcessor(ocrDetectorProcessor)
        if (!textRecognizer.isOperational) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Timber.w("Detector dependencies are not yet available.")

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            val lowstorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowstorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show()
                Timber.w(getString(R.string.low_storage_error))
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setRequestedFps(1f)
            .setFlashMode(if (useFlash) Camera.Parameters.FLASH_MODE_TORCH else null)
            .setFocusMode(if (autoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO else null)
            .build()
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        if (preview != null) {
            preview!!.stop()
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (preview != null) {
            preview!!.release()
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode  The request code passed in [.requestPermissions].
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [PackageManager.PERMISSION_GRANTED]
     * or [PackageManager.PERMISSION_DENIED]. Never null.
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Timber.d("Got unexpected permission result: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.d("Camera permission granted - initialize the camera source")
            // we have permission, so create the camerasource
            val autoFocus = intent.getBooleanExtra(AutoFocus, true)
            val useFlash = intent.getBooleanExtra(UseFlash, false)
            createCameraSource(autoFocus, useFlash)
            return
        }

        Timber.e("Permission not granted: results len = ${grantResults.size} Result code = %s", if (grantResults.isNotEmpty()) grantResults[0] else "(empty)")

        val listener = DialogInterface.OnClickListener { dialog, id -> finish() }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Multitracker sample")
            .setMessage(R.string.no_camera_permission)
            .setPositiveButton(R.string.ok, listener)
            .show()
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    @Throws(SecurityException::class)
    private fun startCameraSource() {
        // check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
            applicationContext
        )
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            dlg.show()
        }

        if (cameraSource != null) {
            try {
                preview!!.start(cameraSource!!, graphicOverlay)
            } catch (e: IOException) {
                Timber.e("Unable to start camera source. $e")
                cameraSource!!.release()
                cameraSource = null
            }

        }
    }

    /**
     * onTap is called to speak the tapped TextBlock, if any, out loud.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the tap was on a TextBlock
     */
    private fun onTap(rawX: Float, rawY: Float): Boolean {
        val graphic = graphicOverlay.getGraphicAtLocation(rawX, rawY)
        var text: TextBlock? = null
        if (graphic != null) {
            text = graphic.textBlock
            if (text != null && text.value != null) {
                Timber.d("text data is being spoken! %s", text.value)
                displaySelectedTextBlock(text)
            } else {
                Timber.d("text data is null")
            }
        } else {
            Timber.d("no text detected")
        }
        return text != null
    }

    private inner class CaptureGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return onTap(e.rawX, e.rawY) || super.onSingleTapConfirmed(e)
        }
    }

    private fun displaySelectedTextBlock(textBlock: TextBlock) {
        chipGroupResult.removeAllViews()
        textBlock.components.forEach { line ->
            line.value.split(' ').forEach {word ->
                val trimmedWord = word.trimFirstSpecialChar().trimLastSpecialChar().toLowerCase(Locale.ENGLISH)
                if (trimmedWord.isNotBlank()) {
                    val chip = Chip(this)
                    chip.text = trimmedWord
                    chip.setOnClickListener {
                        onWordSelected(trimmedWord)
                    }
                    chipGroupResult.addView(chip as View)
                }
            }
        }
        txtOcrHint.text = if (chipGroupResult.childCount > 0){
            getString(R.string.ocr_select_word_hint)
        }else{
            getString(R.string.ocr_hint)
        }
    }

    private fun onWordSelected(word: String) {
        setResult(Activity.RESULT_OK, intent.putExtra(SELECTED_WORD, word))
        onBackPressed()
    }

    companion object {
        // Intent request code to handle updating play services if needed.
        private const val RC_HANDLE_GMS = 9001

        // Permission request codes need to be < 256
        private const val RC_HANDLE_CAMERA_PERM = 2

        // Constants used to pass extra data in the intent
        const val AutoFocus = "AutoFocus"
        const val UseFlash = "UseFlash"
        const val SELECTED_WORD = "SELECTED_WORD"
    }
}

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

import android.graphics.Rect

import com.example.foreverfreedictionary.ui.screen.ocr_text_recognizer.camera.GraphicOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock

import timber.log.Timber

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
class OcrDetectorProcessor(
    private val graphicOverlay: GraphicOverlay<OcrGraphic>,
    private val scannableArea: Rect
) :
    Detector.Processor<TextBlock> {
    var processEnable = true

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
        graphicOverlay.clear()
        val items = detections.detectedItems
        for (i in 0 until items.size()) {
            val item = items.valueAt(i)
            if (item != null && item.value != null) {
                if (!processEnable || !isInsideScannableArea(item)) continue
                Timber.d("Text detected! %s", item.value)
                val graphic = OcrGraphic(graphicOverlay, item)
                graphicOverlay.add(graphic)
            }
        }
    }

    private fun isInsideScannableArea(textBlock: TextBlock) : Boolean{
        val box = textBlock.boundingBox
        return box.top > scannableArea.top && box.bottom < scannableArea.bottom
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    override fun release() {
        graphicOverlay.clear()
    }
}

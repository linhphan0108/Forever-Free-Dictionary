package com.example.foreverfreedictionary.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import com.example.foreverfreedictionary.R
import kotlinx.android.synthetic.main.dialog_change_text_size.*

class ChangeTextSizeDialog: DialogFragment() {
    var anchorView: View? = null
        set(value) {
            field = value
            setAnchor(value)
        }

    var listener: OnListener? = null
    var currentSize: Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogAnimation)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onStart() {
        super.onStart()
        // remove black outer overlay, or change opacity
        dialog?.window?.also { window ->
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window.attributes?.also { attributes ->
                attributes.dimAmount = 0f
                window.attributes = attributes
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_change_text_size, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnClickListener { dismiss() }
        setAnchor(anchorView)
        seekBarTextSize.progress = currentSize
        seekBarTextSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener?.onNewTextSize(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun setAnchor(anchor: View?){
        if(anchor == null || bubble == null) return
        bubble.setAnchor(anchor)
    }

    interface OnListener{
        fun onNewTextSize(value: Int)
    }
}
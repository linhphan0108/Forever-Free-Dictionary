package com.example.foreverfreedictionary.ui.customView.voiceRippleView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.foreverfreedictionary.ui.customView.BaseCustomView
import com.example.foreverfreedictionary.util.ScreenUtil
import kotlin.math.min

private val MIN_WIDTH = ScreenUtil.dp2px(16)
private val MIN_HEIGHT = ScreenUtil.dp2px(16)

@SuppressLint("ViewConstructor")
class CircleView(
    context: Context,
    attrs: AttributeSet,
    colour: Int,
    rippleType: FillStyle,
    private val rippleStrokeWidth: Float) : BaseCustomView(context, attrs) {
    override fun onConstructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
    }

    private val paint = Paint()

    init {
        visibility = INVISIBLE

        paint.apply {
            isAntiAlias = true
            color = colour
            style = when (rippleType) {
                FillStyle.FILL -> {
                    strokeWidth = 0f
                    Paint.Style.FILL
                }
                FillStyle.STROKE -> {
                    strokeWidth = rippleStrokeWidth
                    Paint.Style.STROKE
                }
                FillStyle.FILL_AND_STROKE -> {
                    strokeWidth = rippleStrokeWidth
                    Paint.Style.FILL_AND_STROKE
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getMeasurementSize(widthMeasureSpec, getDesiredWidthSize())
        val height = getMeasurementSize(heightMeasureSpec, getDesiredHeightSize())
        setMeasuredDimension(width, height)
    }

    private fun getMeasurementSize(measureSpec: Int, defaultSize: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        when (mode) {
            /**
             *  match_parent: the size will be equal parent's size
             *  exact pixels: specified size which is set
             */
            View.MeasureSpec.EXACTLY -> return size

            /**
             *wrap_content:  The size that gets passed could be much larger, taking up the rest of the space. So it might
            say, “I have 411 pixels. Tell me your size that doesn’t exceed 411 pixels.” The question then to the
            programmer is: What should I return?
             */
            View.MeasureSpec.AT_MOST -> return Math.min(defaultSize, size)

            /** Documentation says that this mode is passed in when the layout wants to
            know what the true size is. True size could be as big as it could be; layout will likely then scroll it.
            With that thought, we have returned the maximum size for our view.
             */
            View.MeasureSpec.UNSPECIFIED -> return defaultSize
            else -> return defaultSize
        }
    }

    override fun onDraw(canvas: Canvas) {
        val radius: Float = min(width, height) / 2.toFloat()
        canvas.drawCircle(radius, radius, radius - rippleStrokeWidth, paint)
    }

    override fun getDesiredWidthSize(): Int {
        return MIN_WIDTH
    }

    override fun getDesiredHeightSize(): Int {
        return MIN_HEIGHT
    }


    /**
     * This enum is tightly coupled with attrs#VoiceRippleView.
     */
    enum class FillStyle(val type: Int) {
        FILL(0), STROKE(1), FILL_AND_STROKE(2);
        companion object{
            fun valueOf(value: Int) : FillStyle{
                return when(value){
                    FILL.type -> FILL
                    STROKE.type -> STROKE
                    FILL_AND_STROKE.type -> FILL_AND_STROKE
                    else -> throw java.lang.IllegalArgumentException()
                }
            }
        }
    }
}
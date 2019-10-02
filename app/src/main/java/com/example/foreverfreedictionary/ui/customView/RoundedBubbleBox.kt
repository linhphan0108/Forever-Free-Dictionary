package com.example.foreverfreedictionary.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import android.graphics.*
import android.view.View
import android.widget.Toast
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.util.ScreenUtil
import kotlin.math.max


const val DEFAULT_ARROW_HEIGHT = 44F
const val DEFAULT_ARROW_WIDTH = DEFAULT_ARROW_HEIGHT
class RoundedBubbleBox : LinearLayout {

    private var arrowHeight = DEFAULT_ARROW_HEIGHT
    private var arrowWidth = DEFAULT_ARROW_WIDTH
    private var recBoxRadius = arrowHeight * 0.3f
    private var anchorCenterXOnScreen: Float = 0f

    private lateinit var arrowPaint: Paint
    private lateinit var arrowPath: Path
    private val recBox: RectF by lazy { RectF() }
    private val recBoxContent by lazy { RectF() }
    private var fillColor: Int = Color.RED

    constructor(context: Context): this(context, null, 0, 0)

    constructor(context: Context,
                attrs: AttributeSet? = null): this(context, attrs, 0, 0)

    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0): this(context, attrs, defStyleAttr, 0)
    @SuppressLint("NewApi")
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0,
                defStyleRes: Int = 0): super(context, attrs, defStyleAttr, defStyleRes){
        setWillNotDraw(false)
        onConstruct(context, attrs, defStyleAttr, defStyleRes)
    }


    private fun onConstruct(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0){
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.RoundedBubbleBox, defStyleAttr, defStyleRes)
        try {
        } finally {
            fillColor = ta.getColor(R.styleable.RoundedBubbleBox_fillColor, Color.RED)
            arrowHeight = ta.getDimension(R.styleable.RoundedBubbleBox_arrow_height, DEFAULT_ARROW_HEIGHT)
            arrowWidth = ta.getDimension(R.styleable.RoundedBubbleBox_arrow_width, DEFAULT_ARROW_WIDTH)
            recBoxRadius = ta.getDimension(R.styleable.RoundedBubbleBox_square_box_radius, 0f)
            ta.recycle()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setPadding(
                max(paddingStart, recBoxRadius.toInt()),
                paddingTop + arrowHeight.toInt(),
                max(paddingEnd, recBoxRadius.toInt()),
                max(paddingBottom, recBoxRadius.toInt())

            )
        }else{
            setPadding(
                max(paddingLeft, recBoxRadius.toInt()),
                paddingTop + arrowHeight.toInt(),
                max(paddingRight, recBoxRadius.toInt()),
                max(paddingBottom, recBoxRadius.toInt())

            )
        }

        init()
        setOnClickListener {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init(){
        arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arrowPaint.color = fillColor
        arrowPaint.style = Paint.Style.FILL_AND_STROKE
        arrowPaint.isAntiAlias = true

        //the arrow
        arrowPath = Path()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        calculateRecBox()
        canvas.drawRoundRect(recBox, recBoxRadius, recBoxRadius, arrowPaint)
        calculateArrowPath()
        canvas.drawPath(arrowPath, arrowPaint)
    }

    private fun calculateArrowPath(){
        val arrowHalfWidth = arrowWidth * 0.5f
        val arrowCenterX = when {
            anchorCenterXOnScreen < recBoxContent.left -> recBoxContent.left
            anchorCenterXOnScreen > recBoxContent.right + translationX -> {
                recBoxContent.right - arrowHalfWidth
            }else -> {
                var temp = anchorCenterXOnScreen - recBoxContent.left - translationX - arrowHalfWidth
                if (temp < recBoxContent.left + arrowHalfWidth) {
                    temp = recBoxContent.left + arrowHalfWidth
                }
                temp
            }
        }

        val point1 = PointF(arrowCenterX, 0f)
        val point2 = PointF(arrowCenterX + arrowHalfWidth, arrowHeight)
        val point3 = PointF(arrowCenterX - arrowHalfWidth, arrowHeight)
        arrowPath.moveTo(point1.x, point1.y)
        arrowPath.lineTo(point2.x, point2.y)
        arrowPath.lineTo(point3.x, point3.y)
        arrowPath.lineTo(point1.x, point1.y)
        arrowPath.close()
    }

    private fun calculateRecBox(){
        recBox.set(left.toFloat(), top.toFloat() + arrowHeight,
            right.toFloat(), bottom.toFloat())
        recBoxContent.set(recBox.left + recBoxRadius, recBox.top + recBoxRadius,
            recBox.right - recBoxRadius, recBox.bottom - recBoxRadius)
    }

    fun setAnchor(anchor: View){
        val anchorLocation = IntArray(2)
        anchor.getLocationInWindow(anchorLocation)
        anchorCenterXOnScreen = anchorLocation[0] + anchor.width * 0.2f

        val screenWidth = ScreenUtil.screenWidth()
        var translateX = when {
            anchorLocation[0] < this.left -> anchorLocation[0] - paddingLeft
            anchorLocation[0] + this.width > screenWidth -> anchorLocation[0] + this.width - screenWidth - paddingLeft
            else -> anchorLocation[0] - paddingLeft
        }
        translateX = if (translateX > recBoxRadius) translateX else recBoxRadius.toInt()
        this@RoundedBubbleBox.translationX = translateX.toFloat()
        this@RoundedBubbleBox.translationY = anchorLocation[1].toFloat()
        invalidate()
    }
}
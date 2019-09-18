package com.example.foreverfreedictionary.ui.customView.voiceRippleView

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Property
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import com.example.foreverfreedictionary.R
import com.example.foreverfreedictionary.util.ScreenUtil
import kotlinx.android.synthetic.main.dialog_voice_recognizer.view.*


private const val DEFAULT_RIPPLE_SCALE = 4f
private val DEFAULT_STROKE_WIDTH = ScreenUtil.dp2px(1)
private val DEFAULT_RADIUS = ScreenUtil.dp2px(16)
private val DEFAULT_FILL_TYPE = CircleView.FillStyle.FILL
private const val DEFAULT_RIPPLE_DURATION = 1000

class VoiceRippleView(context: Context, private val attrs: AttributeSet) : RelativeLayout(context, attrs){

    private var rippleScale: Float = DEFAULT_RIPPLE_SCALE
    private var rippleColor: Int = 0
    private var rippleType = CircleView.FillStyle.FILL
    private var rippleStrokeWidth: Float = DEFAULT_STROKE_WIDTH.toFloat()
    private var rippleRadius: Float = DEFAULT_RADIUS.toFloat()
    private var duration: Int = DEFAULT_RIPPLE_DURATION
    private var animatorSet: AnimatorSet? = null

    private lateinit var circleView: CircleView
    private lateinit var iBtn: ImageButton

    var listener: OnClickListener? = null

    init {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.VoiceRippleView)
        rippleRadius = styledAttributes.getDimension(R.styleable.VoiceRippleView_rv_radius, resources.getDimension(R.dimen.rippleRadius))
        rippleStrokeWidth = styledAttributes.getDimension(R.styleable.VoiceRippleView_rv_strokeWidth, DEFAULT_STROKE_WIDTH.toFloat())
        rippleColor = styledAttributes.getColor(R.styleable.VoiceRippleView_rv_color, ContextCompat.getColor(context, R.color.colorAccent))
        rippleType = CircleView.FillStyle.valueOf(styledAttributes.getInt(R.styleable.VoiceRippleView_rv_type, DEFAULT_FILL_TYPE.type))
        rippleScale = styledAttributes.getFloat(R.styleable.VoiceRippleView_rv_scale, DEFAULT_RIPPLE_SCALE)
        duration = styledAttributes.getInteger(R.styleable.VoiceRippleView_rv_duration, DEFAULT_RIPPLE_DURATION)
        styledAttributes.recycle()
        prepareViews()
    }

    private fun prepareViews(){
        circleView = CircleView(context, attrs, rippleColor, rippleType, rippleStrokeWidth).apply {
            visibility = View.VISIBLE
        }
        iBtn = ImageButton(context, attrs)
        iBtn.setImageResource(R.drawable.outline_mic_white_36)
        iBtn.setBackgroundResource(R.drawable.micro_listening_circular_bg)
        iBtn.setOnClickListener{
            listener?.onClick(it)
        }

        addView(circleView, getCircleLayoutParams())
        addView(iBtn, getIconLayoutParams())
    }

    /**
     * Call this class to initiate a new ripple animation.
     */
    fun startAnimation() {
        if(animatorSet == null) {
            animatorSet = generateRipple(duration = duration, target = circleView)
        }
        animatorSet!!.start()
    }

    /**
     * when called it will stop the animation but all listeners attaching the animation still remain,
     * in case you can restart the animation late on.
     */
    fun stopAnimation(){
        animatorSet?.cancel()
    }

    /**
     * when called it will stop the animation and all animation's listeners attached
     */
    fun clear(){
        animatorSet?.cancel()
        animatorSet?.removeAllListeners()
    }

    private fun generateRipple(duration: Int, target: CircleView): AnimatorSet {
        val animatorList = ArrayList<Animator>()
        animatorList.add(provideAnimator(
            target = target,
            type = View.SCALE_X,
            animDuration = duration,
            scale = rippleScale
        ))

        animatorList.add(provideAnimator(
            target = target,
            type = View.SCALE_Y,
            animDuration = duration,
            scale = rippleScale
        ))

        animatorList.add(provideAnimator(
            target = target,
            type = View.ALPHA,
            animDuration = duration,
            scale = 0f
        ))
        return AnimatorSet().apply {
            this.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    target.scaleX = 1f
                    target.scaleY = 1f
                    target.alpha = 1f
                }
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
            })
            playTogether(animatorList)
        }
    }

    private fun provideAnimator(
        target: View,
        type: Property<View, Float>,
        animDuration: Int,
        scale: Float = DEFAULT_RIPPLE_SCALE
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(target, type, scale).apply {
            duration = animDuration.toLong()
            repeatCount = ValueAnimator.INFINITE
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                }
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
            })
        }
    }

    private fun getCircleLayoutParams(): LayoutParams {
        val widthHeight = (2 * rippleRadius + rippleStrokeWidth).toInt()
        return LayoutParams(widthHeight, widthHeight).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        }
    }

    private fun getIconLayoutParams(): LayoutParams {
        val circleSize = (2 * rippleRadius + rippleStrokeWidth)
        val iconSize = (circleSize + circleSize * rippleScale * 0.25).toInt()
        return LayoutParams(iconSize, iconSize).apply {
            addRule(CENTER_IN_PARENT, TRUE)
        }
    }
}

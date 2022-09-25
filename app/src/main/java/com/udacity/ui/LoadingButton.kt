package com.udacity.ui

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.udacity.MainActivity.Companion.DEFAULT_ANIMATION_DURATION
import com.udacity.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // defining variables that we need
    private var widthSize = 0
    private var heightSize = 0
    private var circleDimAnimation = 360f
    private var btnW = 0f
    private var loadingB = 1f
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var btnTxt = resources.getText(R.string.button_name).toString()
    private var btnColor = resources.getColor(R.color.colorPrimary)
    private var btnColorDark = resources.getColor(R.color.colorPrimaryDark)
    private var btnLoadingTxt = resources.getText(R.string.button_loading).toString()
    private val animatorSet = AnimatorSet()
    lateinit var loadingBtnAnimator: ValueAnimator
    lateinit var circleAnimator: ValueAnimator
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Clicked) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                btnTxt = btnLoadingTxt
                // here we say if button was loading we call the fun animationFunLoading
                animationFunLoading()
            }
            ButtonState.Completed ->{
                // here we say if button was completed we call the fun animationFunEnd
                animationFunEnd()
            }
    }
        // we update and that's it
        invalidate()
    }

    private fun animationFunEnd() {
        // we call animatorSet if its the end of animation
        animatorSet.end()
        // and make the btn clickable again
        this.isClickable = true
    }
     fun animationFunLoading() {
         // animation for rect
        loadingBtnAnimator = ValueAnimator.ofFloat(0f,(widthSize.toFloat())).apply {
            interpolator =AccelerateInterpolator(1f)
            repeatMode = ValueAnimator.RESTART
            repeatCount = 1
            addUpdateListener {
                btnW = animatedValue as Float
                invalidate()
            }
        }
         // animation for circle
        circleAnimator = ValueAnimator.ofFloat(0f,circleDimAnimation).apply {
            interpolator =AccelerateInterpolator(1f)
            addUpdateListener {animation ->
                loadingB = animatedValue as Float
                invalidate()
            }
        }
         // here i play animation together
        animatorSet.play(loadingBtnAnimator).with(circleAnimator)
        animatorSet.duration = DEFAULT_ANIMATION_DURATION
        animatorSet.start()
         // and make clickable false until the end of animation
        this.isClickable=false
    }
    init {
        isClickable = true
        buttonState = ButtonState.Clicked
        context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
            .apply {
                btnTxt = getText(R.styleable.LoadingButton_btnTxt) as String
                btnColor = getColor(R.styleable.LoadingButton_btnColor,0)
                btnColorDark = getColor(R.styleable.LoadingButton_btnColorDark,0)
            }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        loadingBtn(canvas)
        loadingCircle(canvas)
        textInBtn(canvas)
    }
    private fun textInBtn(canvas: Canvas?) {
        // draw txt
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.color = Color.WHITE
        paint.textSize = 40f
        canvas?.drawText(btnTxt,widthSize.toFloat() / 2,heightSize.toFloat() / 2,paint)
    }
    private fun loadingCircle(canvas: Canvas?) {
        // draw circle
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = resources.getColor(R.color.colorAccent)
        canvas?.drawArc( widthSize - 140f, heightSize / 2 - 40f,
            widthSize - 75f, heightSize / 2 + 40f,
            0f,loadingB , true,paint)
    }
    private fun loadingBtn(canvas: Canvas?) {
        // draw rect for btn
            paint.style = Paint.Style.FILL
            paint.textAlign = Paint.Align.CENTER
            paint.typeface = Typeface.create("",Typeface.BOLD)
            paint.color = btnColor
            canvas?.drawRect(0f, 0f, widthSize.toFloat(), measuredHeight.toFloat(), paint)

    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}
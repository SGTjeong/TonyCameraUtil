package com.example.tonywidgets

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import java.lang.Exception

class TonyRecordButton : View {
    interface ActionListener {
        fun onHandsFreeReady(isReady : Boolean){}

        fun onHandsFree(){}

        fun onStartRecord(){}

        fun onFinishRecord(){}

        fun onCapture()
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if(context == null || attrs == null) return
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        if(context == null || attrs == null) return
        init(context, attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    companion object{
        val DEFAULT_SIZE = 300f
        val DEFAULT_STROKE_WIDTH = 25f
        val DEFAULT_DURATION = 30000L
    }

    private var listener : ActionListener?= null

    private var currentSize = 300f
    private var outerSize = 300f
    private var expandSize = 350f
    private var containerSize = 400f
    private var rectSize = 200f
    private var rectCurrentSize = 0f

    private var strokeWidth = DEFAULT_STROKE_WIDTH
    private var strokeColor : Int = Color.WHITE
    private var buttonColor : Int = Color.WHITE
    private var recordColor : Int = Color.RED
    private var strokeAlpha : Int = 255

    private var startRecordTime = System.currentTimeMillis()
    private var duration = DEFAULT_DURATION
    private var outerBorderRect = RectF()
    private var innerRect = RectF()
    private var isRecording = false
    private var recordEnabled = true

    private lateinit var recordAngleValueAnimator : ValueAnimator
    private lateinit var buttonScaleValueAnimator : ValueAnimator
    private lateinit var rectScaleValueAnimator : ValueAnimator


    private fun init(context: Context, attrs: AttributeSet) {
        loadAttributes(context, attrs)
        initAnimation()
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attrArr = context.obtainStyledAttributes(attrs, R.styleable.tony_camera_button, 0, 0)
        try{
            duration = attrArr.getInteger(R.styleable.tony_camera_button_maxDuration, DEFAULT_DURATION.toInt()).toLong()
            strokeWidth = attrArr.getDimensionPixelSize(R.styleable.tony_camera_button_strokeWidth, 100).toFloat()
            buttonColor = attrArr.getColor(R.styleable.tony_camera_button_buttonColor, Color.WHITE)
            strokeColor = attrArr.getColor(R.styleable.tony_camera_button_strokeColor, Color.WHITE)
            recordColor = attrArr.getColor(R.styleable.tony_camera_button_recordColor, Color.RED)
            strokeAlpha = attrArr.getInteger(R.styleable.tony_camera_button_strokeAlpha, 255)
            invalidate()
        } catch (e : Exception){
            Log.e("TonyPhonePicker", e.toString())
        } finally {
            attrArr.recycle()
        }
    }

    private fun initAnimation() {
        recordAngleValueAnimator = ValueAnimator.ofInt(0, duration.toInt()).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = this@TonyRecordButton.duration
            addUpdateListener {
                postInvalidate()
                if((it.animatedValue as Int) == duration.toInt()){
                    onRecordEnd()
                }
            }
        }

        buttonScaleValueAnimator = ValueAnimator.ofFloat().apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = 300L
            addUpdateListener {
                currentSize = it.animatedValue as Float
                postInvalidate()
                if(currentSize == expandSize){
                    if(!isRecording && it.currentPlayTime > duration * 0.8){
                        onRecordStart()
                    }
                }
            }
        }


        rectScaleValueAnimator = ValueAnimator.ofFloat().apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = 300L
            addUpdateListener {
                rectCurrentSize = it.animatedValue as Float
                postInvalidate()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val minSide = Math.min(w,h)

        containerSize = minSide.toFloat()
        outerSize = containerSize / 1.5f
        expandSize = outerSize * 1.25f
        currentSize = outerSize
        rectSize = outerSize / 3

        outerBorderRect.set(
            (containerSize - expandSize)/2,
            (containerSize - expandSize)/2,
            (containerSize + expandSize)/2,
            (containerSize + expandSize)/2
        )

        innerRect.set(
            (containerSize - rectSize)/2,
            (containerSize - rectSize)/2,
            (containerSize + rectSize)/2,
            (containerSize + rectSize)/2
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let{
            val whitePaint = Paint()
            whitePaint.color = strokeColor
            whitePaint.alpha = strokeAlpha
            whitePaint.style = Paint.Style.STROKE
            whitePaint.strokeWidth = strokeWidth
            whitePaint.isAntiAlias = true

            val grayPaint = Paint()
            grayPaint.color = buttonColor
            grayPaint.style = Paint.Style.FILL
            grayPaint.isAntiAlias = true

            Log.e("WONSIK", "draw : $strokeWidth")
            it.drawCircle(containerSize/2, containerSize/2, currentSize/2, whitePaint)
            it.drawCircle(containerSize/2, containerSize/2, (currentSize-strokeWidth)/2, grayPaint )

            val redPaint = Paint()
            redPaint.color = recordColor
            redPaint.style = Paint.Style.STROKE
            redPaint.strokeWidth = strokeWidth
            redPaint.strokeCap = Paint.Cap.ROUND
            redPaint.strokeJoin = Paint.Join.ROUND
            redPaint.isAntiAlias = true

            val redFillPaint = Paint()
            redFillPaint.color = recordColor
            redFillPaint.isAntiAlias = true

            it.drawRoundRect(
                (containerSize - rectCurrentSize)/2,
                (containerSize - rectCurrentSize)/2,
                (containerSize + rectCurrentSize)/2,
                (containerSize + rectCurrentSize)/2,
                10f,
                10f,
                redFillPaint
            )

            if(isRecording) {
                outerBorderRect.set(
                    (containerSize - currentSize)/2,
                    (containerSize - currentSize)/2,
                    (containerSize + currentSize)/2,
                    (containerSize + currentSize)/2
                )
                canvas.drawArc(outerBorderRect, -90f, calculateCurrentAngle(), false, redPaint)
            }
        }
    }

    private fun onCapture() {
        listener?.onCapture()
    }

    private fun calculateCurrentAngle() : Float {
        val millisPassed = System.currentTimeMillis() - startRecordTime
        return millisPassed * 360f / duration
    }

    private fun onLongPressTrigger() {
        buttonScaleValueAnimator.setFloatValues(outerSize, expandSize)
        buttonScaleValueAnimator.start()
    }

    private fun onRecordStart() {
        isRecording = true
        startRecordTime = System.currentTimeMillis()
        recordAngleValueAnimator.start()
        listener?.onStartRecord()
    }


    private fun onRecordEnd() {
        isRecording = false
        isHandsFree = false
        isReadyForHandsFree = false
        buttonScaleValueAnimator.setFloatValues(expandSize, outerSize)
        buttonScaleValueAnimator.start()
        recordAngleValueAnimator.cancel()
        rectCurrentSize = 0f
        invalidate()
        listener?.onFinishRecord()
    }

    private var isReadyForHandsFree = false
    private var isHandsFree = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector?.onTouchEvent(event)
        if(!isRecording && event?.action == MotionEvent.ACTION_UP){
            buttonScaleValueAnimator?.cancel()
            buttonScaleValueAnimator?.setFloatValues(currentSize, outerSize)
            buttonScaleValueAnimator?.start()
        }

        if(isRecording && event?.action == MotionEvent.ACTION_UP){
            if(isReadyForHandsFree){
                isHandsFree = true
                rectScaleValueAnimator.setFloatValues(0f, rectSize)
                rectScaleValueAnimator.start()
                listener?.onHandsFree()
            }
            else {
                onRecordEnd()
            }
        }

        if(isRecording && event?.action == MotionEvent.ACTION_POINTER_UP){
            if(isReadyForHandsFree){
                isHandsFree = true
            }
            else {
                onRecordEnd()
            }
        }

        if(event?.action == MotionEvent.ACTION_MOVE){
            if(event.x < 0){
                if(isRecording && !isReadyForHandsFree){
                    isReadyForHandsFree = true
                    buttonScaleValueAnimator.setFloatValues(expandSize, outerSize)
                    buttonScaleValueAnimator.start()
                    listener?.onHandsFreeReady(true)
                }
            }
            else{
                if(isRecording && isReadyForHandsFree){
                    isReadyForHandsFree = false
                    buttonScaleValueAnimator.setFloatValues(outerSize, expandSize)
                    buttonScaleValueAnimator.start()
                    listener?.onHandsFreeReady(false)
                }
            }

        }

        return true
    }

    fun setRecordEnabled(isEnabled: Boolean){
        recordEnabled = isEnabled
    }

    fun setActionListener(listener : ActionListener){
        this.listener = listener
    }

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener(){
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            if(isRecording){
                onRecordEnd()
            }
            else{
                onCapture()
            }

            return super.onSingleTapUp(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            if(recordEnabled) {
                onLongPressTrigger()
            }
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return true
        }
    })

}
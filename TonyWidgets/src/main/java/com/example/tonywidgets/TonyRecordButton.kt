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
    private val TAG = this.javaClass.simpleName

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

    private var listener : ActionListener?= null

    private var currentSize = DEFAULT_SIZE
    private var outerSize = DEFAULT_SIZE
    private var expandSize = DEFAULT_SIZE * 1.25f
    private var containerSize = DEFAULT_SIZE * 1.5f
    private var rectSize = DEFAULT_SIZE / 3
    private var rectCurrentSize = 0f

    private var strokeWidth = DEFAULT_STROKE_WIDTH
    private var strokeColor : Int = DEFAULT_COLOR
    private var buttonColor : Int = DEFAULT_COLOR
    private var recordColor : Int = DEFAULT_RECORD_COLOR
    private var strokeAlpha : Int = DEFAULT_STROKE_ALPHA

    private var duration = DEFAULT_DURATION
    private var isRecording = false
    private var recordEnabled = true
    private var startRecordTime = System.currentTimeMillis()

    private lateinit var recordAngleValueAnimator : ValueAnimator
    private lateinit var buttonScaleValueAnimator : ValueAnimator
    private lateinit var rectScaleValueAnimator : ValueAnimator


    private fun init(context: Context, attrs: AttributeSet) {
        loadAttributes(context, attrs)
        initAnimation()
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attrArr = context.obtainStyledAttributes(attrs, R.styleable.tony_record_button, 0, 0)
        try{
            duration = attrArr.getInteger(R.styleable.tony_record_button_maxDuration, DEFAULT_DURATION.toInt()).toLong()
            strokeWidth = attrArr.getDimensionPixelSize(R.styleable.tony_record_button_strokeWidth, DEFAULT_STROKE_WIDTH.toInt()).toFloat()
            buttonColor = attrArr.getColor(R.styleable.tony_record_button_buttonColor, DEFAULT_COLOR)
            strokeColor = attrArr.getColor(R.styleable.tony_record_button_strokeColor, DEFAULT_COLOR)
            recordColor = attrArr.getColor(R.styleable.tony_record_button_recordColor, DEFAULT_RECORD_COLOR)
            strokeAlpha = attrArr.getInteger(R.styleable.tony_record_button_strokeAlpha, DEFAULT_STROKE_ALPHA)
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
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return

        drawStroke(canvas)
        drawInnerCircle(canvas)
        if(isRecording) {
            drawRecordStroke(canvas)
            drawRecordRect(canvas)
        }
    }

    private fun drawInnerCircle(canvas: Canvas) {
        val centerPaint = Paint().apply {
            color = buttonColor
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(containerSize/2, containerSize/2, (currentSize-strokeWidth)/2, centerPaint )
    }

    private fun drawStroke(canvas: Canvas) {
        val strokePaint = Paint().apply {
            color = strokeColor
            alpha = strokeAlpha
            style = Paint.Style.STROKE
            strokeWidth = this@TonyRecordButton.strokeWidth
            isAntiAlias = true
        }
        canvas.drawCircle(containerSize/2, containerSize/2, currentSize/2, strokePaint)
    }

    private fun drawRecordStroke(canvas: Canvas) {
        val strokeRecordPaint = Paint().apply {
            color = recordColor
            style = Paint.Style.STROKE
            strokeWidth = this@TonyRecordButton.strokeWidth
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }
        val outerBorderRect = RectF().apply {
            set(
                (containerSize - currentSize)/2,
                (containerSize - currentSize)/2,
                (containerSize + currentSize)/2,
                (containerSize + currentSize)/2
            )
        }
        canvas.drawArc(outerBorderRect, -90f, calculateCurrentAngle(), false, strokeRecordPaint)
    }

    private fun drawRecordRect(canvas: Canvas) {
        val recordPaint = Paint()?.apply {
            color = recordColor
            isAntiAlias = true
        }
        canvas.drawRoundRect(
            (containerSize - rectCurrentSize)/2,
            (containerSize - rectCurrentSize)/2,
            (containerSize + rectCurrentSize)/2,
            (containerSize + rectCurrentSize)/2,
            10f,
            10f,
            recordPaint
        )
    }

    fun setRecordEnabled(isEnabled: Boolean){
        recordEnabled = isEnabled
    }

    private fun onCapture() {
        listener?.onCapture()
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

    private fun calculateCurrentAngle() : Float {
        val millisPassed = System.currentTimeMillis() - startRecordTime
        return millisPassed * 360f / duration
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

            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            if(recordEnabled) {
                onLongPressTrigger()
            }
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            return true
        }
    })

    fun setActionListener(listener : ActionListener){
        this.listener = listener
    }

    interface ActionListener {
        fun onHandsFreeReady(isReady : Boolean){}

        fun onHandsFree(){}

        fun onStartRecord(){}

        fun onFinishRecord(){}

        fun onCapture()
    }

    companion object{
        val DEFAULT_SIZE = 300f
        val DEFAULT_STROKE_WIDTH = 25f
        val DEFAULT_STROKE_ALPHA = 255
        val DEFAULT_DURATION = 30000L
        val DEFAULT_COLOR = Color.WHITE
        val DEFAULT_RECORD_COLOR = Color.RED
    }
}
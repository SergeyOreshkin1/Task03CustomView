package com.example.task03customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.icu.util.Calendar
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin

class CustomAnalogClock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mHeight = 0
    private var mWidth = 0
    private var mPadding = 0
    private val mNumeralSpacing = 0
    private var mHandTruncation = 0
    private var mHourHandTruncation = 0
    private var mRadius = 0
    private var mPaint: Paint? = null
    private val mRect: Rect = Rect()
    private var isInit = false
    private var hourHandColor: Int = 0
    private var minuteHandColor: Int = 0
    private var secondHandColor: Int = 0

    private val mClockHours = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    init {
        mPaint = Paint()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomAnalogClock,
            0, 0
        ).apply {

            try {
                hourHandColor = getResourceId(R.styleable.CustomAnalogClock_hourHandColor, 0)
                minuteHandColor = getResourceId(R.styleable.CustomAnalogClock_minuteHandColor, 0)
                secondHandColor = getResourceId(R.styleable.CustomAnalogClock_secondHandColor, 0)

            } finally {
                recycle()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isInit) {

            mHeight = height
            mWidth = width
            mPadding = mNumeralSpacing + 50
            val minAttr = height.coerceAtMost(width)
            mRadius = minAttr / 2 - mPadding

            mHandTruncation = minAttr / 20
            mHourHandTruncation = minAttr / 17
            isInit = true
        }

        requireNotNull(canvas).drawColor(Color.WHITE)

        mPaint?.reset()
        mPaint?.style = Paint.Style.STROKE;
        mPaint?.strokeWidth = 12f
        mPaint?.isAntiAlias = true;
        mPaint?.let {
            canvas.drawCircle(
                (mWidth / 2).toFloat(),
                (mHeight / 2).toFloat(),
                (mRadius + mPadding - 10).toFloat(),
                it
            )
        }
        mPaint?.style = Paint.Style.FILL
        mPaint?.let { canvas.drawCircle((mWidth / 2).toFloat(), (mHeight / 2).toFloat(), 12f, it) }

        val fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, resources.displayMetrics)
                .toInt()
        mPaint?.textSize = fontSize.toFloat()

        for (hour in mClockHours) {
            val tmp = hour.toString()
            requireNotNull(mPaint).getTextBounds(tmp, 0, tmp.length, mRect)

            val angle = Math.PI / 6 * (hour - 3)
            val x = (mWidth / 2 + cos(angle) * mRadius - mRect.width() / 2).toInt()
            val y = (mHeight / 2 + sin(angle) * mRadius + mRect.height() / 2).toInt()
            canvas.drawText(
                hour.toString(),
                x.toFloat(),
                y.toFloat(),
                requireNotNull(mPaint)
            )
        }

        val calendar: Calendar = Calendar.getInstance()
        var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour

        drawHandLine(
            canvas, ((hour + calendar.get(Calendar.MINUTE) / 60) * 5f).toInt(),
            isHour = true, isSecond = false
        )

        drawHandLine(canvas, calendar.get(Calendar.MINUTE), isHour = false, isSecond = false)

        drawHandLine(canvas, calendar.get(Calendar.SECOND), isHour = false, isSecond = true)

        postInvalidateDelayed(500)
        invalidate()

    }

    private fun drawHandLine(canvas: Canvas, moment: Int, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * moment / 30 - Math.PI / 2
        val handRadius =
            if (isHour) mRadius - mHandTruncation - mHourHandTruncation else mRadius - mHandTruncation
        requireNotNull(mPaint).color = ContextCompat.getColor(context, hourHandColor)
        if (isSecond) requireNotNull(mPaint).color =
            ContextCompat.getColor(context, secondHandColor)
        if (!isSecond && !isHour) requireNotNull(mPaint).color =
            ContextCompat.getColor(context, minuteHandColor)
        canvas.drawLine(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mWidth / 2 + cos(angle) * handRadius).toFloat(),
            (mHeight / 2 + sin(angle) * handRadius).toFloat(),
            requireNotNull(mPaint)
        )
    }

    fun setColor(
        @DrawableRes hourHandColor: Int = R.color.black,
        @DrawableRes minuteHandColor: Int = R.color.black,
        @DrawableRes secondHandColor: Int = R.color.black
    ) {
        this.hourHandColor = hourHandColor
        this.minuteHandColor = minuteHandColor
        this.secondHandColor = secondHandColor
    }
}
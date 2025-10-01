package com.airei.app.phc.attendance.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.airei.app.phc.attendance.R

class HorizontalSegmentedProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var segmentCount: Int = 5
    private var segmentHeight: Float = 10f
    private var segmentSpacing: Float = 10f
    private var segmentRadius: Float = 5f
    private var currentStep: Int = 0
    private var segmentColor: Int = Color.BLACK
    private var completedSegmentColor: Int = Color.GREEN

    private var segmentWidth: Float = 40f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalSegmentedProgressBar,
            0, 0
        ).apply {
            try {
                segmentCount = getInt(R.styleable.HorizontalSegmentedProgressBar_segmentCount, 5)
                segmentHeight =
                    getDimension(R.styleable.HorizontalSegmentedProgressBar_segmentHeight, 10f)
                segmentSpacing =
                    getDimension(R.styleable.HorizontalSegmentedProgressBar_segmentSpacing, 10f)
                segmentRadius =
                    getDimension(R.styleable.HorizontalSegmentedProgressBar_segmentRadius, 5f)
                currentStep = getInt(R.styleable.HorizontalSegmentedProgressBar_currentStep, 0)
                segmentColor =
                    getColor(R.styleable.HorizontalSegmentedProgressBar_segmentColor, Color.BLACK)
                completedSegmentColor = getColor(
                    R.styleable.HorizontalSegmentedProgressBar_completedSegmentColor,
                    Color.GREEN
                )
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL

        // Calculate the segment width based on the view width and spacing
        segmentWidth = (width - (segmentCount - 1) * segmentSpacing) / segmentCount

        for (i in 0 until segmentCount) {
            paint.color = if (i < currentStep) completedSegmentColor else segmentColor
            val left = i * (segmentWidth + segmentSpacing)
            val top = (height / 2f) - (segmentHeight / 2f)
            val right = left + segmentWidth
            val bottom = top + segmentHeight
            canvas.drawRoundRect(left, top, right, bottom, segmentRadius, segmentRadius, paint)
        }
    }

    fun setCurrentStep(step: Int) {
        currentStep = step
        invalidate()
    }

    fun getCurrentStep(): Int {
        return currentStep
    }

    fun getSegmentCount(): Int {
        return segmentCount
    }
}
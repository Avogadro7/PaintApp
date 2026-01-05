package com.example.paintapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ExperimentCanvas(
    private val context: Context,
    attrs: AttributeSet
) : View(context, attrs) {
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var path: Path = Path()
    private var paint: Paint = Paint()
    private var x: Float = 0.toFloat()
    private var y: Float = 0.toFloat()

    private var brushColor: Int = Color.BLACK
    private var brushStrokeWidth = 5f
    private var paths: MutableList<PathData> = ArrayList()

    init {

        paint.color = brushColor
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = brushStrokeWidth


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (mPath: PathData in paths) {
            paint.color = mPath.color
            paint.strokeWidth = mPath.strokeWidth.toFloat()
            canvas.drawPath(mPath.path, paint)
        }


    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
    }

    fun onStartTouchEvent(x: Float, y: Float) {
        path = Path()
        val pathData = PathData(brushColor, brushStrokeWidth.toInt(), path)
        paths.add(pathData)
        path.moveTo(x, y)
        this.x = x
        this.y = y
    }

    fun onMoveTouchEvent(x: Float, y: Float) {
        val dx = Math.abs(this.x - x)
        val dy = Math.abs(this.y - y)

        if (dx >= Tollerance || dy >= Tollerance) {
            path.quadTo(this.x, this.y, (x + this.x) / 2, (y + this.y) / 2)
            this.x = x
            this.y = y
        }
    }

    fun onUpTouchEvent() {
        path.lineTo(x, y)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            //GESTURE
            MotionEvent.ACTION_DOWN -> {
                onStartTouchEvent(x, y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                onMoveTouchEvent(x, y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                onUpTouchEvent()
                invalidate()
            }
        }

        return true
    }

    fun clearDrawing() {
        if (paths.size > 0) {
            path.reset()
            paths.clear()
            invalidate()
        }

    }

    fun undo() {
        if (paths.size > 0) {
            paths.removeAt(paths.lastIndex)
            invalidate()
        }
    }


    fun selectedColors(color: Int) {
        brushColor = color
    }

    fun setStrokeWidth(width: Int) {
        brushStrokeWidth = width.toFloat()
    }

    companion object {
        private val Tollerance = 5f
    }
}
package com.neko.circleimageview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.neko.v2ray.R

class CircleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shaderMatrix = Matrix()

    private var bitmapShader: BitmapShader? = null
    private var bitmap: Bitmap? = null

    private var borderColor: Int = Color.WHITE
    private var borderWidth: Float = 8f
    private var rainbowBorderEnabled = false

    private var sweepAngle = 0f
    private var animatedBorderWidth = borderWidth
    private var borderAlpha = 255

    private val rainbowColors = intArrayOf(
        Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED
    )

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0).apply {
            try {
                borderColor = getColor(R.styleable.CircleImageView_borderColor, Color.WHITE)
                borderWidth = getDimension(R.styleable.CircleImageView_borderWidth, 8f)
                rainbowBorderEnabled = getBoolean(R.styleable.CircleImageView_rainbowBorderEnabled, false)
                animatedBorderWidth = borderWidth
            } finally {
                recycle()
            }
        }

        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth

        startBorderAnimation()
        startPulseAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return

        if (bitmap == null) {
            bitmap = getBitmapFromDrawable()
            bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = bitmapShader
        }

        val radius = (width.coerceAtMost(height) / 2f) - animatedBorderWidth / 2f
        updateShaderMatrix()
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

        if (borderWidth > 0) {
            if (rainbowBorderEnabled) {
                val sweep = SweepGradient(width / 2f, height / 2f, rainbowColors, null)
                val matrix = Matrix()
                matrix.postRotate(sweepAngle, width / 2f, height / 2f)
                sweep.setLocalMatrix(matrix)
                borderPaint.shader = sweep
            } else {
                borderPaint.shader = null
                borderPaint.color = borderColor
            }

            borderPaint.strokeWidth = animatedBorderWidth
            borderPaint.alpha = borderAlpha
            canvas.drawCircle(width / 2f, height / 2f, radius, borderPaint)
        }
    }

    private fun getBitmapFromDrawable(): Bitmap {
        val d = drawable ?: throw IllegalStateException("Drawable is null")
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        return bmp
    }

    private fun updateShaderMatrix() {
        bitmap?.let {
            val scale: Float
            val dx: Float
            val dy: Float

            shaderMatrix.set(null)
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()
            val bWidth = it.width.toFloat()
            val bHeight = it.height.toFloat()

            if (bWidth * viewHeight > viewWidth * bHeight) {
                scale = viewHeight / bHeight
                dx = (viewWidth - bWidth * scale) * 0.5f
                dy = 0f
            } else {
                scale = viewWidth / bWidth
                dx = 0f
                dy = (viewHeight - bHeight * scale) * 0.5f
            }

            shaderMatrix.setScale(scale, scale)
            shaderMatrix.postTranslate(dx, dy)
            bitmapShader?.setLocalMatrix(shaderMatrix)
        }
    }

    private fun startBorderAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                sweepAngle = it.animatedValue as Float
                if (rainbowBorderEnabled) invalidate()
            }
            start()
        }
    }

    private fun startPulseAnimation() {
        ValueAnimator.ofFloat(borderWidth * 0.8f, borderWidth * 1.4f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                animatedBorderWidth = it.animatedValue as Float
                invalidate()
            }
            start()
        }

        ValueAnimator.ofInt(100, 255).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                borderAlpha = it.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    fun setRainbowBorderEnabled(enabled: Boolean) {
        rainbowBorderEnabled = enabled
        invalidate()
    }
}

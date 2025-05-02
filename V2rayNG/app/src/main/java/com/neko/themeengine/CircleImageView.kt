package com.neko.themeengine

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class CircleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1
) : ShapeableImageView(context, attrs, defStyleAttr) {

    private val shapeDrawable = MaterialShapeDrawable(
        ShapeAppearanceModel.builder()
            .setAllCornerSizes(CornerSize { it.height() / 2f })
            .build()
    )

    private lateinit var animator: ValueAnimator

    init {
        background = shapeDrawable
    }

    override fun setBackgroundColor(color: Int) {
        shapeDrawable.fillColor = ColorStateList.valueOf(color)
    }

    fun setBackgroundColorRes(@ColorRes color: Int) {
        setBackgroundColor(ResourcesCompat.getColor(resources, color, context.theme))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                animator = ValueAnimator.ofFloat(2f, 4f).apply {
                    addUpdateListener {
                        shapeDrawable.setCornerSize(CornerSize { rect ->
                            rect.height() / (it.animatedValue as Float)
                        })
                    }
                    start()
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                animator.reverse()
            }
        }
        return super.onTouchEvent(event)
    }
}

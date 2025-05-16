package com.neko.themeengine

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    init {
        background = MaterialShapeDrawable(
            ShapeAppearanceModel.builder()
                .setAllCornerSizes(CornerSize { it.height() / 2 })
                .build()
        )
    }

    override fun setBackgroundColor(color: Int) {
        (background as? MaterialShapeDrawable)?.fillColor = ColorStateList.valueOf(color)
    }

    fun setBackgroundColorRes(@ColorRes colorResId: Int) {
        val color = ResourcesCompat.getColor(resources, colorResId, context.theme)
        setBackgroundColor(color)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}

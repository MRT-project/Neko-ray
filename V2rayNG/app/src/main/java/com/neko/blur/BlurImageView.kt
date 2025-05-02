package com.neko.blur

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.neko.v2ray.R

/**
 * Custom ImageView that applies a blur effect to the displayed image using RenderScript.
 * Supports setting blur radius and bitmap scaling.
 */
class BlurImageView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null, 
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var defaultBitmapScale = 0.1f
    private val maxRadius = 25
    private val minRadius = 1
    private var imageOnView: Drawable? = null

    init {
        attrs?.let {
            if (drawable != null) {
                imageOnView = drawable
                val typedArray: TypedArray = context.theme.obtainStyledAttributes(it, R.styleable.BlurImageView, 0, 0)
                val radius = typedArray.getInteger(R.styleable.BlurImageView_radius, 0)
                setBlur(radius)
                typedArray.recycle()
            }
        }
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        setImageDrawable(BitmapDrawable(resources, bm))
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (imageOnView == null) imageOnView = drawable
    }

    /**
     * Sets the bitmap scale factor for blurring.
     */
    fun setBitmapScale(bitmapScale: Float) {
        defaultBitmapScale = bitmapScale
    }

    /**
     * Applies blur effect to the image based on the given radius.
     * If radius is 0, resets to the original image.
     */
    fun setBlur(radius: Int) {
        imageOnView = imageOnView ?: drawable
        
        when {
            radius in (minRadius + 1)..maxRadius -> {
                val blurred = blurRenderScript((imageOnView as BitmapDrawable).bitmap, radius)
                setImageBitmap(blurred)
                invalidate()
            }
            radius == 0 -> {
                setImageDrawable(imageOnView)
                invalidate()
            }
            else -> Log.e("BLUR", "Invalid blur radius: $radius")
        }
    }

    /**
     * Uses RenderScript to apply a Gaussian blur effect on the given bitmap.
     */
    private fun blurRenderScript(smallBitmap: Bitmap, radius: Int): Bitmap {
        val width = (smallBitmap.width * defaultBitmapScale).toInt()
        val height = (smallBitmap.height * defaultBitmapScale).toInt()

        val inputBitmap = Bitmap.createScaledBitmap(smallBitmap, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val renderScript = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        val inputAlloc = Allocation.createFromBitmap(renderScript, inputBitmap)
        val outputAlloc = Allocation.createFromBitmap(renderScript, outputBitmap)
        
        blurScript.setRadius(radius.toFloat())
        blurScript.setInput(inputAlloc)
        blurScript.forEach(outputAlloc)
        outputAlloc.copyTo(outputBitmap)
        
        return outputBitmap
    }
}

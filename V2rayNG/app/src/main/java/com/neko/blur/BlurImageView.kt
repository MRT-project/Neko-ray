package com.neko.blur

import android.content.Context
import android.graphics.*
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import com.neko.v2ray.R
import kotlin.math.roundToInt

/**
 * BlurImageView adalah ImageView kustom yang mendukung efek blur
 * pada gambar, kompatibel dengan Android API 27 hingga 35.
 *
 * - API 31+: Menggunakan View.setRenderEffect() untuk efek blur modern.
 * - API < 31: Menggunakan BlurMaskFilter sebagai fallback ringan.
 *
 * XML Atribut:
 * - app:radius="10" untuk mengatur blur secara langsung.
 */
class BlurImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var defaultBitmapScale = 0.2f
    private val maxRadius = 25
    private val minRadius = 1
    private var imageOnView: Drawable? = null
    private var initialRadius = 0
    private var appliedBlur = false

    init {
        // Simpan radius dari XML
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(it, R.styleable.BlurImageView, 0, 0)
            initialRadius = typedArray.getInteger(R.styleable.BlurImageView_radius, 0)
            typedArray.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Terapkan blur saat drawable sudah tersedia
        if (!appliedBlur && drawable != null) {
            imageOnView = drawable
            setBlur(initialRadius)
            appliedBlur = true
        }
    }

    /**
     * Simpan referensi gambar asli.
     */
    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (imageOnView == null) imageOnView = drawable
    }

    /**
     * Atur skala bitmap sebelum diproses blur.
     */
    fun setBitmapScale(scale: Float) {
        defaultBitmapScale = scale
    }

    /**
     * Terapkan efek blur dengan radius tertentu.
     * Radius 0 akan mengembalikan gambar asli dan menghapus efek blur.
     */
    fun setBlur(radius: Int) {
        val drawableBitmap = (imageOnView ?: drawable) as? BitmapDrawable ?: return
        val bitmap = drawableBitmap.bitmap

        if (radius == 0) {
            setImageDrawable(imageOnView)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                this.setRenderEffect(null)
            }
            return
        }

        if (radius in (minRadius + 1)..maxRadius) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setImageDrawable(BitmapDrawable(resources, bitmap))
                applyViewBlurEffect(radius)
            } else {
                val blurred = fastBlurLegacy(bitmap, radius)
                setImageBitmap(blurred)
            }
        }
    }

    /**
     * Terapkan RenderEffect pada View (API 31 ke atas).
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun applyViewBlurEffect(radius: Int) {
        val effect = RenderEffect.createBlurEffect(
            radius.toFloat(),
            radius.toFloat(),
            Shader.TileMode.CLAMP
        )
        this.setRenderEffect(effect)
    }

    /**
     * Fallback blur untuk API < 31.
     */
    private fun fastBlurLegacy(bitmap: Bitmap, radius: Int): Bitmap {
        val scaled = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * defaultBitmapScale).roundToInt(),
            (bitmap.height * defaultBitmapScale).roundToInt(),
            true
        ).copy(Bitmap.Config.ARGB_8888, true)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val canvas = Canvas(scaled)
        paint.maskFilter = BlurMaskFilter(radius.toFloat(), BlurMaskFilter.Blur.NORMAL)
        canvas.drawBitmap(scaled, 0f, 0f, paint)

        return scaled
    }
}

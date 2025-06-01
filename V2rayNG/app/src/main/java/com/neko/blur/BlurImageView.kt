package com.neko.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.neko.v2ray.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class BlurImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    private var blurRadius: Int = 15
    private var blurScale: Float = 0.25f

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BlurImageView,
            0, 0
        ).apply {
            try {
                blurRadius = getFloat(R.styleable.BlurImageView_blurRadius, 15f).toInt()
                blurScale = getFloat(R.styleable.BlurImageView_blurScale, 0.25f)
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blur()
    }

    fun setBlurRadius(radius: Int) {
        blurRadius = radius
        blur()
    }

    fun setBlurScale(scale: Float) {
        blurScale = scale
        blur()
    }

    private fun blur() {
        val drawable = drawable ?: return
        val originalBitmap = (drawable as? BitmapDrawable)?.bitmap ?: return

        val scaledWidth = (originalBitmap.width * blurScale).roundToInt()
        val scaledHeight = (originalBitmap.height * blurScale).roundToInt()
        if (scaledWidth <= 0 || scaledHeight <= 0) return

        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, true)
        val blurredBitmap = fastBlur(scaledBitmap, blurRadius)
        setImageBitmap(blurredBitmap)
    }

    private fun fastBlur(sentBitmap: Bitmap, radius: Int): Bitmap {
        val bitmap = sentBitmap.copy(sentBitmap.config ?: Bitmap.Config.ARGB_8888, true)

        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)

        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1

        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        val vmin = IntArray(max(w, h))

        var rsum: Int
        var gsum: Int
        var bsum: Int
        var p: Int
        var yp: Int
        var yi = 0

        val dv = IntArray(256 * div)
        for (i in dv.indices) dv[i] = i / div

        var yw = 0
        for (y in 0 until h) {
            rsum = 0; gsum = 0; bsum = 0
            for (i in -radius..radius) {
                val offset = min(wm, max(i, 0))
                val idx = yi + offset
                p = pix.getOrElse(idx) { 0 }
                rsum += (p shr 16) and 0xff
                gsum += (p shr 8) and 0xff
                bsum += p and 0xff
            }
            for (x in 0 until w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]

                if (y == 0) vmin[x] = min(x + radius + 1, wm)
                val p1 = pix[yw + vmin[x]]
                val p2 = pix[yw + max(x - radius, 0)]

                rsum += ((p1 shr 16) and 0xff) - ((p2 shr 16) and 0xff)
                gsum += ((p1 shr 8) and 0xff) - ((p2 shr 8) and 0xff)
                bsum += (p1 and 0xff) - (p2 and 0xff)

                yi++
            }
            yw += w
        }

        for (x in 0 until w) {
            rsum = 0; gsum = 0; bsum = 0
            yp = -radius * w
            for (i in -radius..radius) {
                val yIndex = max(0, yp) + x
                rsum += r.getOrElse(yIndex) { 0 }
                gsum += g.getOrElse(yIndex) { 0 }
                bsum += b.getOrElse(yIndex) { 0 }
                yp += w
            }
            yi = x
            for (y in 0 until h) {
                pix[yi] = (0xff shl 24) or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                if (x == 0) vmin[y] = min(y + radius + 1, hm) * w
                val p1 = x + vmin[y]
                val p2 = x + max(y - radius, 0) * w

                rsum += r.getOrElse(p1) { 0 } - r.getOrElse(p2) { 0 }
                gsum += g.getOrElse(p1) { 0 } - g.getOrElse(p2) { 0 }
                bsum += b.getOrElse(p1) { 0 } - b.getOrElse(p2) { 0 }

                yi += w
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }
}

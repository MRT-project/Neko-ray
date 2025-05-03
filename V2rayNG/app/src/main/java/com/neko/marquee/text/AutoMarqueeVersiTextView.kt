package com.neko.marquee.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.neko.v2ray.BuildConfig

class AutoMarqueeVersiTextView : AppCompatTextView {

    private var memekVersion: String? = null

    private fun jupokInfoSlur() {
        text = BuildConfig.VERSION_NAME
    }

    override fun isFocused(): Boolean {
        return true
    }

    constructor(context: Context) : super(context) {
        jupokInfoSlur()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        jupokInfoSlur()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        jupokInfoSlur()
    }
}

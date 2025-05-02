package com.neko.marquee.deviceinfo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * A custom TextView that displays the device name and is always focused.
 */
class Device : AppCompatTextView {

    private var memekVersion: String? = null

    /**
     * Initializes the view by setting the device name as text.
     */
    private fun jupokInfoSlur() {
        text = Build.DEVICE
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

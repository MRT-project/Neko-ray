package com.neko.marquee.deviceinfo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * A custom TextView that always remains focused and displays the hardware information of the device.
 */
class Hardware : AppCompatTextView {

    private var memekVersion: String? = null

    /**
     * Ensures the view always appears focused.
     * @return true, indicating this view is always focused.
     */
    override fun isFocused(): Boolean {
        return true
    }

    /**
     * Retrieves and displays the device's hardware information.
     */
    private fun displayHardwareInfo() {
        text = Build.HARDWARE
    }

    // Primary constructor
    constructor(context: Context) : super(context) {
        displayHardwareInfo()
    }

    // Constructor with attributes
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        displayHardwareInfo()
    }

    // Constructor with attributes and default style
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        displayHardwareInfo()
    }
}

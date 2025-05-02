package com.neko.marquee.deviceinfo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Custom TextView that always appears focused and displays the Android OS version.
 */
class OsVersion : AppCompatTextView {

    private var memekVersion: String? = null

    /**
     * Ensures the view always appears focused.
     */
    override fun isFocused(): Boolean {
        return true
    }

    /**
     * Sets the text to display the Android OS version.
     */
    private fun setOsVersionInfo() {
        text = Build.VERSION.RELEASE
    }

    /**
     * Primary constructor to initialize the view and set the OS version info.
     */
    constructor(context: Context) : super(context) {
        setOsVersionInfo()
    }

    /**
     * Constructor with attributes, initializes the view and sets OS version info.
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setOsVersionInfo()
    }

    /**
     * Constructor with attributes and style, initializes the view and sets OS version info.
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setOsVersionInfo()
    }
}

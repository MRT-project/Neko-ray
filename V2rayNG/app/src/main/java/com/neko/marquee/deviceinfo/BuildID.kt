package com.neko.marquee.deviceinfo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Custom TextView that always appears focused and displays the device's Build ID.
 */
class BuildID : AppCompatTextView {

    /**
     * Primary constructor used when creating this view programmatically.
     */
    constructor(context: Context) : super(context) {
        displayBuildID()
    }

    /**
     * Constructor used when inflating this view from XML with attributes.
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        displayBuildID()
    }

    /**
     * Constructor used when inflating this view from XML with a style attribute.
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        displayBuildID()
    }

    /**
     * Ensures the view always appears focused.
     * Useful for marquee effects.
     */
    override fun isFocused(): Boolean {
        return true
    }

    /**
     * Sets the text of the TextView to display the device's Build ID.
     */
    private fun displayBuildID() {
        text = Build.ID
    }
}

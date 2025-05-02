package com.neko.marquee.deviceinfo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Custom TextView that always stays focused and displays the device brand.
 */
class Brand : AppCompatTextView {
    
    /**
     * Constructor for initializing the view programmatically.
     */
    constructor(context: Context) : super(context) {
        init()
    }

    /**
     * Constructor for initializing the view from XML with attributes.
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    /**
     * Constructor for initializing the view with a defined style.
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    /**
     * Ensures that this view always appears focused.
     * This is useful for marquee effects.
     */
    override fun isFocused(): Boolean {
        return true
    }

    /**
     * Initializes the TextView with the device brand name.
     */
    private fun init() {
        text = Build.BRAND
    }
}

package com.neko.marquee.deviceinfo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class Baseband : AppCompatTextView {

    private var memekVersion: String? = null

    override fun isFocused(): Boolean {
        return true
    }

    private fun jupokInfoSlur() {
        text = Build.getRadioVersion()
    }

    constructor(context: Context) : super(context) {
        jupokInfoSlur()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        jupokInfoSlur()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        jupokInfoSlur()
    }
}

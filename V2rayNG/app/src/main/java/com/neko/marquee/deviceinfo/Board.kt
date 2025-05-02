package com.neko.marquee.deviceinfo

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class Board : AppCompatTextView {
    private var memekVersion: String? = null

    private fun jupokInfoSlur() {
        text = Build.BOARD
    }

    override fun isFocused(): Boolean {
        return true
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

package com.neko.v2ray.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.neko.themeengine.ThemeEngine
import com.neko.v2ray.R

class ThemePreviewPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : Preference(context, attrs) {

    private val themeEngine = ThemeEngine.getInstance(context)

    init {
        summary = themeEngine.staticTheme.name.replaceFirstChar(Char::uppercaseChar)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val imageView = holder.findViewById(R.id.circlePreview) as? ImageView ?: return
        val color = context.getColor(themeEngine.staticTheme.primaryColor)

        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(color)
            setSize(72, 72)
            setStroke(2, ContextCompat.getColor(context, android.R.color.darker_gray))
        }

        imageView.setImageDrawable(drawable)
    }
}

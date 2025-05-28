package com.neko.themeengine

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat

class FontContextWrapper(base: Context) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context): Context {
            val appFont = ThemeEngine.getInstance(context).appFont
        
            return if (appFont == AppFont.DEFAULT) {
                resetSystemFont()
                context
            } else {
                val typeface = ResourcesCompat.getFont(context, appFont.fontRes)
                typeface?.let { overrideSystemFonts(it) }
                FontContextWrapper(context)
            }
        }

        private fun overrideSystemFonts(typeface: Typeface) {
            try {
                val defaultFontField = Typeface::class.java.getDeclaredField("sSystemFontMap")
                defaultFontField.isAccessible = true
                val map = defaultFontField.get(null) as? MutableMap<String, Typeface>
                map?.keys?.forEach { map[it] = typeface }

                // Override field bawaan Typeface
                overrideTypefaceField("DEFAULT", typeface)
                overrideTypefaceField("DEFAULT_BOLD", typeface)
                overrideTypefaceField("SANS_SERIF", typeface)
                overrideTypefaceField("SERIF", typeface)
                overrideTypefaceField("MONOSPACE", typeface)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun overrideTypefaceField(fieldName: String, typeface: Typeface) {
            try {
                val field = Typeface::class.java.getDeclaredField(fieldName)
                field.isAccessible = true
                field.set(null, typeface)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun resetSystemFont() {
            try {
                // Kembalikan default font bawaan sistem
                overrideTypefaceField("DEFAULT", Typeface.DEFAULT)
                overrideTypefaceField("DEFAULT_BOLD", Typeface.DEFAULT_BOLD)
                overrideTypefaceField("SANS_SERIF", Typeface.SANS_SERIF)
                overrideTypefaceField("SERIF", Typeface.SERIF)
                overrideTypefaceField("MONOSPACE", Typeface.MONOSPACE)
        
                val defaultFontField = Typeface::class.java.getDeclaredField("sSystemFontMap")
                defaultFontField.isAccessible = true
                val original = defaultFontField.get(null) as? MutableMap<String, Typeface>
                original?.clear() // kosongkan supaya sistem pakai default
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

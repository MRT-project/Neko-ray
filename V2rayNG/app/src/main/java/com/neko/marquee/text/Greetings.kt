package com.neko.marquee.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.Calendar

/**
 * A custom TextView that displays a greeting message based on the time of day
 * and the device's language setting.
 */
class Greetings : AppCompatTextView {

    constructor(context: Context) : super(context) {
        updateGreeting()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        updateGreeting()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        updateGreeting()
    }

    override fun isFocused(): Boolean {
        return true // Ensures marquee effect works by making the TextView always appear focused
    }

    /**
     * Updates the text with a greeting message based on the current time and language.
     */
    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val language = resources.configuration.locales[0].language

        val greeting = when (language) {
            "ar", "fa" -> getArabicGreeting(hour)
            "in" -> getIndonesianGreeting(hour)
            "ja" -> getJapaneseGreeting(hour)
            "jw" -> getJavaneseGreeting(hour)
            "ru" -> getRussianGreeting(hour)
            "su" -> getSundaneseGreeting(hour)
            "vi" -> getVietnameseGreeting(hour)
            "zh", "CN" -> getChineseGreeting(hour)
            "zh", "TW" -> getTaiwaneseGreeting(hour)
            "bn" -> getBengaliGreeting(hour)
            "tr" -> getTurkishGreeting(hour)
            else -> getEnglishGreeting(hour)
        }
        text = greeting
    }

    /** Helper methods to get greetings in different languages **/
    private fun getEnglishGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 Good Morning..."
        in 9..15 -> "⛅ Good Afternoon..."
        in 16..20 -> "🌥️ Good Evening..."
        in 21..23 -> "🌙 Good Night..."
        else -> "💤 It's time to go to sleep..."
    }

    private fun getArabicGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 صباح الخير..."
        in 9..15 -> "⛅ مساء الخير..."
        in 16..20 -> "🌥️ مساء الخير..."
        in 21..23 -> "🌙 طاب مساؤك..."
        else -> "💤 حان الوقت للذهاب الى النوم..."
    }

    private fun getIndonesianGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 Selamat Pagi..."
        in 9..15 -> "⛅ Selamat Siang..."
        in 16..20 -> "🌥️ Selamat Sore..."
        in 21..23 -> "🌙 Selamat Malam..."
        else -> "💤 Waktunya Tidur..."
    }

    private fun getJapaneseGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 おはよう..."
        in 9..15 -> "⛅ こんにちは..."
        in 16..20 -> "🌥️ こんばんは..."
        in 21..23 -> "🌙 おやすみ..."
        else -> "💤 寝る時間だよ..."
    }

    private fun getJavaneseGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 sugeng enjang..."
        in 9..15 -> "⛅ sugeng siang..."
        in 16..20 -> "🌥️ sugeng sonten..."
        in 21..23 -> "🌙 sugeng dalu..."
        else -> "💤 Wis wayahe turu..."
    }

    private fun getRussianGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 Доброе утро..."
        in 9..15 -> "⛅ Добрый день..."
        in 16..20 -> "🌥️ Добрый вечер..."
        in 21..23 -> "🌙 Спокойной ночи..."
        else -> "💤 Пора идти спать..."
    }

    private fun getSundaneseGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 Wilujeng énjing..."
        in 9..15 -> "⛅ Wilujeng siang..."
        in 16..20 -> "🌥️ Wilujeng sonten..."
        in 21..23 -> "🌙 Wilujeng wengi..."
        else -> "💤 Wanci saré..."
    }

    private fun getVietnameseGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 Chào buổi sáng..."
        in 9..15 -> "⛅ Chào buổi chiều..."
        in 16..20 -> "🌥️ Chào buổi chiều..."
        in 21..23 -> "🌙 Chúc ngủ ngon..."
        else -> "💤 Đã đến giờ đi ngủ..."
    }

    private fun getChineseGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 早上好..."
        in 9..15 -> "⛅ 下午好..."
        in 16..20 -> "🌥️ 下午好..."
        in 21..23 -> "🌙 晚安..."
        else -> "💤 是时候去睡觉了..."
    }

    private fun getTaiwaneseGreeting(hour: Int) = getChineseGreeting(hour)

    private fun getBengaliGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 শুভ সকাল..."
        in 9..15 -> "⛅ শুভ বিকাল..."
        in 16..20 -> "🌥️ শুভ সন্ধ্যা..."
        in 21..23 -> "🌙 শুভ রাত্রি..."
        else -> "💤 ঘুমাতে যাওয়ার সময় হয়েছে..."
    }

    private fun getTurkishGreeting(hour: Int) = when (hour) {
        in 4..8 -> "🌤 Günaydın..."
        in 9..15 -> "⛅ Tünaydın..."
        in 16..20 -> "🌥️ İyi akşamlar..."
        in 21..23 -> "🌙 İyi geceler..."
        else -> "💤 Uyuma zamanı geldi..."
    }
}

package com.neko.themeengine

import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import com.neko.v2ray.R

enum class ContrastLevel { DEFAULT, MEDIUM, HIGH }

enum class Theme(
    @StyleRes val themeId: Int,
    @ColorRes val primaryColor: Int
) {
    Amber(R.style.Theme_ThemeEngine_Amber, R.color.amber_theme_primary),
    AmberMediumContrast(R.style.Theme_ThemeEngine_Amber_MediumContrast, R.color.amber_theme_primary_mediumContrast),
    AmberHighContrast(R.style.Theme_ThemeEngine_Amber_HighContrast, R.color.amber_theme_primary_highContrast),

    Blue(R.style.Theme_ThemeEngine_Blue, R.color.blue_theme_primary),
    BlueMediumContrast(R.style.Theme_ThemeEngine_Blue_MediumContrast, R.color.blue_theme_primary_mediumContrast),
    BlueHighContrast(R.style.Theme_ThemeEngine_Blue_HighContrast, R.color.blue_theme_primary_highContrast),

    Brown(R.style.Theme_ThemeEngine_Brown, R.color.brown_theme_primary),
    BrownMediumContrast(R.style.Theme_ThemeEngine_Brown_MediumContrast, R.color.brown_theme_primary_mediumContrast),
    BrownHighContrast(R.style.Theme_ThemeEngine_Brown_HighContrast, R.color.brown_theme_primary_highContrast),

    Cyan(R.style.Theme_ThemeEngine_Cyan, R.color.cyan_theme_primary),
    CyanMediumContrast(R.style.Theme_ThemeEngine_Cyan_MediumContrast, R.color.cyan_theme_primary_mediumContrast),
    CyanHighContrast(R.style.Theme_ThemeEngine_Cyan_HighContrast, R.color.cyan_theme_primary_highContrast),

    DeepOrange(R.style.Theme_ThemeEngine_DeepOrange, R.color.deep_orange_theme_primary),
    DeepOrangeMediumContrast(R.style.Theme_ThemeEngine_DeepOrange_MediumContrast, R.color.deep_orange_theme_primary_mediumContrast),
    DeepOrangeHighContrast(R.style.Theme_ThemeEngine_DeepOrange_HighContrast, R.color.deep_orange_theme_primary_highContrast),

    DeepPurple(R.style.Theme_ThemeEngine_DeepPurple, R.color.deep_purple_theme_primary),
    DeepPurpleMediumContrast(R.style.Theme_ThemeEngine_DeepPurple_MediumContrast, R.color.deep_purple_theme_primary_mediumContrast),
    DeepPurpleHighContrast(R.style.Theme_ThemeEngine_DeepPurple_HighContrast, R.color.deep_purple_theme_primary_highContrast),

    Green(R.style.Theme_ThemeEngine_Green, R.color.green_theme_primary),
    GreenMediumContrast(R.style.Theme_ThemeEngine_Green_MediumContrast, R.color.green_theme_primary_mediumContrast),
    GreenHighContrast(R.style.Theme_ThemeEngine_Green_HighContrast, R.color.green_theme_primary_highContrast),

    Indigo(R.style.Theme_ThemeEngine_Indigo, R.color.indigo_theme_primary),
    IndigoMediumContrast(R.style.Theme_ThemeEngine_Indigo_MediumContrast, R.color.indigo_theme_primary_mediumContrast),
    IndigoHighContrast(R.style.Theme_ThemeEngine_Indigo_HighContrast, R.color.indigo_theme_primary_highContrast),

    LightBlue(R.style.Theme_ThemeEngine_LightBlue, R.color.light_blue_theme_primary),
    LightBlueMediumContrast(R.style.Theme_ThemeEngine_LightBlue_MediumContrast, R.color.light_blue_theme_primary_mediumContrast),
    LightBlueHighContrast(R.style.Theme_ThemeEngine_LightBlue_HighContrast, R.color.light_blue_theme_primary_highContrast),

    Lime(R.style.Theme_ThemeEngine_Lime, R.color.lime_theme_primary),
    LimeMediumContrast(R.style.Theme_ThemeEngine_Lime_MediumContrast, R.color.lime_theme_primary_mediumContrast),
    LimeHighContrast(R.style.Theme_ThemeEngine_Lime_HighContrast, R.color.lime_theme_primary_highContrast),

    Orange(R.style.Theme_ThemeEngine_Orange, R.color.orange_theme_primary),
    OrangeMediumContrast(R.style.Theme_ThemeEngine_Orange_MediumContrast, R.color.orange_theme_primary_mediumContrast),
    OrangeHighContrast(R.style.Theme_ThemeEngine_Orange_HighContrast, R.color.orange_theme_primary_highContrast),

    Pink(R.style.Theme_ThemeEngine_Pink, R.color.pink_theme_primary),
    PinkMediumContrast(R.style.Theme_ThemeEngine_Pink_MediumContrast, R.color.pink_theme_primary_mediumContrast),
    PinkHighContrast(R.style.Theme_ThemeEngine_Pink_HighContrast, R.color.pink_theme_primary_highContrast),

    Purple(R.style.Theme_ThemeEngine_Purple, R.color.purple_theme_primary),
    PurpleMediumContrast(R.style.Theme_ThemeEngine_Purple_MediumContrast, R.color.purple_theme_primary_mediumContrast),
    PurpleHighContrast(R.style.Theme_ThemeEngine_Purple_HighContrast, R.color.purple_theme_primary_highContrast),

    Red(R.style.Theme_ThemeEngine_Red, R.color.red_theme_primary),
    RedMediumContrast(R.style.Theme_ThemeEngine_Red_MediumContrast, R.color.red_theme_primary_mediumContrast),
    RedHighContrast(R.style.Theme_ThemeEngine_Red_HighContrast, R.color.red_theme_primary_highContrast),

    Teal(R.style.Theme_ThemeEngine_Teal, R.color.teal_theme_primary),
    TealMediumContrast(R.style.Theme_ThemeEngine_Teal_MediumContrast, R.color.teal_theme_primary_mediumContrast),
    TealHighContrast(R.style.Theme_ThemeEngine_Teal_HighContrast, R.color.teal_theme_primary_highContrast),

    Violet(R.style.Theme_ThemeEngine_Violet, R.color.violet_theme_primary),
    VioletMediumContrast(R.style.Theme_ThemeEngine_Violet_MediumContrast, R.color.violet_theme_primary_mediumContrast),
    VioletHighContrast(R.style.Theme_ThemeEngine_Violet_HighContrast, R.color.violet_theme_primary_highContrast),

    Yellow(R.style.Theme_ThemeEngine_Yellow, R.color.yellow_theme_primary),
    YellowMediumContrast(R.style.Theme_ThemeEngine_Yellow_MediumContrast, R.color.yellow_theme_primary_mediumContrast),
    YellowHighContrast(R.style.Theme_ThemeEngine_Yellow_HighContrast, R.color.yellow_theme_primary_highContrast),

    Chartreuse(R.style.Theme_ThemeEngine_Chartreuse, R.color.chartreuse_theme_primary),
    ChartreuseMediumContrast(R.style.Theme_ThemeEngine_Chartreuse_MediumContrast, R.color.chartreuse_theme_primary_mediumContrast),
    ChartreuseHighContrast(R.style.Theme_ThemeEngine_Chartreuse_HighContrast, R.color.chartreuse_theme_primary_highContrast),

    BlueGrey(R.style.Theme_ThemeEngine_BlueGrey, R.color.blue_grey_theme_primary),
    BlueGreyMediumContrast(R.style.Theme_ThemeEngine_BlueGrey_MediumContrast, R.color.blue_grey_theme_primary_mediumContrast),
    BlueGreyHighContrast(R.style.Theme_ThemeEngine_BlueGrey_HighContrast, R.color.blue_grey_theme_primary_highContrast),

    Grey(R.style.Theme_ThemeEngine_Grey, R.color.grey_theme_primary),
    GreyMediumContrast(R.style.Theme_ThemeEngine_Grey_MediumContrast, R.color.grey_theme_primary_mediumContrast),
    GreyHighContrast(R.style.Theme_ThemeEngine_Grey_HighContrast, R.color.grey_theme_primary_highContrast);

    val baseName: String
        get() = name.removeSuffix("MediumContrast").removeSuffix("HighContrast")

    val contrastLevel: ContrastLevel
        get() = when {
            name.endsWith("MediumContrast") -> ContrastLevel.MEDIUM
            name.endsWith("HighContrast") -> ContrastLevel.HIGH
            else -> ContrastLevel.DEFAULT
        }

    fun getContrastTheme(level: ContrastLevel): Theme {
        return values().firstOrNull {
            it.baseName == this.baseName && it.contrastLevel == level
        } ?: this
    }
}

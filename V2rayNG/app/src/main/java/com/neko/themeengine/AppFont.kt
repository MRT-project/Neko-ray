package com.neko.themeengine

import androidx.annotation.FontRes
import com.neko.v2ray.R

enum class AppFont(@FontRes val fontRes: Int, val displayName: String) {
    DEFAULT(0, "Default"),
    ROBOTO(R.font.roboto_regular, "Roboto"),
    LATO(R.font.lato_regular, "Lato"),
    MONTSERRAT(R.font.montserrat_thin, "Montserrat"),
    STENCIL(R.font.semibold_stencil, "Stencil"),
    POPPINS(R.font.poppins, "Poppins"),
    BALENTIA(R.font.balentia, "Balentia"),
    DREAMHOUR(R.font.dreamhour, "Dreamhour"),
    GRUNELL(R.font.grunell, "Grunell"),
    SHIROUGA(R.font.shirouga, "Shirouga"),
    YOMOGI(R.font.yomogi, "Yomogi")
}

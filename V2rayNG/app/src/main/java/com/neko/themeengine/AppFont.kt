package com.neko.themeengine

import androidx.annotation.FontRes
import com.neko.v2ray.R

enum class AppFont(@FontRes val fontRes: Int, val displayName: String) {
    DEFAULT(0, "Default"),
    ROBOTO(R.font.roboto_regular, "Roboto"),
    LATO(R.font.lato_regular, "Lato"),
    WINTERING(R.font.wintering, "Wintering"),
    STENCIL(R.font.semibold_stencil, "Stencil"),
    POPPINS(R.font.poppins, "Poppins"),
    MISTERY(R.font.mistery, "Mistery"),
    DREAMHOUR(R.font.dreamhour, "Dreamhour"),
    GRUNELL(R.font.grunell, "Grunell"),
    SHIROUGA(R.font.shirouga, "Shirouga"),
    BUBBLEZ(R.font.bubblez, "Bubblez"),
    JAPAN(R.font.japan, "Japan"),
    ONEPIECE(R.font.onepiece, "OnePiece")
}

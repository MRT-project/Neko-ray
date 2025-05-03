package com.neko.v2ray.dto

data class IPAPIInfo(
    var ip: String? = null,
    var city: String? = null,
    var region: String? = null,
    var region_code: String? = null,
    var country: String? = null,
    var country_name: String? = null,
    var country_code: String? = null
)
package com.dentalhelpinghands.models.office.loginModel

import java.io.Serializable

data class ResponseObjectModel(
    val ResponseCode: Int,
    val ResponseMsg: String,
    val Result: String,
    val ServerTime: String,
    val `data`: DataX
): Serializable
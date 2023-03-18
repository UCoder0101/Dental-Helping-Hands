package com.dentalhelpinghands.models.office.loginModel

import java.io.Serializable

data class ResponseArrayModel(
    val ResponseCode: Int,
    val ResponseMsg: String,
    val Result: String,
    val ServerTime: String,
    val `data`: MutableList<DataX>,
    val `job_apply_master`: MutableList<DataX>,
    ): Serializable

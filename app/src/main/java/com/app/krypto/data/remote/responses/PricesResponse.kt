package com.app.krypto.data.remote.responses

import com.google.gson.annotations.SerializedName

data class PricesResponse(
    @SerializedName("data")val priceData: PriceData,
    val status: String
)
package com.app.krypto.data.remote.responses

import com.app.krypto.data.local.entities.Price

data class PriceData(
    val network: String,
    val prices: List<Price>
)
package com.app.krypto.data.remote.responses

import com.app.krypto.data.local.entities.Balance
import com.google.gson.annotations.SerializedName

data class BalanceResponse(
    @SerializedName("data") val balance: Balance,
    val status: String
)



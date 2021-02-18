package com.app.krypto.data.remote.responses

data class DataX(
    val addresses: List<Address>,
    val has_more: Boolean,
    val network: String,
    val page: Int
)
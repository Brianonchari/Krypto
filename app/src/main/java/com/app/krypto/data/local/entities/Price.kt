package com.app.krypto.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Price(
    val exchange: String,
    val price: String,
    val price_base: String,
    val time: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int
)
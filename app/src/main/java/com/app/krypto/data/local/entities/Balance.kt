package com.app.krypto.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ID = 0
@Entity
data class Balance(
    val available_balance: String,
    val network: String,
    val pending_received_balance: String,
    @PrimaryKey(autoGenerate = false)
    var id: Int = ID
)

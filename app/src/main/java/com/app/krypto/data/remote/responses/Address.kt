package com.app.krypto.data.remote.responses

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *Created by Brian Onchari on 13/01/2021.
 */
@Entity(tableName = "address")
data class Address(
    val address: String,
    val available_balance: String,
    val is_segwit: Boolean,
    val label: String,
    val pending_received_balance: String,
    @PrimaryKey(autoGenerate = false)
    val user_id: Int
)
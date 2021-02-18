package com.app.krypto.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.krypto.data.local.daos.AddressDao
import com.app.krypto.data.local.daos.BalanceDao
import com.app.krypto.data.local.daos.PriceDao
import com.app.krypto.data.local.entities.Balance
import com.app.krypto.data.local.entities.Price
import com.app.krypto.data.remote.responses.Address

/**
 *Created by Brian Onchari on 28/01/2021.
 */

@Database(
    entities = [Balance::class, Address::class, Price::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase():RoomDatabase() {

    abstract fun balanceDao():BalanceDao
    abstract fun addressDao():AddressDao
    abstract fun priceDao():PriceDao


}
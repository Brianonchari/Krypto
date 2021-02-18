package com.app.krypto.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.krypto.data.local.entities.Price

/**
 *Created by Brian Onchari on 12/02/2021.
 */
@Dao
interface PriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrice(price: Price)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(price: List<Price>)

    @Query("SELECT * FROM Price")
     fun observeAllPrices(): LiveData<List<Price>>
}
package com.app.krypto.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.krypto.data.local.entities.Balance

/**
 *Created by Brian Onchari on 28/01/2021.
 */

@Dao
interface BalanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: Balance)

    @Delete
    suspend fun delete(balance: Balance)

    @Query("SELECT * FROM Balance")
     fun getBalance(): LiveData<Balance>
    @Query("SELECT available_balance FROM BALANCE")
    fun observeAvailableBalance():LiveData<String>
    @Query("SELECT pending_received_balance FROM Balance")
    fun observePendingRcvBalance():LiveData<String>

    @Query("SELECT * FROM Balance")
    fun observeBalance(): LiveData<Balance>



}
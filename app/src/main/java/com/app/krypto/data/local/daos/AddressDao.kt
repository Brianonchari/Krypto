package com.app.krypto.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.krypto.data.remote.responses.Address

/**
 *Created by Brian Onchari on 30/01/2021.
 */
@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddresses(address: List<Address>)

    @Delete
    suspend fun deleteAddress(address: Address)

    @Query("SELECT * FROM Address")
    fun observeAllAddresses(): LiveData<List<Address>>
}
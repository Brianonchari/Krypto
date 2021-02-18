package com.app.krypto.repositories

import androidx.lifecycle.LiveData
import com.app.krypto.data.local.entities.Balance
import com.app.krypto.data.local.entities.Price
import com.app.krypto.data.remote.responses.*
import com.app.krypto.utils.Resource

/**
 *Created by Brian Onchari on 19/01/2021.
 */
interface BlockIORepository {
    suspend fun getBalance(): Resource<BalanceResponse>
    suspend fun getBitcoinBasePrices():Resource<PricesResponse>
    suspend fun getAddresses(): Resource<AddressesResponse>
    suspend fun insertBalance(balance: Balance)
    suspend fun getCachedBalance(): Balance

    suspend fun createNewAddress(label:String):Resource<CreateAddressResponse>
    suspend fun insertAddresses(address:List<Address>)
    suspend fun insertAddress(address: Address)
    suspend fun insertPrice(price: Price)
    suspend fun insertPrices(price:List<Price>)
    suspend fun deleteAddress(address: Address)
    fun observeAllAddresses():LiveData<List<Address>>
    fun observeAvailableBalance():LiveData<String>
    fun observePendingRcvdBalance():LiveData<String>
    fun observeAllPrices():LiveData<List<Price>>
    fun observeBalance():LiveData<Balance>
}
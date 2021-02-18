package com.app.krypto.repositories

import androidx.lifecycle.LiveData
import com.app.krypto.data.local.entities.Balance
import com.app.krypto.data.local.entities.Price
import com.app.krypto.data.remote.responses.*
import com.app.krypto.utils.Resource

/**
 *Created by Brian Onchari on 01/02/2021.
 */
class FakeKryptoRepositoryAndroidTest:BlockIORepository {

    override suspend fun getBalance(): Resource<BalanceResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertBalance(balance: Balance) {
        TODO("Not yet implemented")
    }

    override suspend fun getCachedBalance(): Balance {
        TODO("Not yet implemented")
    }

    override suspend fun getAddresses(): Resource<AddressesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAddresses(address: List<Address>) {
        TODO("Not yet implemented")
    }

    override fun observeAllAddresses(): LiveData<List<Address>> {
        TODO("Not yet implemented")
    }

    override fun observeAvailableBalance(): LiveData<String> {
        TODO("Not yet implemented")
    }

    override fun observePendingRcvdBalance(): LiveData<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getBitcoinBasePrices(): Resource<PricesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAddress(address: Address) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPrice(price: Price) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPrices(price: List<Price>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAddress(address: Address) {
        TODO("Not yet implemented")
    }

    override fun observeAllPrices(): LiveData<List<Price>> {
        TODO("Not yet implemented")
    }

    override suspend fun createNewAddress(label: String): Resource<CreateAddressResponse> {
        TODO("Not yet implemented")
    }

    override fun observeBalance(): LiveData<Balance> {
        TODO("Not yet implemented")
    }
}
package com.app.krypto.repositories

import com.app.krypto.data.remote.responses.BalanceResponse
import com.app.krypto.data.local.entities.Balance
import com.app.krypto.data.remote.responses.DataX
import com.app.krypto.utils.Resource

/**
 *Created by Brian Onchari on 19/01/2021.
 * Fake test double repository for testing viewmodel. To simulate behaviour of
 * a real repository.
 */
class FakeBlockIORepository: BlockIORepository {
    private var shouldReturnNetworkError = false
    fun shouldReturnNetworkError(value:Boolean){
        shouldReturnNetworkError = value
    }

    override suspend fun getBalance(): Resource<BalanceResponse> {
        return if(shouldReturnNetworkError){
            Resource.error("Error",null)
        }else{
            Resource.success(BalanceResponse(balance = Balance("","",""),status = ""))
        }
    }

    override suspend fun getAddresses(): Resource<DataX> {
        TODO("Not yet implemented")
    }
}
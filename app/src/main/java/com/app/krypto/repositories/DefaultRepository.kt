package com.app.krypto.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.krypto.data.local.daos.AddressDao
import com.app.krypto.data.local.daos.BalanceDao
import com.app.krypto.data.local.daos.PriceDao
import com.app.krypto.data.local.entities.Balance
import com.app.krypto.data.local.entities.Price
import com.app.krypto.data.remote.BlockIOApi
import com.app.krypto.data.remote.responses.*
import com.app.krypto.utils.Resource
import javax.inject.Inject

/**
 *Created by Brian Onchari on 19/01/2021.
 */
class DefaultRepository @Inject constructor(
    private val blockIOApi: BlockIOApi,
    private val balanceDao: BalanceDao,
    private val addressDao: AddressDao,
    private val priceDao: PriceDao

) : BlockIORepository {
    override suspend fun getBalance(): Resource<BalanceResponse> {
        return try {
            val response = blockIOApi.getBalance()
            if (response.isSuccessful) {
                Log.d("TAG", "getBalance: Response >> ${response.body()}")
                response.body().let {
                    return@let Resource.success(it)
                }
            } else {
                Log.d("TAG", "getBalance: Error Response >>> ${response.message()}")
                Resource.error("An unknown error occured", null)
            }
        } catch (ex: Exception) {
            Resource.error("Could not reach the server.Check your internet connection", null)
        }
    }

    override suspend fun getAddresses(): Resource<AddressesResponse> {
        return try {
            val response = blockIOApi.getAddresses()
            Log.d("TAG", "getAddresses:>>>${response.body()} ")
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (ex: Exception) {
            Resource.error("Could not reach the server.Check your internet connection", null)
        }
    }

    override suspend fun getBitcoinBasePrices(): Resource<PricesResponse> {
        return try {
            val response = blockIOApi.getBitcoinBasePrice()
            Log.d("TAG", "getBitcoinBasePrices: ${response.body()}")
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?:Resource.error("Unknown Error occured",null)
            }else{
                Resource.error("Unknown error occured",null)
            }
        }catch (ex:Exception){
            Resource.error("Could not reach the server.Check your internet connection", null)
        }
    }

    override suspend fun createNewAddress(label:String): Resource<CreateAddressResponse> {
        return try {
            val response = blockIOApi.createAddress(Data(label))
            Log.d("TAG", "createNewAddress: $response")
            if(response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                }?:Resource.error("Unknown Error occured",null)
            }else{
                Resource.error("Unknown error occured",null)
            }

        }catch (e:java.lang.Exception){
            Resource.error("Could not reach the server.Check your internet connection", null)
        }
    }

    override suspend fun insertPrice(price: Price) {
        return priceDao.insertPrice(price)
    }

    override suspend fun insertPrices(price: List<Price>) {
     return priceDao.insertPrices(price)
    }

    override fun observeAllPrices(): LiveData<List<Price>> {
       return priceDao.observeAllPrices()
    }

    override suspend fun insertBalance(balance: Balance) {
        balanceDao.insertBalance(balance)
    }

    //    override suspend fun getCachedBalance(): Balance {
//        return balanceDao.getBalance()
//    }
//    override  fun getCachedBalance(): Balance {
//        return balanceDao.getBalance()
//    }
//    override fun getCachedBalance(): Balance {
//        TODO("Not yet implemented")
//    }
    override fun getCachedBalance(): LiveData<Balance> {
        return balanceDao.getBalance()
    }

    override suspend fun insertAddresses(address: List<Address>) {
        addressDao.insertAddresses(address)
    }

    override suspend fun insertAddress(address: Address) {
        addressDao.insertAddress(address)
    }

    override suspend fun deleteAddress(address: Address) {
        addressDao.deleteAddress(address)
    }

    override fun observeAllAddresses(): LiveData<List<Address>> {
        return addressDao.observeAllAddresses()
    }

    override fun observeAvailableBalance(): LiveData<String> {
        return balanceDao.observeAvailableBalance()
    }

    override fun observePendingRcvdBalance(): LiveData<String> {
        return balanceDao.observePendingRcvBalance()
    }

    override fun observeBalance(): LiveData<Balance> {
        return balanceDao.observeBalance()
    }
}
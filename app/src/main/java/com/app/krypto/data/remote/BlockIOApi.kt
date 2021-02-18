package com.app.krypto.data.remote

import com.app.krypto.BuildConfig
import com.app.krypto.data.remote.responses.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BlockIOApi {
    @GET("/api/v2/get_balance/")
    suspend fun getBalance(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Response<BalanceResponse>

    @GET("/api/v2/get_my_addresses/")
    suspend fun getAddresses(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ): Response<AddressesResponse>

    @GET("/api/v2/get_current_price/")
    suspend fun getBitcoinBasePrice(
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY
    ):Response<PricesResponse>

    @POST("/v2/get_new_address/")
    suspend fun createAddress(
        @Body data:Data,
        @Query("api_key")
        apiKey: String = BuildConfig.API_KEY,
    ):Response<CreateAddressResponse>

}
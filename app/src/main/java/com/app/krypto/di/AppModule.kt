package com.app.krypto.di

import android.content.Context
import androidx.room.Room
import com.app.krypto.BuildConfig
import com.app.krypto.data.local.AppDatabase
import com.app.krypto.data.local.daos.AddressDao
import com.app.krypto.data.local.daos.BalanceDao
import com.app.krypto.data.local.daos.PriceDao
import com.app.krypto.data.remote.BlockIOApi
import com.app.krypto.repositories.BlockIORepository
import com.app.krypto.repositories.DefaultRepository
import com.app.krypto.utils.Constants.BASE_URL
import com.app.krypto.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn


import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesBalanceDao(database: AppDatabase) = database.balanceDao()

    @Singleton
    @Provides
    fun providesAddressDao(database: AppDatabase) = database.addressDao()

    @Singleton
    @Provides
    fun providesPriceDao(database: AppDatabase) = database.priceDao()

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): BlockIOApi = retrofit.create(BlockIOApi::class.java)

    @Singleton
    @Provides
    fun providesDefaultRepository(
        api: BlockIOApi,
        dao: BalanceDao,
        addressDao: AddressDao,
        priceDao:PriceDao
    ) = DefaultRepository(api, dao, addressDao,priceDao) as BlockIORepository
}

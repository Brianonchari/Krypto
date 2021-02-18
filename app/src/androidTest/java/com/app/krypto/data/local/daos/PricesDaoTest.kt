package com.app.krypto.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.krypto.data.local.AppDatabase
import com.app.krypto.data.local.entities.Price
import com.app.krypto.data.local.getOrAwaitValue
import com.app.krypto.data.remote.responses.Address
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class PricesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase
    private lateinit var priceDao :PriceDao

    @Before
    fun setup(){
        hiltRule.inject()
        priceDao = database.priceDao()
    }

    @After
    fun teardown(){
        database.close()
    }


    @Test
    fun insertPrice() = runBlockingTest {
        val priceItem = Price("Coinbase","12000","USD",123,id = 1)
        priceDao.insertPrice(priceItem)
        val allPrices = priceDao.observeAllPrices().getOrAwaitValue()
        assertThat(allPrices).contains(priceItem)
    }

    @Test
    fun insertPriceList() = runBlockingTest {
        val priceItem1 = Price("Coinbase","12000","USD",123,id = 1)
        val priceItem2 = Price("Coinbase","12000","USD",123,id = 2)
        val priceItem3 = Price("Coinbase","12000","USD",123,id = 3)
        val list:List<Price> = mutableListOf(priceItem1,priceItem2,priceItem3)
        priceDao.insertPrices(list)
        val allAddresses = priceDao.observeAllPrices().getOrAwaitValue()
        assertThat(allAddresses).containsAnyIn(list)
    }
    
}
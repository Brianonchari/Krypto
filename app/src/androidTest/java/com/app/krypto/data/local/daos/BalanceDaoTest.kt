package com.app.krypto.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.krypto.data.local.AppDatabase
import com.app.krypto.data.local.entities.Balance
import com.app.krypto.data.local.getOrAwaitValue
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

/**
 *Created by Brian Onchari on 28/01/2021.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class BalanceDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Inject
    @Named("test_db")
    lateinit var database:AppDatabase
    private lateinit var balanceDao:BalanceDao

    @Before
    fun setup(){
        hiltRule.inject()
        balanceDao = database.balanceDao()
    }
    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertBalance() = runBlockingTest {
        val balanceItem = Balance("0.111","BTC","0.0",id=1)
        balanceDao.insertBalance(balanceItem)
        val balance = balanceDao.observeBalance().getOrAwaitValue()
        assertThat(balance).isEqualTo(balanceItem)
    }

    @Test
    fun observeAvailableBalance()= runBlockingTest {
        val balanceItem = Balance("0.111","BTC","0.0",id=1)
        balanceDao.insertBalance(balanceItem)
        val availableBalance = balanceDao.observeBalance().getOrAwaitValue()
        assertThat(availableBalance.available_balance).isEqualTo("0.111")
    }

    @Test
    fun observePendingRcvBalance() = runBlockingTest {
        val balanceItem = Balance("0.111","BTC","0.0",id=1)
        balanceDao.insertBalance(balanceItem)
        val pendingReceivableBalance = balanceDao.observeBalance().getOrAwaitValue()
        assertThat(pendingReceivableBalance.pending_received_balance).isEqualTo("0.0")
    }

    @Test
    fun deleteBalance()= runBlockingTest {
        val balanceItem = Balance("0.111","BTC","0.0",id=1)
        balanceDao.insertBalance(balanceItem)
        balanceDao.delete(balanceItem)
        val balance = balanceDao.observeBalance().getOrAwaitValue()
        assertThat(balance).isNull()
    }
}
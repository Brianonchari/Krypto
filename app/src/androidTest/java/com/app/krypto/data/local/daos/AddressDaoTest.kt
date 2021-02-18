package com.app.krypto.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.app.krypto.data.local.AppDatabase
import com.app.krypto.data.local.getOrAwaitValue
import com.app.krypto.data.remote.responses.Address
import com.app.krypto.launchFragmentInHiltContainer
import com.app.krypto.ui.fragments.PriceDetailFragment
import com.app.krypto.ui.fragments.ProfileFragment
import com.app.krypto.ui.fragments.WalletsFragment
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
class AddressDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase
    private lateinit var addressDao: AddressDao

    @Before
    fun setup() {
        hiltRule.inject()
        addressDao = database.addressDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAddressItem() = runBlockingTest {
        val addressItem = Address("address", "5", true, "label", "0.000", user_id = 1)
        addressDao.insertAddress(addressItem)
        val allAddressItems = addressDao.observeAllAddresses().getOrAwaitValue()
        assertThat(allAddressItems).contains(addressItem)
    }

    @Test
    fun insertAddressLists() = runBlockingTest {
        val addressItem1 = Address("address", "5", true, "label", "0.000", user_id = 1)
        val addressItem2 = Address("address", "5", true, "label", "0.000", user_id = 2)
        val addressItem3 = Address("address", "5", true, "label", "0.000", user_id = 3)
        val list:List<Address> = mutableListOf(addressItem1,addressItem2,addressItem3)
        addressDao.insertAddresses(list)
        val allAddresses = addressDao.observeAllAddresses().getOrAwaitValue()
        assertThat(allAddresses).containsAnyIn(list)
    }

    @Test
    fun deleteAddress() = runBlockingTest {
        val addressItem = Address("address", "5", true, "label", "0.000", user_id = 1)
        addressDao.insertAddress(addressItem)
        addressDao.deleteAddress(addressItem)
        val allAddresses  = addressDao.observeAllAddresses().getOrAwaitValue()
        assertThat(allAddresses).doesNotContain(addressItem)
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<PriceDetailFragment> {

        }
    }
}
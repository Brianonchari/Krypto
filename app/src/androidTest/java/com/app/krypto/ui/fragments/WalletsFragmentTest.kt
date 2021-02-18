package com.app.krypto.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject


/**
 * Created by Brian Onchari on 27/01/2021.
 */
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class WalletsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    lateinit var fragmentFactory: KryptoFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }
}
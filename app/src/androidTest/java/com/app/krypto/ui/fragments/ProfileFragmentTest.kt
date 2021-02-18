package com.app.krypto.ui.fragments

import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Created by Brian Onchari on 27/01/2021.
 */
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ProfileFragmentTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup(){
        hiltRule.inject()
    }
}
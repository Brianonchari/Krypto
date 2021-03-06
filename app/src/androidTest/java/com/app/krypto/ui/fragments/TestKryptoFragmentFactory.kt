package com.app.krypto.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.app.krypto.adapters.AddressRecyclerAdapter
import com.app.krypto.repositories.FakeKryptoRepositoryAndroidTest
import com.app.krypto.ui.viewmodels.WalletViewModel
import javax.inject.Inject

/**
 *Created by Brian Onchari on 22/01/2021.
 */
class TestKryptoFragmentFactory @Inject constructor(
    private val addressRecyclerAdapter: AddressRecyclerAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            WalletsFragment::class.java.name -> WalletsFragment()
            ProfileFragment::class.java.name -> ProfileFragment()
            PriceDetailFragment::class.java.name -> PriceDetailFragment()
            else -> super.instantiate(classLoader, className)

        }
    }
}
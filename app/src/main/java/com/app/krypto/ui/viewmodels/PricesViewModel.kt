package com.app.krypto.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.krypto.data.local.entities.Price
import com.app.krypto.repositories.BlockIORepository
import com.app.krypto.utils.Event
import com.app.krypto.utils.NetworkHelperUtil
import com.app.krypto.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by Brian Onchari on 11/02/2021.
 */
@HiltViewModel
class PricesViewModel @Inject constructor(
    private val repository: BlockIORepository,
    val networkHelperUtil: NetworkHelperUtil
) : ViewModel() {

    val prices = MutableLiveData<Event<Resource<List<Price>>>>()
    val _prices = repository.observeAllPrices()


    init {
        getPrices()
    }

    private fun getPrices() {
        viewModelScope.launch {
            prices.value = Event(Resource.loading(null))
            if (networkHelperUtil.isNetworkConnected()) {
                repository.getBitcoinBasePrices().let {
                    prices.postValue(Event(Resource.success(it.data?.priceData?.prices)))
                    val prices = it.data?.priceData?.prices
                    prices?.let { it1 -> repository.insertPrices(it1) }

                }
            } else prices.postValue(
                Event(
                    Resource.error(
                        "You don't have an active internet connection. Please check your connection and try again",
                        null
                    )
                )
            )
        }
    }
}
package com.app.krypto.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.krypto.data.local.entities.Balance
import com.app.krypto.data.remote.responses.Address
import com.app.krypto.data.remote.responses.Data
import com.app.krypto.repositories.BlockIORepository
import com.app.krypto.utils.Event
import com.app.krypto.utils.NetworkHelperUtil
import com.app.krypto.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *Created by Brian Onchari on 16/01/2021.
 *
 */
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: BlockIORepository,
    val networkHelper: NetworkHelperUtil
) : ViewModel() {
    private val _loadingState = MutableLiveData<Event<Resource<Boolean>>>()
    val loadingState:LiveData<Event<Resource<Boolean>>>
    get() = _loadingState

    private val _createAddress = MutableLiveData<Event<Resource<Data>>>()
    val createAddress:LiveData<Event<Resource<Data>>>
    get() = _createAddress
    private val _balance = MutableLiveData<Event<Resource<Balance>>>()
    private val _localBal = MutableLiveData<Event<Resource<Balance>>>()

    val getCachedAddresses = repository.observeAllAddresses()

    private val _addresses = MutableLiveData<Event<Resource<List<Address>>>>()
    val availableBalance = repository.observeAvailableBalance()
    val pendingReceivedBal = repository.observePendingRcvdBalance()

    init {
        getWalletBalance()
        getAddresses()
        getCachedBalance()
    }

    fun deleteAddressItem(address: Address) = viewModelScope.launch {
        repository.deleteAddress(address)
    }

    fun insertAddress(address: Address) = viewModelScope.launch {
        repository.insertAddress(address)
    }

    private fun getCachedBalance() {
        viewModelScope.launch {
            _balance.value = Event(Resource.loading(null))
            repository.getCachedBalance().let {
                _localBal.postValue(Event(Resource.success(it)))
            }
        }
    }

    /**Get Wallet Balance**/
    private fun getWalletBalance() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getBalance().let {
                    _balance.postValue(Event(Resource.success(it.data?.balance)))
                    val balance = Balance(
                        it.data?.balance?.available_balance.toString(),
                        it.data?.balance?.network.toString(),
                        it.data?.balance?.pending_received_balance.toString()
                    )
                    Log.d("TAG", "getWalletBalance: Insert Balance to db")
                    repository.insertBalance(balance)
                }
            } else _balance.postValue(
                Event(
                    Resource.error(
                        "You don't have an active internet connection. Please check your connection and try again",
                        null
                    )
                )
            )
        }
    }

    private fun getAddresses() {
        viewModelScope.launch {
            _addresses.value = Event(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                repository.getAddresses().let {
                    _addresses.postValue(Event(Resource.success(it.data?.data?.addresses)))
                    val address = it.data?.data?.addresses
                    address?.let { it1 -> repository.insertAddresses(it1) }
                }
            } else _addresses.postValue(
                Event(
                    Resource.error(
                        "You don't have an active internet connection. Please check your connection and try again",
                        null
                    )
                )
            )
        }
    }

     fun createNewAddress(label:String){
        _createAddress.postValue(Event(Resource.loading(null)))
        if(label.isEmpty()){
            _createAddress.postValue(Event(Resource.error("Please enter address label",null)))
            return
        }

        viewModelScope.launch {
          repository.createNewAddress(label).let {
              _createAddress.postValue(Event(Resource.success(it.data?.data)))
          }
        }
    }
}

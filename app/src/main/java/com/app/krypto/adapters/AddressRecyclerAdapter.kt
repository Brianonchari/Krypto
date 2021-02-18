package com.app.krypto.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.krypto.data.remote.responses.Address
import com.app.krypto.databinding.AddressItemBinding
import dagger.assisted.AssistedInject
import javax.inject.Inject
import javax.inject.Singleton

/**
 *Created by Brian Onchari on 16/01/2021.
 */
class AddressRecyclerAdapter:
    RecyclerView.Adapter<AddressRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.user_id == newItem.user_id
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallBack)
    var addressItems: List<Address>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = addressItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addressesList = addressItems[position]
        holder.binding.label.text = addressesList.label
        holder.binding.availableBalance.text = addressesList.available_balance
        holder.binding.pendingBalance.text = addressesList.pending_received_balance
    }

}
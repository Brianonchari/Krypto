package com.app.krypto.adapters

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.app.krypto.data.remote.responses.Address
import com.app.krypto.databinding.AddressItemBinding
import javax.inject.Inject

/**
 *Created by Brian Onchari on 16/01/2021.
 */
class AddressRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<AddressRecyclerAdapter.ViewHolder>() {

    private var visibility = false

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
        holder.binding.address.text = addressesList.address

        holder.itemView.setOnClickListener {
            val transition = Slide(Gravity.START)
            TransitionManager.beginDelayedTransition(holder.binding.parentLayout, transition)
            holder.binding.address.visibility = if (visibility) View.INVISIBLE else View.VISIBLE
            visibility = !visibility
        }
    }
}
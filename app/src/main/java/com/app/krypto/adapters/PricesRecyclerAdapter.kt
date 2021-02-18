package com.app.krypto.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.krypto.data.local.entities.Price
import com.app.krypto.databinding.PriceItemBinding

/**
 *Created by Brian Onchari on 16/01/2021.
 */
class PricesRecyclerAdapter:RecyclerView.Adapter<PricesRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PriceItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Price>() {
        override fun areItemsTheSame(oldItem: Price, newItem: Price): Boolean {
            return oldItem.price == newItem.price
        }

        override fun areContentsTheSame(oldItem: Price, newItem: Price): Boolean {
           return  oldItem ==newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallBack)
    var priceItems: List<Price>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PriceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val priceList = differ.currentList[position]
        holder.binding.priceTv.text = priceList.price
        holder.binding.exchange.text = priceList.exchange
        holder.binding.priceBase.text = priceList.price_base

    }
}
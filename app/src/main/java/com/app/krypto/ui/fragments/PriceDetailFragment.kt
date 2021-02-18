package com.app.krypto.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.krypto.R
import com.app.krypto.adapters.PricesRecyclerAdapter
import com.app.krypto.databinding.FragmentPriceDetailBinding
import com.app.krypto.ui.viewmodels.PricesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PriceDetailFragment @Inject constructor() : Fragment(R.layout.fragment_price_detail) {
    private var _binding: FragmentPriceDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: PricesViewModel
    val pricesRecyclerAdapter = PricesRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPriceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PricesViewModel::class.java)
        subscribeToObservers()
        setupRecyclerview()
    }

    private fun subscribeToObservers() {
        viewModel._prices.observe(viewLifecycleOwner, Observer { response ->
            Log.d("TAG", "subscribeToObservers:$response ")
            pricesRecyclerAdapter.differ.submitList(response)
        })
    }

    private fun setupRecyclerview() {
        val pricesRV = binding.priceRv
        pricesRV.apply {
            adapter = pricesRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
//            ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(this)
        }
    }
}
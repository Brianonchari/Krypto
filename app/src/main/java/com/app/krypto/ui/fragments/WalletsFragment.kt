package com.app.krypto.ui.fragments

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.krypto.R
import com.app.krypto.adapters.AddressRecyclerAdapter
import com.app.krypto.databinding.AddressDialogBinding
import com.app.krypto.databinding.FragmentWalletBinding
import com.app.krypto.databinding.ReceiveDialogBinding
import com.app.krypto.databinding.SendDialogBinding
import com.app.krypto.ui.viewmodels.WalletViewModel
import com.app.krypto.utils.Status
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class WalletsFragment : Fragment(R.layout.fragment_wallet) {
    lateinit var viewModel:WalletViewModel
    val addressRecyclerAdapter = AddressRecyclerAdapter()
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    private var menu: Menu? = null

    companion object {
        private const val TAG = "WalletsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notification_menu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notification -> Toast.makeText(
                requireContext(),
                "Move to notifications",
                Toast.LENGTH_SHORT
            ).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WalletViewModel::class.java)
        Log.d(TAG, "onViewCreated: Data")
        subscribeToObservers()
        receiveBitcoin()
        sendBitcoin()
        createNewBitcoinAddress()
        setupRecyclerview()

    }

    private val itemTouchCallBack= object :ItemTouchHelper.SimpleCallback(
        0,LEFT or RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        )=true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = addressRecyclerAdapter.addressItems[pos]
            viewModel?.deleteAddressItem(item)
            /**Delete Address from local db and archive it from the api**/
            Snackbar.make(requireView(),"Successfully archived an address",Snackbar.LENGTH_LONG)
                .apply {
                    setAction("UNDO"){
                        viewModel?.insertAddress(item)
                        Log.d(TAG, "onSwiped: Swipe works")
                        // TODO: 08/02/2021 Unarchive address from the server
                    }
                    show()
                }
        }
    }

    private fun createNewBitcoinAddress() {
        binding.fabAddAddress.setOnClickListener {
            val mBinding =
                AddressDialogBinding.inflate(LayoutInflater.from(parentFragment?.requireContext()))
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(mBinding.root)
            val mAlertDialog = mBuilder.show()
            mBinding.createAddress.setOnClickListener {
                mAlertDialog.dismiss()
                createAddress()
            }
            mBinding.cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun sendBitcoin() {
        binding.send.setOnClickListener {
            val binding =
                SendDialogBinding.inflate(LayoutInflater.from(parentFragment?.requireContext()))
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(binding.root)
            val mAlertDialog = mBuilder.show()
            // TODO: 20/01/2021 dialog operations here
        }
    }

    private fun receiveBitcoin() {
        binding.receive.setOnClickListener {
            val binding =
                ReceiveDialogBinding.inflate(LayoutInflater.from(parentFragment?.requireContext()))
            val mBuilder = AlertDialog.Builder(requireContext())
                .setView(binding.root)
            val mAlertDialog = mBuilder.show()
            val data = "BITCOIN_ADDRESS"
            val textToSend = StringBuilder()
            textToSend.append(data)
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix =
                    multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 600, 600)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)
                binding.qrImageview.setImageBitmap(bitmap)
                binding.qrImageview.visibility = View.VISIBLE
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }
//
    private fun setupRecyclerview() {
        val rvAddress = binding.addressRecyclerview
        rvAddress.apply {
            adapter = addressRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(this)
        }
    }

    private fun createAddress(){
        val mBinding =AddressDialogBinding.inflate(LayoutInflater.from(parentFragment?.requireContext()))
        val label = mBinding.addressLabel.text.toString()
        if(label.isEmpty()){
            return
        }
        viewModel?.createNewAddress(label)
    }

    /**Observe livedata and display data **/
    private fun subscribeToObservers() {
        viewModel?.createAddress?.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {result->
                when(result.status){
                    Status.SUCCESS ->{
                        val label = result.data?.label
                    }

                    Status.ERROR ->{
                        Log.d(TAG, "subscribeToObservers: Error")
                    }
                    Status.LOADING ->{
                        /* NO-OP */
                    }
                }
            }

        })

        viewModel?.getCachedAddresses?.observe(viewLifecycleOwner, Observer {
            addressRecyclerAdapter.addressItems = it
        })
        /**Observe Balance**/
        viewModel?.availableBalance?.observe(viewLifecycleOwner, Observer {
            val availabaleBalance = it ?: "0.00BTC"
            val balance = "$availabaleBalance BTC"
            binding.balance.text = balance
        })
        viewModel?.pendingReceivedBal?.observe(viewLifecycleOwner, Observer {
            val pendingReceivedBal = it ?: "0.00BTC"
            val pendingBalance = "$pendingReceivedBal BTC"
            binding.pendingBalance.text = pendingBalance
        })
    }
}

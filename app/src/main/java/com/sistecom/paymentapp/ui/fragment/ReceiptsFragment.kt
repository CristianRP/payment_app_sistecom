package com.sistecom.paymentapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.receipt.Receipt
import com.sistecom.paymentapp.databinding.ReceiptsFragmentBinding
import com.sistecom.paymentapp.ui.adapter.ReceiptByCustomerAdapter
import com.sistecom.paymentapp.ui.base.ViewModelFactory
import com.sistecom.paymentapp.ui.viewmodel.ReceiptsViewModel
import com.sistecom.paymentapp.utils.PrefManagerHelper

import com.sistecom.paymentapp.utils.Status.SUCCESS
import com.sistecom.paymentapp.utils.Status.LOADING
import com.sistecom.paymentapp.utils.Status.ERROR
import okhttp3.internal.notify

class ReceiptsFragment : Fragment() {

    companion object {
        fun newInstance() = ReceiptsFragment()
    }

    private lateinit var receiptsViewModel: ReceiptsViewModel
    private lateinit var receiptsAdapter: ReceiptByCustomerAdapter
    private lateinit var receiptsFragmentBinding: ReceiptsFragmentBinding
    private val customerAlternId by lazy { PrefManagerHelper.read(PrefManagerHelper.CUSTOMER_ID, "1") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        receiptsFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.receipts_fragment,
                container,
                false
        )
        receiptsFragmentBinding.lifecycleOwner = this
        receiptsFragmentBinding.title = resources.getString(R.string.title_receipts)
        setupViewModel()
        setupUI()
        setupObservers()
        return receiptsFragmentBinding.root
    }

    private fun setupViewModel() {
        receiptsViewModel = ViewModelProvider(
                this,
                ViewModelFactory(SistecomApiHelper(RetrofitBuilder.apiService, alternId = customerAlternId)))
                .get(ReceiptsViewModel::class.java)
    }

    private fun setupUI() {
        receiptsFragmentBinding.recyclerReceipts.layoutManager = LinearLayoutManager(activity?.applicationContext)
        receiptsAdapter = ReceiptByCustomerAdapter(arrayListOf())
        receiptsFragmentBinding.recyclerReceipts.adapter = receiptsAdapter
    }

    private fun setupObservers() {
        receiptsViewModel.getReceiptsByCustomer().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource.status) {
                    SUCCESS -> {
                        receiptsFragmentBinding.recyclerReceipts.visibility = View.VISIBLE
                        receiptsFragmentBinding.progressBar.visibility = View.GONE
                        resource.data?.let { data -> retrieveList(data.data) }
                    }
                    ERROR -> {
                        receiptsFragmentBinding.recyclerReceipts.visibility = View.VISIBLE
                        receiptsFragmentBinding.progressBar.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        receiptsFragmentBinding.progressBar.visibility = View.VISIBLE
                        receiptsFragmentBinding.recyclerReceipts.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(listReceipts: List<Receipt>) {
        receiptsAdapter.apply {
            addReceipts(listReceipts)
            notifyDataSetChanged()
        }
    }

}
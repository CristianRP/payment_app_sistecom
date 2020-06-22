package com.sistecom.paymentapp.ui.adapter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sistecom.paymentapp.MainActivity
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.order.Order
import com.sistecom.paymentapp.data.model.order.OrderByContractResponse
import com.sistecom.paymentapp.databinding.OrdersByContractFragmentBinding
import com.sistecom.paymentapp.databinding.PendingOrdersFragmentBinding
import com.sistecom.paymentapp.ui.base.ViewModelFactory
import com.sistecom.paymentapp.ui.fragment.OrdersByContractFragment
import com.sistecom.paymentapp.ui.fragment.PaymentFragment
import com.sistecom.paymentapp.ui.viewmodel.OrdersByContractViewModel
import com.sistecom.paymentapp.ui.viewmodel.PendingOrdersViewModel
import com.sistecom.paymentapp.utils.PrefManagerHelper
import com.sistecom.paymentapp.utils.PrefManagerHelper.CUSTOMER_ID
import com.sistecom.paymentapp.utils.Status

class PendingOrdersFragment : Fragment() {

    companion object {
        fun newInstance() = PendingOrdersFragment()
    }

    private lateinit var pendingOrdersByContractViewModel: PendingOrdersViewModel
    private lateinit var ordersByContractAdapter: OrdersByContractAdapter
    private var tracker: SelectionTracker<Long>? = null
    private lateinit var ordersByContractFragmentBinding: OrdersByContractFragmentBinding
    private val customerAlternId by lazy { PrefManagerHelper.read(CUSTOMER_ID, "1") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ordersByContractFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.orders_by_contract_fragment,
                container,
                false
        )
        ordersByContractFragmentBinding.lifecycleOwner = this
        ordersByContractFragmentBinding.title = resources.getString(R.string.title_order_contract)
        setupViewModel()
        setupUI()
        setupObservers()
        return ordersByContractFragmentBinding.root
    }

    private fun setupViewModel() {
        pendingOrdersByContractViewModel = ViewModelProvider(
                this,
                ViewModelFactory(SistecomApiHelper(RetrofitBuilder.apiService, alternId = customerAlternId)))
                .get(PendingOrdersViewModel::class.java)
    }

    private fun setupUI() {
        ordersByContractFragmentBinding.recyclerOrders.layoutManager = LinearLayoutManager(activity?.applicationContext)
        ordersByContractAdapter = OrdersByContractAdapter(arrayListOf()) { order: Order -> navigateToOrders(order) }
        ordersByContractFragmentBinding.recyclerOrders.adapter = ordersByContractAdapter
        tracker = SelectionTracker.Builder<Long>(
                "mySelection",
                ordersByContractFragmentBinding.recyclerOrders,
                StableIdKeyProvider(ordersByContractFragmentBinding.recyclerOrders),
                OrdersByContractAdapter.MyItemDetailsLookup(ordersByContractFragmentBinding.recyclerOrders),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()
        ordersByContractAdapter.tracker = tracker
    }

    private fun setupObservers() {
        pendingOrdersByContractViewModel.getPendingOrdersByContract().observe(viewLifecycleOwner, Observer {
            it?.let {resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        ordersByContractFragmentBinding.recyclerOrders.visibility = View.VISIBLE
                        ordersByContractFragmentBinding.progressBar.visibility = View.GONE
                        resource.data?.let { data -> retrieveList(data.data) }
                    }
                    Status.ERROR -> {
                        ordersByContractFragmentBinding.recyclerOrders.visibility = View.VISIBLE
                        ordersByContractFragmentBinding.progressBar.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        ordersByContractFragmentBinding.progressBar.visibility = View.VISIBLE
                        ordersByContractFragmentBinding.recyclerOrders.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(listOrder: List<Order>) {
        ordersByContractAdapter.apply {
            addOrders(listOrder)
            notifyDataSetChanged()
        }
    }

    private fun navigateToOrders(order: Order) {
        Log.e("ORDERS_FRAGMENT", "MS: ONCLICK")
        val orderBundle = PendingOrdersFragmentArgs.Builder(order)
        findNavController().navigate(R.id.action_pending_orders_fragment_to_payment_fragment,
                orderBundle.build().toBundle())
    }
}
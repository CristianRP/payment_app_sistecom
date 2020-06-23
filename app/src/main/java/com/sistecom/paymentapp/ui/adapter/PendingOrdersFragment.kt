package com.sistecom.paymentapp.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.selects.select

class PendingOrdersFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = PendingOrdersFragment()
    }

    private lateinit var pendingOrdersByContractViewModel: PendingOrdersViewModel
    private lateinit var pendingOrdersAdapter: PendingOrdersAdapter
    private var tracker: SelectionTracker<Long>? = null
    private lateinit var ordersByContractFragmentBinding: OrdersByContractFragmentBinding
    private val customerAlternId by lazy { PrefManagerHelper.read(CUSTOMER_ID, "1") }
    private var selectedItems: ArrayList<Order> = arrayListOf()

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
        if (savedInstanceState != null) {
            tracker?.onRestoreInstanceState(savedInstanceState)
        }
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
        pendingOrdersAdapter = PendingOrdersAdapter(arrayListOf())
        ordersByContractFragmentBinding.recyclerOrders.adapter = pendingOrdersAdapter
        tracker = SelectionTracker.Builder(
                "selection-1",
                ordersByContractFragmentBinding.recyclerOrders,
                StableIdKeyProvider(ordersByContractFragmentBinding.recyclerOrders),
                PendingOrdersAdapter.MyItemDetailsLookup(ordersByContractFragmentBinding.recyclerOrders),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()
        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            val gson = Gson()
            override fun onSelectionChanged() {
                val numberOfSelections = tracker?.selection!!.size()
                if (numberOfSelections > 0){
                    ordersByContractFragmentBinding.title = "$numberOfSelections ordenes seleccionadas"
                    ordersByContractFragmentBinding.selected = true
                    ordersByContractFragmentBinding.btnPayment.show()
                    ordersByContractFragmentBinding.btnPayment.extend()
                    tracker?.selection!!.map {
                        val order = pendingOrdersAdapter.ordersList[it.toInt()]
                        if (!selectedItems.contains(order)) {
                            selectedItems.apply {
                                add(order)
                            }
                        }
                        Log.e("SELECTED_ITEMS", "TEST : ${gson.toJson(order)}")
                    }
                } else {
                    ordersByContractFragmentBinding.title = resources.getString(R.string.title_order_contract)
                    ordersByContractFragmentBinding.selected = false
                    ordersByContractFragmentBinding.btnPayment.hide()
                    selectedItems.clear()
                }
            }
        })
        pendingOrdersAdapter.tracker = tracker
        ordersByContractFragmentBinding.btnPayment.setOnClickListener(this)
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
        pendingOrdersAdapter.apply {
            addOrders(listOrder)
            notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        val gson = Gson()
        Log.e("PENDING", "ORDERS: ${gson.toJson(selectedItems)}")
        navigateToOrders(selectedItems)
    }

    private fun navigateToOrders(orders: List<Order>) {
        Log.e("ORDERS_FRAGMENT", "MS: ONCLICK")
        val orderBundle = PendingOrdersFragmentArgs.Builder(orders.toTypedArray())
        findNavController().navigate(R.id.action_pending_orders_fragment_to_payment_fragment,
                orderBundle.build().toBundle())
    }
}
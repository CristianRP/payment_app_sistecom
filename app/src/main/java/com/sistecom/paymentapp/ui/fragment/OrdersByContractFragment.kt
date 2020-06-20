package com.sistecom.paymentapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.Contract
import com.sistecom.paymentapp.data.model.order.Order
import com.sistecom.paymentapp.data.model.order.OrderByContractResponse
import com.sistecom.paymentapp.databinding.OrdersByContractFragmentBinding
import com.sistecom.paymentapp.ui.adapter.OrdersByContractAdapter
import com.sistecom.paymentapp.ui.base.ViewModelFactory
import com.sistecom.paymentapp.ui.viewmodel.OrdersByContractViewModel

import com.sistecom.paymentapp.utils.Status.SUCCESS
import com.sistecom.paymentapp.utils.Status.LOADING
import com.sistecom.paymentapp.utils.Status.ERROR

class OrdersByContractFragment : Fragment() {

    companion object {
        fun newInstance() = OrdersByContractFragment()
    }

    private lateinit var ordersByContractViewModel: OrdersByContractViewModel
    private lateinit var ordersByContractAdapter: OrdersByContractAdapter
    private lateinit var ordersByContractFragmentBinding: OrdersByContractFragmentBinding
    private val contractByCustomerArguments: ContractByCustomerFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        ordersByContractFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.orders_by_contract_fragment,
                container,
                false
        )
        ordersByContractFragmentBinding.lifecycleOwner = this

        setupViewModel()
        setupUI()
        setupObservers()
        return ordersByContractFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(OrdersByContractViewModel::class.java)
        //Toast.makeText(activity?.applicationContext, arguments?.let { OrdersByContractFragmentArgs.fromBundle(it).contractId }.toString(), Toast.LENGTH_LONG).show()
    }

    private fun setupViewModel() {
        val contractId = contractByCustomerArguments.contractId
        Log.e("ORDERS", "CONTRACT ID: $contractId")
        ordersByContractViewModel = ViewModelProvider(
                this,
                ViewModelFactory(SistecomApiHelper(RetrofitBuilder.apiService, contractId)))
                .get(OrdersByContractViewModel::class.java)
    }

    private fun setupUI() {
        ordersByContractFragmentBinding.recyclerOrders.layoutManager = LinearLayoutManager(activity?.applicationContext)
        ordersByContractAdapter = OrdersByContractAdapter(arrayListOf()) { order: Order -> view?.let { navigateToOrders(it, order = order) } }
        ordersByContractFragmentBinding.recyclerOrders.adapter = ordersByContractAdapter
    }

    private fun setupObservers() {
        ordersByContractViewModel.getOrdersByContract().observe(viewLifecycleOwner, Observer {
            it?.let {resource ->
                when (resource.status) {
                    SUCCESS -> {
                        ordersByContractFragmentBinding.recyclerOrders.visibility = View.VISIBLE
                        ordersByContractFragmentBinding.progressBar.visibility = View.GONE
                        resource.data?.let { data -> retrieveList(data.data) }
                    }
                    ERROR -> {
                        ordersByContractFragmentBinding.recyclerOrders.visibility = View.VISIBLE
                        ordersByContractFragmentBinding.progressBar.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
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

    private fun navigateToOrders(view: View, order: Order) {
        Log.e("ORDERS_FRAGMENT", "MS: ONCLICK")
        /*val action =
                ContractByCustomerFragmentDirections
                        .actionContractByCustomerFragmentToOrdersByContractFragment()
        action.contractId = contract.id
        view.findNavController().navigate(action)*/
        val gson = Gson()
        val bundle = Bundle()
        bundle.apply {
            putString("order", gson.toJson(order))
        }
        val orderFragment = PaymentFragment()
        orderFragment.arguments = bundle

        Log.e("ORDERS_FRAGMENT", "MS: $order")

        /*activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_content, orderFragment)
                ?.addToBackStack(OrdersByContractFragment::class.java.simpleName)
                ?.commit()*/
    }


}
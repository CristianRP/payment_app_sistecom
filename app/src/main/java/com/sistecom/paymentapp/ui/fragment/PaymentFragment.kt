package com.sistecom.paymentapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.model.order.Order
import com.sistecom.paymentapp.databinding.PaymentFragmentBinding
import com.sistecom.paymentapp.ui.adapter.PendingOrdersFragmentArgs
import com.sistecom.paymentapp.ui.viewmodel.PaymentViewModel
import com.sistecom.paymentapp.ui.viewmodel.PendingOrdersViewModel

class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    private lateinit var viewModel: PaymentViewModel
    private lateinit var paymentFragmentBinding: PaymentFragmentBinding
    private val orderArgs: PendingOrdersFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        paymentFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.payment_fragment,
                container,
                false
        )
        paymentFragmentBinding.title = resources.getString(R.string.title_payment)
        return paymentFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        val orders = orderArgs.orders
        val gson = Gson()
        Log.e("ORER_FRAG", "MS: ${gson.toJson(orders)}")
        paymentFragmentBinding.order = orders[0]
        var pato = 0.toDouble()
        orders.map {
            pato += it.amount
        }
        paymentFragmentBinding.title = pato.toString()
    }



}
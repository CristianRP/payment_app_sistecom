package com.sistecom.paymentapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.model.order.Order
import com.sistecom.paymentapp.databinding.PaymentFragmentBinding
import com.sistecom.paymentapp.ui.viewmodel.PaymentViewModel

class PaymentFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentFragment()
    }

    private lateinit var viewModel: PaymentViewModel
    private lateinit var paymentFragmentBinding: PaymentFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        paymentFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.payment_fragment,
                container,
                false
        )
        return paymentFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        // TODO: Use the ViewModel
        val gson = Gson()
        val orderGson = arguments?.getString("order")
        val order = gson.fromJson(orderGson, Order::class.java)
        Log.e("ORER_FRAG", "MS: $order")
        paymentFragmentBinding.order = order

    }



}
package com.sistecom.paymentapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.databinding.RegisterFragmentBinding
import com.sistecom.paymentapp.ui.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    //private lateinit var viewModelRegister: RegisterViewModel
    private lateinit var binding: RegisterFragmentBinding
    private val viewModelRegister by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)
        binding.viewModelRegister = viewModelRegister
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        // viewModelRegister = ViewModelProvider(this).get(RegisterViewModel::class.java)

    }

}
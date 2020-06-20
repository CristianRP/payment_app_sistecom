package com.sistecom.paymentapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.auth.CognitoReferenceResponse
import com.sistecom.paymentapp.data.repository.MainRepository
import com.sistecom.paymentapp.databinding.RegisterFragmentBinding
import com.sistecom.paymentapp.ui.viewmodel.RegisterViewModel
import com.sistecom.paymentapp.utils.Status
import kotlinx.android.synthetic.main.register_fragment.view.*

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    //private lateinit var viewModelRegister: RegisterViewModel
    private lateinit var binding: RegisterFragmentBinding
    private val viewModelRegister by lazy { ViewModelProvider(this).get(RegisterViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        binding =
                DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)
        binding.viewModelRegister = viewModelRegister
        binding.lifecycleOwner = this
        Glide.with(binding.constraintContainerRegister.context)
                .load(binding.constraintContainerRegister.context.getDrawable(R.drawable.img_background_blueyellow))
                .into(binding.constraintContainerRegister.imgFondoPrincipal)
        //binding.btnSend.setOnClickListener() { v -> goToLogin(v) }
        //binding.btnLogin.setOnClickListener() { goToLogin() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        // viewModelRegister = ViewModelProvider(this).get(RegisterViewModel::class.java)
        val visibilityObserver = Observer<Int> {
            binding.progressBar.visibility = it
        }

        viewModelRegister.visibility.observe(viewLifecycleOwner, visibilityObserver)

        val signUpSuccess = Observer<Boolean> {
            if (it) {
                referenceCognitoToCustomer()
            }
        }

        viewModelRegister.signUpSuccess.observe(viewLifecycleOwner, signUpSuccess)
    }

    private fun referenceCognitoToCustomer() {
        viewModelRegister.user?.customerRefCode?.let { viewModelRegister.user?.userName?.let { it1 -> initApi(it, it1) } }

        viewModelRegister.addCognitoReferenceToCustomer().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        //profileFragmentBinding.containerProfile.visibility = View.VISIBLE
                        //receiptsFragmentBinding.progressBar.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        resource.data?.let { data -> getResponse(data) }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        //receiptsFragmentBinding.progressBar.visibility = View.GONE
                        //profileFragmentBinding.containerProfile.visibility = View.GONE
                        Log.e("REGISTER_FRAGMENT", it.message)
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        //profileFragmentBinding.containerProfile.visibility = View.GONE
                        //receiptsFragmentBinding.progressBar.visibility = View.VISIBLE
                        //receiptsFragmentBinding.recyclerReceipts.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun goToLogin() {
        /*activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_content, LoginFragment())
                //?.addToBackStack(LoginFragment.toString())
                ?.commit()*/
    }

    private fun initApi(alternId: String, userId: String) {
        viewModelRegister.apiHelper = SistecomApiHelper(RetrofitBuilder.apiService, alternId = alternId, userId = userId)
        viewModelRegister.mainRepository = MainRepository(viewModelRegister.apiHelper)
    }

    private fun getResponse(cognitoResponse: CognitoReferenceResponse) {
        //Toast.makeText(context, "Cliente referenciado: ${cognitoResponse.reference.message}")
        Toast.makeText(context, "Cliente referenciado con éxito", Toast.LENGTH_SHORT).show()
        Log.d("REGISTER_VM_REFER", cognitoResponse.toString())
        findNavController().popBackStack()
    }

}
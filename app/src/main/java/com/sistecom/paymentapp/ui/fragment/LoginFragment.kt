package com.sistecom.paymentapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.user.Customer
import com.sistecom.paymentapp.databinding.LoginFragmentBinding
import com.sistecom.paymentapp.ui.base.ViewModelFactory
import com.sistecom.paymentapp.ui.viewmodel.LoginViewModel
import com.sistecom.paymentapp.ui.viewmodel.ProfileViewModel
import com.sistecom.paymentapp.utils.PrefManagerHelper
import com.sistecom.paymentapp.utils.PrefManagerHelper.CUSTOMER_ID
import com.sistecom.paymentapp.utils.PrefManagerHelper.USER_ID
import com.sistecom.paymentapp.utils.Status
import kotlinx.android.synthetic.main.register_fragment.view.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        binding =
                DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        Glide.with(binding.constraintContainerLogin.context)
                .load(binding.constraintContainerLogin.context.getDrawable(R.drawable.img_background_login))
                .into(binding.constraintContainerLogin.imgFondoPrincipal)
        //binding.btnSend.setOnClickListener() { v -> goToHome(v) }
        binding.btnRegisterForm.setOnClickListener() { goToRegister() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val signInCognitSuccess = Observer<Boolean> {
            if (it) {
                setupProfileViewModel()
                getUserProfile()
                activity?.onBackPressed()
                //goToHome()
            }
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner, signInCognitSuccess)

        val visibility = Observer<Int> {
            binding.progressBar.visibility = it
        }

        viewModel.visibility.observe(viewLifecycleOwner, visibility)
    }

    private fun setupProfileViewModel() {
        profileViewModel = ViewModelProvider(
                this,
                ViewModelFactory(SistecomApiHelper(RetrofitBuilder.apiService, userId = binding.etUserName.text.toString())))
                .get(ProfileViewModel::class.java)
    }

    private fun getUserProfile() {
        viewModel.visibility.value = View.VISIBLE
        profileViewModel.getCustomerProfileByUser().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        viewModel.visibility.value = View.GONE
                        //profileFragmentBinding.containerProfile.visibility = View.VISIBLE
                        //receiptsFragmentBinding.progressBar.visibility = View.GONE
                        resource.data?.let { data -> initCustomer(data.data[0].customer) }


                    }
                    Status.ERROR -> {
                        viewModel.visibility.value = View.GONE
                        //receiptsFragmentBinding.progressBar.visibility = View.GONE
                        //profileFragmentBinding.containerProfile.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        viewModel.visibility.value = View.VISIBLE
                        //profileFragmentBinding.containerProfile.visibility = View.GONE
                        //receiptsFragmentBinding.progressBar.visibility = View.VISIBLE
                        //receiptsFragmentBinding.recyclerReceipts.visibility = View.GONE
                    }
                }
            }
        })
    }

    fun goToHome() {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_content, ContractByCustomerFragment())
                ?.commit()
    }

    private fun goToRegister() {
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_content, RegisterFragment())
                ?.commit()
    }

    private fun initCustomer(customerResponse: Customer) {
        customerResponse.userId?.let { PrefManagerHelper.write(USER_ID, it) }
        customerResponse.id?.let { PrefManagerHelper.write(CUSTOMER_ID, it) }
    }

}

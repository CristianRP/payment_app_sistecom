package com.sistecom.paymentapp.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.user.Customer
import com.sistecom.paymentapp.databinding.ProfileFragmentBinding
import com.sistecom.paymentapp.ui.base.ViewModelFactory
import com.sistecom.paymentapp.ui.viewmodel.LoginViewModel
import com.sistecom.paymentapp.ui.viewmodel.ProfileViewModel
import com.sistecom.paymentapp.ui.viewmodel.ReceiptsViewModel
import com.sistecom.paymentapp.utils.AuthenticationStatus
import com.sistecom.paymentapp.utils.Status

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var profileFragmentBinding: ProfileFragmentBinding
    private val viewModelLogin: LoginViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        profileFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.profile_fragment,
                container,
                false
        )
        profileFragmentBinding.lifecycleOwner = this
        profileFragmentBinding.btnShare.setOnClickListener { signOutUser() }
        return profileFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelLogin.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState) {
                AuthenticationStatus.AUTHENTICATED -> showWelcome()
                AuthenticationStatus.UNAUTHENTICATED -> showLogin()
                AuthenticationStatus.INVALID_AUTHENTICATION -> showErrorMessage()
                else -> showErrorMessage()
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        getCustomerProfileView()
        profileFragmentBinding.viewmodel = viewModel
        // TODO: Use the ViewModel
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
                this,
                ViewModelFactory(SistecomApiHelper(RetrofitBuilder.apiService, customerId = 1)))
                .get(ProfileViewModel::class.java)
    }

    fun getCustomerProfileView() {
        viewModel.getCustomerProfile().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when(resource.status) {
                    Status.SUCCESS -> {
                        profileFragmentBinding.containerProfile.visibility = View.VISIBLE
                        //receiptsFragmentBinding.progressBar.visibility = View.GONE
                        resource.data?.let { data -> initCustomer(data.data[0].customer) }
                    }
                    Status.ERROR -> {
                        //receiptsFragmentBinding.progressBar.visibility = View.GONE
                        profileFragmentBinding.containerProfile.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        profileFragmentBinding.containerProfile.visibility = View.GONE
                        //receiptsFragmentBinding.progressBar.visibility = View.VISIBLE
                        //receiptsFragmentBinding.recyclerReceipts.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun initCustomer(customerResponse: Customer) {
        Log.e("PROFILE_CUSTOMER", "Entra $customerResponse")
        val alternId = "Código de cliente: ${customerResponse.alternId}"
        val fullName = "Nombre de cliente: ${customerResponse.firstName} ${customerResponse.lastName}"
        val email = "Email: ${customerResponse.email}"
        val document = "${customerResponse.documentTypeId}: ${customerResponse.documentId}"
        val phoneNumber = "Número de teléfono: ${customerResponse.phoneNumber}"
        val nit = "Nit: ${customerResponse.taxId}"
        profileFragmentBinding.txtUserName.text = alternId
        profileFragmentBinding.txtUserEmail.text = fullName
        profileFragmentBinding.txtUserDob.text = email
        profileFragmentBinding.txtUserAddress.text = document
        profileFragmentBinding.txtSecurity.text = phoneNumber
        profileFragmentBinding.txtManageAccounts.text = nit
    }

    private fun signOutUser() {
        viewModelLogin.signOutUser()
    }

    private fun showWelcome() {
        Toast.makeText(activity?.applicationContext, "WELCOME", Toast.LENGTH_SHORT).show()
    }

    private fun showLogin() {
        Toast.makeText(activity?.applicationContext, "Inicia sesión para continuar.", Toast.LENGTH_SHORT).show()
        val navOptions = NavOptions.Builder().setLaunchSingleTop(true)
        findNavController().popBackStack(R.id.app_navigation, true)
        findNavController().navigate(R.id.authentication_navigation, null)
    }

    private fun showErrorMessage() {
        Toast.makeText(activity?.applicationContext, "Error en la aplicación.", Toast.LENGTH_SHORT).show()
    }
}
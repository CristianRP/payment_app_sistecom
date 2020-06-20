package com.sistecom.paymentapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.api.RetrofitBuilder
import com.sistecom.paymentapp.data.api.SistecomApiHelper
import com.sistecom.paymentapp.data.model.Contract
import com.sistecom.paymentapp.data.model.DataNode
import com.sistecom.paymentapp.data.model.order.Order
import com.sistecom.paymentapp.databinding.ContractByCustomerFragmentBinding
import com.sistecom.paymentapp.ui.adapter.ContractByCustomerAdapter
import com.sistecom.paymentapp.ui.base.ViewModelFactory
import com.sistecom.paymentapp.ui.viewmodel.ContractByCustomerViewModel
import com.sistecom.paymentapp.ui.viewmodel.LoginViewModel
import com.sistecom.paymentapp.utils.AuthenticationStatus
import com.sistecom.paymentapp.utils.PrefManagerHelper

import com.sistecom.paymentapp.utils.Status.SUCCESS
import com.sistecom.paymentapp.utils.Status.LOADING
import com.sistecom.paymentapp.utils.Status.ERROR

class ContractByCustomerFragment : Fragment() {

    companion object {
        fun newInstance() = ContractByCustomerFragment()
    }

    private val viewModelLogin: LoginViewModel by activityViewModels()

    private lateinit var viewModelContractByCustomer: ContractByCustomerViewModel
    private lateinit var contractByCustomerAdapter: ContractByCustomerAdapter
    private lateinit var contractByCustomerBinding: ContractByCustomerFragmentBinding
    private val customerAlternId by lazy { PrefManagerHelper.read(PrefManagerHelper.CUSTOMER_ID, "1") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        contractByCustomerBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.contract_by_customer_fragment,
                container,
                false
        )
        contractByCustomerBinding.lifecycleOwner = this

        setupViewModel()
        setupUI()
        setupObservers()
        return contractByCustomerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelLogin.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState) {
                AuthenticationStatus.AUTHENTICATED -> showWelcome()
                AuthenticationStatus.UNAUTHENTICATED -> showLogin()
                AuthenticationStatus.INVALID_AUTHENTICATION -> showLogin()
                else -> showErrorMessage()
            }
        })
    }

    private fun setupViewModel() {
        viewModelContractByCustomer = ViewModelProvider(
               this,
                ViewModelFactory(SistecomApiHelper(RetrofitBuilder.apiService, alternId = customerAlternId)))
                .get(ContractByCustomerViewModel::class.java)
    }

    private fun setupUI() {
        contractByCustomerBinding.recyclerContractsByCustomer.layoutManager = LinearLayoutManager(activity?.applicationContext)
        contractByCustomerAdapter = ContractByCustomerAdapter(arrayListOf()) { dataNode : DataNode -> view?.let { navigateToOrders(it, contract = dataNode.contract) } }
        contractByCustomerBinding.recyclerContractsByCustomer.adapter = contractByCustomerAdapter
    }

    private fun setupObservers() {
        viewModelContractByCustomer.getContractsByCustomer().observe(viewLifecycleOwner, Observer {
            it?.let {resource ->
                when (resource.status) {
                    SUCCESS -> {
                        contractByCustomerBinding.recyclerContractsByCustomer.visibility = View.VISIBLE
                        contractByCustomerBinding.progressBar.visibility = View.GONE
                        resource.data?.let { data -> retrieveList(data.data) }
                    }
                    ERROR -> {
                        contractByCustomerBinding.recyclerContractsByCustomer.visibility = View.VISIBLE
                        contractByCustomerBinding.progressBar.visibility = View.GONE
                        Toast.makeText(activity?.applicationContext, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        contractByCustomerBinding.progressBar.visibility = View.VISIBLE
                        contractByCustomerBinding.recyclerContractsByCustomer.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(dataNode: List<DataNode>) {
        contractByCustomerAdapter.apply {
            addDataNodes(dataNode)
            notifyDataSetChanged()
        }
    }

    private fun navigateToOrders(view: View, contract: Contract) {
        val args = ContractByCustomerFragmentArgs.Builder()
        args.contractId = contract.id
        findNavController().navigate(R.id.contract_customer_to_orders_contract, args.build().toBundle())
    }

    private fun showWelcome() {
        Toast.makeText(activity?.applicationContext, "WELCOME", Toast.LENGTH_SHORT).show()
    }

    private fun showLogin() {
        Toast.makeText(activity?.applicationContext, "Inicia sesión para continuar.", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.authentication_navigation, null)
    }

    private fun showErrorMessage() {
        Toast.makeText(activity?.applicationContext, "Error en la aplicación.", Toast.LENGTH_SHORT).show()
    }

}
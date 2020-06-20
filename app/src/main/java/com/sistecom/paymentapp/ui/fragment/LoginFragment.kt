package com.sistecom.paymentapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.databinding.LoginFragmentBinding
import com.sistecom.paymentapp.ui.viewmodel.LoginViewModel
import com.sistecom.paymentapp.utils.AuthenticationStatus
import com.sistecom.paymentapp.utils.PrefManagerHelper
import com.sistecom.paymentapp.utils.PrefManagerHelper.COGNITO_MIDDLE_NAME
import com.sistecom.paymentapp.utils.PrefManagerHelper.COGNITO_NAME
import kotlinx.android.synthetic.main.register_fragment.view.*


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by activityViewModels()
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
                .load(binding.constraintContainerLogin.context.getDrawable(R.drawable.img_background_blueyellow))
                .into(binding.constraintContainerLogin.imgFondoPrincipal)
        binding.btnRegisterForm.setOnClickListener() { goToRegister() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val signInCognitSuccess = Observer<Boolean> {
            if (it) {
                findNavController().popBackStack()

                Toast.makeText(activity?.applicationContext,
                        "Bienvenido ${PrefManagerHelper.read(COGNITO_NAME, "usuario")} ${PrefManagerHelper.read(COGNITO_MIDDLE_NAME, "usuario")}",
                        Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner, signInCognitSuccess)

        val visibility = Observer<Int> {
            binding.progressBar.visibility = it
        }

        viewModel.visibility.observe(viewLifecycleOwner, visibility)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when(authenticationState) {
                AuthenticationStatus.AUTHENTICATED -> showWelcome()
                AuthenticationStatus.UNAUTHENTICATED -> showLogin()
                AuthenticationStatus.INVALID_AUTHENTICATION -> showErrorMessage()
                else -> showErrorMessage()
            }
        })
    }

    /*private fun setupProfileViewModel() {
        profileViewModel = ViewModelProvider(
                this,
                ViewModelFactory(SistecomApiHelper(RetrofitBuilder.apiService, userId = binding.etUserName.text.toString())))
                .get(ProfileViewModel::class.java)
    }*/

    /*private fun getUserProfile() {
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
    }*/

    fun goToHome() {
        /*activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_content, ContractByCustomerFragment())
                ?.commit()*/
    }

    private fun goToRegister() {
        val actionToRegisterFragment = LoginFragmentDirections.loginToRegister()
        findNavController().navigate(actionToRegisterFragment)
    }

    private fun showWelcome() {
        findNavController().navigate(R.id.app_navigation)
    }

    private fun showLogin() {
        Toast.makeText(activity?.applicationContext, "Inicia sesión para continuar.", Toast.LENGTH_SHORT).show()
        //findNavController().navigate(R.id.authentication_navigation, null)
    }

    private fun showErrorMessage() {
        Toast.makeText(activity?.applicationContext, "Usuario no confirmado.", Toast.LENGTH_SHORT).show()
    }
}

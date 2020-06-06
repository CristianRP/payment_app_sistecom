package com.sistecom.paymentapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sistecom.paymentapp.ui.fragment.ContractByCustomerFragment
import com.sistecom.paymentapp.ui.fragment.OrdersByContractFragment
import com.sistecom.paymentapp.ui.fragment.RegisterFragment
import kotlinx.android.synthetic.main.content_main.*

/**
 * Created by: cristianramirez
 * On: 20/05/20 at: 07:05 PM
 *
 */

class MainActivity : AppCompatActivity() {

    var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.itemIconTintList = null
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                inflateFragment(ContractByCustomerFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                val bundle = Bundle()
                bundle.apply {
                    putInt("contractId", 1)
                }
                val orderFragment = OrdersByContractFragment()
                orderFragment.arguments = bundle
                inflateFragment(orderFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                inflateFragment(LoginFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                inflateFragment(RegisterFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun inflateFragment(fragment: Fragment) {
        selectedFragment = fragment
        supportFragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();
    }

    override fun onBackPressed() {
        if (selectedFragment is ContractByCustomerFragment) {
            super.onBackPressed()
        } else {
            navigation.selectedItemId = R.id.navigation_home
            inflateFragment(ContractByCustomerFragment())
        }
    }
}
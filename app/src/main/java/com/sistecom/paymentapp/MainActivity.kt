package com.sistecom.paymentapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sistecom.paymentapp.ui.adapter.PendingOrdersFragment
import com.sistecom.paymentapp.ui.fragment.*
import com.sistecom.paymentapp.utils.PrefManagerHelper
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
        inflateFragment(ContractByCustomerFragment())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        PrefManagerHelper.init(this)
    }

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                inflateFragment(ContractByCustomerFragment())
                navigation.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                /*val bundle = Bundle()
                bundle.apply {
                    putInt("contractId", 1)
                }
                val orderFragment = PendingOrdersFragment()
                orderFragment.arguments = bundle*/
                inflateFragment(PendingOrdersFragment())
                navigation.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                inflateFragment(ReceiptsFragment())
                navigation.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                //inflateFragment(RegisterFragment())
                //inflateFragment(ProfileFragment())
                inflateFragment(LoginFragment())
                //supportFragmentManager.beginTransaction().replace(R.id.frame_content, fragment).commit();
                navigation.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun inflateFragment(fragment: Fragment) {
        selectedFragment = fragment
        supportFragmentManager.beginTransaction().replace(R.id.frame_content, fragment)
                .addToBackStack(fragment.toString())
                .commit();
    }

    override fun onBackPressed() {
        if (selectedFragment is ContractByCustomerFragment) {
            super.onBackPressed()
        } else {
            inflateFragment(ContractByCustomerFragment())
            navigation.visibility = View.VISIBLE
            navigation.selectedItemId = R.id.navigation_home
        }
    }
}
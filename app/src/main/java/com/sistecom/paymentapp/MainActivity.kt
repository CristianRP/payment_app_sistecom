package com.sistecom.paymentapp

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
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

    var selectedFragment: Int? = 0
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        //setupActionBar(navController, appBarConfiguration)

        setupBottomNavMenu(navController)

        PrefManagerHelper.init(this)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottomNav = findViewById(R.id.navigation)
        bottomNav.setupWithNavController(navController)
        bottomNav.itemIconTintList = null
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            if (destination.id == R.id.login_fragment || destination.id == R.id.register_fragment) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
            selectedFragment = destination.id
            Toast.makeText(this@MainActivity, "Navigated to $dest", Toast.LENGTH_SHORT).show()
            Log.d("NavigationActivity", "Navigated to $dest")
        }
    }

    /*private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.contract_customer_fragment -> {
                Toast.makeText(this@MainActivity, "pato", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.pending_orders_fragment -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.receipts_fragment -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile_fragment -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }*/

    private fun setupActionBar(navController: NavController,
                               appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navigation).navigateUp(appBarConfiguration)
    }

    /*private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                inflateFragment(ContractByCustomerFragment())
                navigation.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                *//*val bundle = Bundle()
                bundle.apply {
                    putInt("contractId", 1)
                }
                val orderFragment = PendingOrdersFragment()
                orderFragment.arguments = bundle*//*
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
    }*/

   /* private fun inflateFragment(fragment: Fragment) {
        selectedFragment = fragment
        *//*supportFragmentManager.beginTransaction().replace(R.id.frame_content, fragment)
                .addToBackStack(fragment.toString())
                .commit();*//*
    }*/

    override fun onBackPressed() {
        super.onBackPressed()
        /*if (selectedFragment == R.id.contract_customer_fragment) {
            super.onBackPressed()
        } else {
            findNavController(R.id.nav_host_fragment).popBackStack()
            //findNavController(R.id.nav_host_fragment).getBackStackEntry()

        }*/
    }
}
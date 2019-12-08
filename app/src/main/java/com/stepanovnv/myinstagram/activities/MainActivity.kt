package com.stepanovnv.myinstagram.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.fragments.HomeFragment
import com.stepanovnv.myinstagram.fragments.HotFragment


class MainActivity : AppCompatActivity() {

    private lateinit var _menu: BottomNavigationView
    private lateinit var _homeFragment: Fragment
    private lateinit var _hotFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _menu = findViewById(R.id.bottom_nav_view)
        _homeFragment = HomeFragment()
        _hotFragment = HotFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, _homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        _menu.setOnNavigationItemSelectedListener { menuItem -> onMenuSelected(menuItem) }
    }

    private fun onMenuSelected(menuItem: MenuItem): Boolean {
        val fragment = when (menuItem.itemId) {
            R.id.action_home -> _homeFragment
            R.id.action_hot -> _hotFragment
            else -> null
        }
        fragment ?: return false
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        return true
    }
}

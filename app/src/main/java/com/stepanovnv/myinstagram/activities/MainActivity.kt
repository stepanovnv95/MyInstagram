package com.stepanovnv.myinstagram.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.fragments.HomeFragment
import com.stepanovnv.myinstagram.fragments.HotFragment
import com.stepanovnv.myinstagram.fragments.OtherFragment


class MainActivity : AppCompatActivity() {

    private lateinit var _menu: BottomNavigationView
    private lateinit var _homeFragment: Fragment
    private lateinit var _hotFragment: Fragment
    private lateinit var _otherFragment: Fragment

    companion object {
        private var lastSelectedItemId: Int? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _menu = findViewById(R.id.bottom_nav_view)
        _homeFragment = HomeFragment()
        _hotFragment = HotFragment()
        _otherFragment = OtherFragment()

        if (lastSelectedItemId == null)
            lastSelectedItemId = R.id.action_home

        _menu.setOnNavigationItemSelectedListener { menuItem -> onMenuSelected(menuItem) }
        _menu.selectedItemId = lastSelectedItemId!!
    }

    private fun onMenuSelected(menuItem: MenuItem): Boolean {
        val fragment = when (menuItem.itemId) {
            R.id.action_home -> _homeFragment
            R.id.action_hot -> _hotFragment
            R.id.action_other -> _otherFragment
            else -> null
        }
        fragment ?: return false
        lastSelectedItemId = menuItem.itemId
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        return true
    }
}

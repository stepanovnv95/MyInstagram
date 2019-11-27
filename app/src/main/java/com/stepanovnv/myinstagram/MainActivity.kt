package com.stepanovnv.myinstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val _tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomMenu()
    }

    private fun setupBottomMenu() {
        bottom_nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    homeTextView.visibility = View.VISIBLE
                    favoritesTextView.visibility = View.GONE
                }
                R.id.action_favorites -> {
                    homeTextView.visibility = View.GONE
                    favoritesTextView.visibility = View.VISIBLE
                }
                else -> {
                    Log.e(_tag, "Unknown menu item: $it")
                }
            }
            true
        }
    }
}

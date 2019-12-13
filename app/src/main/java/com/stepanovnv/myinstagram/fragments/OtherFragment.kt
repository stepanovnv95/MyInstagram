package com.stepanovnv.myinstagram.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.activities.AboutActivity
import com.stepanovnv.myinstagram.activities.ProfileActivity


class OtherFragment : Fragment() {

    private lateinit var _profileButton: View
    private lateinit var _rateButton: View
    private lateinit var _aboutButton: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_other, container, false)
        activity!!.title = getString(R.string.title_other)

        _profileButton = view.findViewById(R.id.profile_button)
        _rateButton = view.findViewById(R.id.rate_button)
        _aboutButton = view.findViewById(R.id.about_button)

        _profileButton.setOnClickListener { onProfileButtonClicked() }
        _rateButton.setOnClickListener { onRateButtonClicked() }
        _aboutButton.setOnClickListener { onAboutButtonClicked() }

        return view
    }

    private fun onProfileButtonClicked() {
        val intent = Intent(activity, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun onRateButtonClicked() {

    }

    private fun onAboutButtonClicked() {
        val intent = Intent(activity, AboutActivity::class.java)
        startActivity(intent)
    }

}

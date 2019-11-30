package com.stepanovnv.myinstagram

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class HomeFragment : ListFragment() {

    private var _swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        _swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        refresh()
        return view
    }

    private fun refresh() {
        _swipeRefreshLayout?.isRefreshing = true

        val url = "http://v-wall.net/ibcon/auth.php?response_format=json"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("HomeFragment", "Response: %s".format(response.toString()))
            },
            Response.ErrorListener { error ->
                Log.d("HomeFragment", "Error: %s".format(error.toString()))
            }
        )
        val queue = Volley.newRequestQueue(activity as Context)
        queue.add(jsonObjectRequest)
    }

}

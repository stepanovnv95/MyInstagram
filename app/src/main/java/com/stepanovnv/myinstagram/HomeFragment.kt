package com.stepanovnv.myinstagram

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


class HomeFragment : ListFragment() {

    private val _tag = "HomeFragment"
    private var _swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        _swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)

//        refresh()

        return view
    }

    private fun refresh() {
        val context = activity?.applicationContext ?: return

        _swipeRefreshLayout?.isRefreshing = true

        val jsonRequest = JsonObjectRequest(Request.Method.GET, "", null,
            Response.Listener {response ->
                _swipeRefreshLayout?.isRefreshing = false
            },
            Response.ErrorListener { error ->
                Log.e(_tag, error.toString())
                _swipeRefreshLayout?.isRefreshing = false
            }
        )
        jsonRequest.tag = _tag

        val queue = HttpManager.getInstance(context).requestQueue
        queue.add(jsonRequest)
    }

}

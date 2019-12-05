package com.stepanovnv.myinstagram.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stepanovnv.myinstagram.PostData
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.HomeRequest
import org.json.JSONObject


class HomeFragment : ListFragment() {

    private val _tag = "HomeFragment"
    private val _postsData = ArrayList<PostData>()
    private var _httpClient: HttpClient? = null
    private var _swipeRefreshLayout: SwipeRefreshLayout? = null
    private var _refreshHint: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        findWidgets(view)

        if (_httpClient == null) {
            val context = activity?.applicationContext ?: throw Exception("Can't get the context")
            _httpClient = HttpClient(_tag, context)
        }

        _swipeRefreshLayout?.setOnRefreshListener { this.onRefresh() }

        loadPostData()

        return view
    }

    private fun findWidgets(view: View) {
        if (_swipeRefreshLayout == null)
            _swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        if (_refreshHint == null)
            _refreshHint = view.findViewById(R.id.refresh_hint)

    }

    private fun onRefresh() {
        loadPostData()
    }

    private fun loadPostData() {
        onLoadingBegin()
        _httpClient?.addRequest(
            HomeRequest(4),
            {response -> this.onHttpResponse(response)},
            {error -> this.onHttpError(error)}
        )
    }

    private fun onLoadingBegin() {
        if (_postsData.size == 0 )
            _swipeRefreshLayout?.isRefreshing = true
        _refreshHint?.visibility = View.GONE
    }

    private fun onHttpResponse(response: JSONObject) {
        Log.d(_tag, response.toString())
        onLoadingFinished()
    }

    private fun onHttpError(error: String) {
        Log.d(_tag, error)
        onLoadingFinished()
    }

    private fun onLoadingFinished() {
        _swipeRefreshLayout?.isRefreshing = false
        if (_postsData.size == 0 )
            _refreshHint?.visibility = View.VISIBLE
    }

}

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
import org.json.JSONObject


class HomeFragment : ListFragment() {

    private val _tag = "HomeFragment"
    private val _postsData = ArrayList<PostData>()
    private var _swipeRefreshLayout: SwipeRefreshLayout? = null
    private var _httpClient: HttpClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        if (_swipeRefreshLayout == null) {
            _swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        }
        if (_httpClient == null) {
            val context = activity?.applicationContext ?: throw Exception("Can't get the context")
            _httpClient = HttpClient(_tag, context)
        }

        loadPostData()

        return view
    }

    private fun loadPostData() {
        if (_postsData.size == 0 )
            _swipeRefreshLayout?.isRefreshing = true

        if (_httpClient == null) throw Exception("HttpClient not created")
        _httpClient?.request(
            1,
            {response -> this.onHttpResponse(response)},
            {error -> this.onHttpError(error)}
        )
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
    }

}

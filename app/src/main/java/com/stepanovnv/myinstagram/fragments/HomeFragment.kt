package com.stepanovnv.myinstagram.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.adapters.PostAdapter
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.HomeRequest
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : ListFragment() {

    private val _tag = "HomeFragment"
    private val _postsData = ArrayList<PostData>()

    private var _context: Context? = null
    private var _httpClient: HttpClient? = null
    private var _listAdapter: PostAdapter? = null
    private var _swipeRefreshLayout: SwipeRefreshLayout? = null
    private var _listView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _context = activity?.applicationContext ?: throw Exception("Can't get the context")
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        findWidgets(view)

        if (_httpClient == null) {
            _httpClient = HttpClient(_tag, _context as Context)
        }

        _listView?.setOnScrollListener(object: AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (_listView?.getChildAt(0) != null) {
                    _swipeRefreshLayout?.isEnabled =
                        _listView?.firstVisiblePosition == 0 &&
                        _listView?.getChildAt(0)!!.top == 0
                }
            }
        })

        _swipeRefreshLayout?.setOnRefreshListener { this.reloadPosts() }

//        reloadPosts()

        return view
    }

    private fun findWidgets(view: View) {
        if (_swipeRefreshLayout == null)
            _swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        if (_listView == null)
            _listView = view.findViewById(android.R.id.list)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _listAdapter = PostAdapter(activity as Context, android.R.layout.simple_list_item_1, _postsData)
        listAdapter = _listAdapter
    }

    private fun reloadPosts() {
        _postsData.clear()
        loadPostData()
    }

    private fun loadPostData() {
        onLoadingBegin()
        _httpClient?.addRequest(
            HomeRequest(
                _context,
                null,
                {response -> this.onHttpResponse(response)},
                {error -> this.onHttpError(error)}
                )
        )
    }

    private fun onLoadingBegin() {
        if (_postsData.size == 0 )
            _swipeRefreshLayout?.isRefreshing = true
    }

    private fun onHttpResponse(response: JSONObject) {
        Log.d(_tag, response.toString())
        parseJsonResponse(response)
        onLoadingFinished()
    }

    private fun onHttpError(error: String) {
        Log.e(_tag, error)
        onLoadingFinished()
    }

    private fun onLoadingFinished() {
        _listAdapter?.notifyDataSetChanged()
        _swipeRefreshLayout?.isRefreshing = false
    }

    private fun parseJsonResponse(json: JSONObject) {
        val posts = json.optJSONArray("posts") ?: return
        for (i in 0 until posts.length()) {
            val jsonPost = posts.optJSONObject(i) ?: continue
            try {
                _postsData.add(PostData.fromJson(jsonPost))
            } catch (e: JSONException) {
                Log.e(_tag, e.toString())
            }
        }
    }

}
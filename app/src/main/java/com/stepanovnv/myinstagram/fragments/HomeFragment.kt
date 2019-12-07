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
    private val _postsIds = HashSet<Int>()  // for don't add exist post to list view

    private var _context: Context? = null
    private var _httpClient: HttpClient? = null
    private var _listAdapter: PostAdapter? = null
    private var _swipeRefreshLayout: SwipeRefreshLayout? = null
    private var _refreshHint: View? = null
    private var _listView: ListView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _context = activity?.applicationContext ?: throw Exception("Can't get the context")
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        findWidgets(view)

        if (_httpClient == null) {
            _httpClient = HttpClient(_tag, _context as Context)
        }

        configureListView()

        _swipeRefreshLayout?.setOnRefreshListener { this.reloadPosts() }

        reloadPosts()

        return view
    }

    private fun findWidgets(view: View) {
        if (_swipeRefreshLayout == null)
            _swipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
        if (_listView == null)
            _listView = view.findViewById(android.R.id.list)
        if (_refreshHint == null)
            _refreshHint = view.findViewById(R.id.refresh_hint)
    }

    private fun configureListView() {
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _listAdapter = PostAdapter(activity as Context, android.R.layout.simple_list_item_1, _postsData)
        _listAdapter!!.onEnd = { loadPostData() }
        listAdapter = _listAdapter
    }

    private fun reloadPosts() {
        _postsData.clear()
        _postsIds.clear()
        _listAdapter?.notifyDataSetChanged()
        loadPostData()
    }

    private fun loadPostData() {
        onLoadingBegin()
        _httpClient?.addRequest(
            HomeRequest(
                _context,
                if (_postsData.size > 0) _postsData.last().id else null ,
                {response -> this.onHttpResponse(response)},
                {error -> this.onHttpError(error)}
            )
        )
    }

    private fun onLoadingBegin() {
        if (_postsData.size == 0 )
            _swipeRefreshLayout?.isRefreshing = true
        _refreshHint?.visibility = View.GONE
    }

    private fun onHttpResponse(response: JSONObject) {
        Log.d(_tag, response.toString())
        if (parseJsonResponse(response))
            _listAdapter?.notifyDataSetChanged()
        onLoadingFinished()
    }

    private fun onHttpError(error: String) {
        Log.e(_tag, error)
        onLoadingFinished()
    }

    private fun onLoadingFinished() {
        _swipeRefreshLayout?.isRefreshing = false
        if (_postsData.size == 0)
            _refreshHint?.visibility = View.VISIBLE
    }

    private fun parseJsonResponse(json: JSONObject): Boolean {
        val posts = json.optJSONArray("posts") ?: return false
        var dataChanged = false
        for (i in 0 until posts.length()) {
            val jsonPost = posts.optJSONObject(i) ?: continue
            try {
                val pd = PostData.fromJson(jsonPost)
                if (_postsIds.add(pd.id)) {   // for don't add exist post to list view
                    _postsData.add(pd)
                    dataChanged = true
                }
            } catch (e: JSONException) {
                Log.e(_tag, e.toString())
            }
        }
        return dataChanged
    }

}
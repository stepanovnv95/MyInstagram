package com.stepanovnv.myinstagram.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.adapters.PostListAdapter
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.PostRequest
import org.json.JSONException
import org.json.JSONObject


abstract class BaseListFragment : Fragment() {

    @Suppress("PropertyName")
    protected abstract val TAG: String

    protected val postsArray = LinkedHashSet<PostData>()
    private val _adapter = PostListAdapter(postsArray)

    private lateinit var _appContext: Context  // TODO("Maybe remove context?")
    private lateinit var _httpClient: HttpClient
    private var _loading = false

    private var _view: View? = null

    // widgets
    private lateinit var _refreshView: SwipeRefreshLayout
    private var _listView: RecyclerView? = null
    private lateinit var _emptyHintView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (_view != null)
            return _view

        _appContext = activity!!.applicationContext

        _view = inflater.inflate(R.layout.fragment_base_list, container, false)
        activity!!.title = getTitle()

        _refreshView = _view!!.findViewById(R.id.refresh)
        _emptyHintView = _view!!.findViewById(R.id.empty)
        if (_listView == null) {
            _adapter.setHasStableIds(true)
        } else {
            _listView!!.adapter = null
        }
        _listView = _view!!.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }
        val divider = ContextCompat.getDrawable(_listView!!.context, R.drawable.list_divider)!!
        val itemDecoration = DividerItemDecoration(_listView!!.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(divider)
        _listView!!.addItemDecoration(itemDecoration)
        _adapter.onEndScrolled = { loadPostData() }

        _httpClient = HttpClient(TAG, _appContext)

        _refreshView.setOnRefreshListener { reloadPostData() }
        if (postsArray.isEmpty())
            loadPostData()
        else
            onLoadingFinished()

        return _view
    }

    protected abstract fun constructHttpRequest(): PostRequest
    protected abstract fun getTitle(): String

    private fun reloadPostData() {
        val size = postsArray.size
        postsArray.clear()
        _adapter.myNotifyItemRangeRemoved(0, size)
        loadPostData()
    }

    private fun loadPostData() {
        if (_loading)
            return
        else
            _loading = true
        onLoadingBegin()
        val request = constructHttpRequest()
        request.onResponse = { response -> onHttpResponse(response) }
        request.onError = { error -> onError(error)}
        _httpClient.addRequest(request)
    }

    private fun onLoadingBegin() {
        if (postsArray.size == 0)
            _refreshView.isRefreshing = true
        _emptyHintView.visibility = View.GONE
    }

    private fun onHttpResponse(jsonObject: JSONObject) {
        Log.d(TAG, jsonObject.toString())
        parseJsonResponse(jsonObject)
        onLoadingFinished()
    }

    private fun onError(error: String) {
        Log.e(TAG, error)
        onLoadingFinished()
    }

    private fun onLoadingFinished() {
        _refreshView.isRefreshing = false
        if (postsArray.size == 0)
            _emptyHintView.visibility = View.VISIBLE
        else
            _emptyHintView.visibility = View.GONE
        _loading = false
    }

    private fun parseJsonResponse(jsonObject: JSONObject) {
        val posts = jsonObject.optJSONArray("posts") ?: return
        val sizeBefore = postsArray.size
        for (i in 0 until posts.length()) {
            val jsonPost = posts.optJSONObject(i) ?: continue
            try {
                val postDate = PostData.fromJson(_appContext ,jsonPost)
                postsArray.add(postDate)
            } catch (e: JSONException) {
                Log.e(TAG, e.toString())
            }
        }
        _adapter.myNotifyItemRangeInserted(sizeBefore, postsArray.size - sizeBefore)
    }

}

package com.stepanovnv.myinstagram.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.adapters.MyPostAdapter
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.http.HttpClient


abstract class BaseListFragment : Fragment() {

    @Suppress("PropertyName")
    protected abstract val TAG: String

    private val _postsArray = LinkedHashSet<PostData>()
    private val _adapter = MyPostAdapter(_postsArray)

    private lateinit var _context: Context  // TODO("Maybe remove context?")
    private lateinit var _httpClient: HttpClient

    // widgets
    private lateinit var _refreshView: SwipeRefreshLayout
    private lateinit var _listView: RecyclerView
    private lateinit var _emptyHintView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _context = activity!!.applicationContext

        val view = inflater.inflate(R.layout.fragment_base_list, container, false)
        _refreshView = view.findViewById(R.id.refresh)
        _emptyHintView = view.findViewById(R.id.empty)
        _listView = view.findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(_context)
            adapter = _adapter
        }

        _httpClient = HttpClient(TAG, _context)

        return view
    }

}

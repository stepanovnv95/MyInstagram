package com.stepanovnv.myinstagram.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.views.LoadingView
import com.stepanovnv.myinstagram.views.PostView


class PostAdapter(context: Context, resource: Int, objects: List<PostData>) :
    ArrayAdapter<PostData>(context, resource, objects) {

    private val _context: Context = context
    private val _array: List<PostData> = objects
    private var _loadingView: LoadingView? = null
    var onEnd: ((LoadingView?) -> Unit)? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        if ( (position == count - 1) && (onEnd != null) )
            onEnd?.invoke(_loadingView)

        return if (position < _array.size) constructPostView(position, convertView)
        else constructLoadingView(convertView)
    }

    override fun getCount(): Int {
        return if (_array.isEmpty()) 0 else _array.size + 1
    }

    private fun constructPostView(position: Int, convertView: View?): View {
        val row: PostView = when (convertView) {
            is PostView -> convertView
            else -> PostView(_context)
        }
        row.postData = _array[position]
        return row
    }

    private fun constructLoadingView(@Suppress("UNUSED_PARAMETER") convertView: View?): View {
        _loadingView = LoadingView(_context)
        return _loadingView!!
    }

}
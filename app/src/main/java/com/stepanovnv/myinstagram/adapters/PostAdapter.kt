package com.stepanovnv.myinstagram.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.views.PostView


class PostAdapter(context: Context, resource: Int, objects: List<PostData>) :
    ArrayAdapter<PostData>(context, resource, objects) {

    private val _context: Context = context
    private val _array: List<PostData> = objects

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = PostView(_context)
        view.postData = _array[position]
        return view
    }

    override fun getCount(): Int {
        return _array.count()
    }

}
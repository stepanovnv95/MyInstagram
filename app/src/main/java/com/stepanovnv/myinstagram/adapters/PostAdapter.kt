package com.stepanovnv.myinstagram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.PostData


class PostAdapter(context: Context, resource: Int, objects: List<PostData>) :
    ArrayAdapter<PostData>(context, resource, objects) {

    private val _context: Context = context
    private val _array: List<PostData> = objects

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.fragment_row_test, parent, false)
        val textView = row.findViewById<TextView>(R.id.textView)
        textView.text = _array[position].url
        return row
    }

    override fun getCount(): Int {
        return _array.count()
    }

}
package com.stepanovnv.myinstagram.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.PostData


// TODO("Rename adapter and holder")
class MyPostAdapter(private val _dataset: Collection<PostData>)
    : RecyclerView.Adapter<MyPostAdapter.MyViewHolder>() {

    class MyViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_base_item, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return _dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.text = _dataset.elementAt(position).url
    }

}

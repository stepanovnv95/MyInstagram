package com.stepanovnv.myinstagram.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.views.PostView


// TODO("Rename adapter and holder")
class MyPostAdapter(private val _dataset: Collection<PostData>)
    : RecyclerView.Adapter<MyPostAdapter.MyViewHolder>() {

    class MyViewHolder(val view: PostView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val postView = PostView(parent.context)
        return MyViewHolder(postView)
    }

    override fun getItemCount(): Int {
        return _dataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.view.postData = _dataset.elementAt(position)
    }

}

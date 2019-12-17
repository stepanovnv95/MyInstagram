package com.stepanovnv.myinstagram.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.Comment


class CommentsListAdapter(private val _dataset: Collection<Comment>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return _dataset.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.username_textview)
            .text = _dataset.elementAt(position).username
        holder.itemView.findViewById<TextView>(R.id.date_textview)
            .text = _dataset.elementAt(position).date
        holder.itemView.findViewById<TextView>(R.id.text_textview)
            .text = _dataset.elementAt(position).text
    }

}

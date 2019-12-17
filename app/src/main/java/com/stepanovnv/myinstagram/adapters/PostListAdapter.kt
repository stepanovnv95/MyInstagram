package com.stepanovnv.myinstagram.adapters


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.views.AdmobView
import com.stepanovnv.myinstagram.views.PostView


@Suppress("PrivatePropertyName")
class PostListAdapter(private val _dataset: Collection<PostData>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PostViewHolder(val view: PostView) : RecyclerView.ViewHolder(view)
    class AdmobViewHolder(view: AdmobView) : RecyclerView.ViewHolder(view)
    private val _postsForAd = 12

    var onEndScrolled: (() -> Unit)? = null

    private val VIEW_TYPE_POST      = 0
    private val VIEW_TYPE_ADMOB   = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_POST)
            return PostViewHolder(PostView(parent.context))
        return AdmobViewHolder(AdmobView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val i = adapterIndexToArray(position)
        if (holder is PostViewHolder) {
            holder.view.postData = _dataset.elementAt(i)
        }
        if (position == itemCount - 1)
            onEndScrolled?.invoke()
    }

    private fun adapterIndexToArray(i: Int): Int {
        return i - i / (_postsForAd + 1)
    }
    private fun arrayIndexToAdapter(i: Int): Int {
        return i + i / _postsForAd
    }

    override fun getItemCount(): Int {
        val datasetSize = _dataset.size
        val ads = datasetSize / _postsForAd
        return datasetSize + ads
    }

    override fun getItemViewType(position: Int): Int {
        if ( (position + 1) % (_postsForAd + 1) == 0 )
            return VIEW_TYPE_ADMOB
        return VIEW_TYPE_POST
    }

    override fun getItemId(position: Int): Long {
        return if (getItemViewType(position) == VIEW_TYPE_POST)
            _dataset.elementAt(adapterIndexToArray(position)).id.toLong()
        else
            -position.toLong()
    }

    fun myNotifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        val start = arrayIndexToAdapter(positionStart)
        val end = arrayIndexToAdapter(positionStart + itemCount)
        val count = end - start
        notifyItemRangeChanged(start, count)
    }

    fun myNotifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        val start = arrayIndexToAdapter(positionStart)
        val end = arrayIndexToAdapter(positionStart + itemCount)
        val count = end - start
        notifyItemRangeRemoved(start, count)
    }
}

package com.stepanovnv.myinstagram.adapters


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stepanovnv.myinstagram.data.PostData
import com.stepanovnv.myinstagram.views.PostView


// TODO("Rename adapter and holder")
@Suppress("PrivatePropertyName")
class MyPostAdapter(private val _dataset: Collection<PostData>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PostViewHolder(val view: PostView) : RecyclerView.ViewHolder(view)
//    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var onEndScrolled: (() -> Unit)? = null

//    private val VIEW_TYPE_POST      = 0
//    private val VIEW_TYPE_LOADING   = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            VIEW_TYPE_POST -> {
//                PostViewHolder(PostView(parent.context))
//            }
//            VIEW_TYPE_LOADING -> {
//                val loadingView = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.item_loading, parent, false)
//                LoadingViewHolder(loadingView)
//            }
//            else -> throw Exception("Unknown view type")
//        }
        return PostViewHolder(PostView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is PostViewHolder -> {
//                holder.view.postData = _dataset.elementAt(position)
//            }
//            is LoadingViewHolder -> {
//                onEndScrolled?.invoke()
//            }
//        }
        (holder as PostViewHolder).view.postData = _dataset.elementAt(position)
        if (position == itemCount - 1)
            onEndScrolled?.invoke()
    }

    override fun getItemCount(): Int {
//        return if (_dataset.isEmpty()) 0 else _dataset.size + 1
        return _dataset.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return if (position == itemCount - 1) VIEW_TYPE_LOADING else VIEW_TYPE_POST
//    }

}

package com.stepanovnv.myinstagram.fragments

import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.http.requests.HomeRequest
import com.stepanovnv.myinstagram.http.requests.PostRequest


class HotFragment : BaseListFragment() {

    override val TAG = "HotFragment"
    override fun constructHttpRequest(): PostRequest {
        return HomeRequest(
            context,
            if (postsArray.size > 0) postsArray.last().id else null
        )
    }

    override fun getTitle(): String {
        return getString(R.string.title_hot)
    }

}

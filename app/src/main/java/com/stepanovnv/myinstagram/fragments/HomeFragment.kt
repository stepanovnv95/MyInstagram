package com.stepanovnv.myinstagram.fragments

import com.stepanovnv.myinstagram.http.requests.HomeRequest
import com.stepanovnv.myinstagram.http.requests.PostRequest


class HomeFragment : BaseListFragment() {

    override val TAG = "HomeFragment"
    override fun constructHttpRequest(): PostRequest {
        return HomeRequest(
            context,
            if (postsArray.size > 0) postsArray.last().id else null
        )
    }

}

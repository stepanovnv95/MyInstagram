package com.stepanovnv.myinstagram.http.requests

import android.content.Context
import android.graphics.Bitmap


class ImageRequest(context: Context, imageUrl: String, val onResponse: (Bitmap) -> Unit, onError: (String) -> Unit)
    : AbstractRequest(context, onError) {
    override val url: String = imageUrl
}

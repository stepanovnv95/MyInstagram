package com.stepanovnv.myinstagram.http

import android.content.Context
import android.graphics.Bitmap
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.stepanovnv.myinstagram.http.requests.AbstractRequest
import com.stepanovnv.myinstagram.http.requests.ImageRequest as MyImageRequest
import com.stepanovnv.myinstagram.http.requests.PostRequest
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception


class HttpClient(tag: String, context: Context) {

    private val _tag: String = "%s_%s".format("HttpClient", tag)
    private val _context: Context = context

    fun addRequest(request: AbstractRequest) {
        when (request) {
            is PostRequest -> addPostRequest(request)
            is MyImageRequest -> addImageRequest(request)
            else -> throw Exception("Unknown request type")
        }
    }

    private fun addPostRequest(request: PostRequest) {
        val r = object : StringRequest(
            Method.POST, request.url,
            Response.Listener { response ->
                try {
                    request.onResponse?.invoke(JSONObject(response))
                } catch (e: JSONException) {
                    request.onError?.invoke("Can't parse to json: %s".format(response.toString()))
                }
            },
            Response.ErrorListener { error ->
                request.onError?.invoke(error.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return request.params as MutableMap<String, String>
            }
        }
        r.tag = _tag
        r.setShouldCache(false)
        val queue = HttpQueueSingleton.getInstance(_context).requestQueue
        queue.add(r)
    }

    private fun addImageRequest(request: MyImageRequest) {
        val r = ImageRequest(
            request.url,
            Response.Listener { response: Bitmap -> request.onResponse(response) },
            0, 0, null,
            Bitmap.Config.RGB_565,
            Response.ErrorListener { error -> request.onError?.invoke(error.toString()) }
        )
        val queue = HttpQueueSingleton.getInstance(_context).requestQueue
        queue.add(r)
    }

}

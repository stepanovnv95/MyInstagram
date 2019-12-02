package com.stepanovnv.myinstagram.http

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class HttpClient(tag: String, context: Context) {

    private val _tag: String = "%s_%s".format("HttpClient", tag)
    private val _context: Context = context

    fun request(a: Any, onResponse: (JSONObject) -> Unit, onError: (String) -> Unit) {
        val request = JsonObjectRequest(Request.Method.GET, "", null,
            Response.Listener { response -> onResponse(response) },
            Response.ErrorListener { error -> onError(error.toString()) }
        )
        request.tag = _tag
        val queue = HttpQueueSingleton.getInstance(_context).requestQueue
        queue.add(request)
    }
}

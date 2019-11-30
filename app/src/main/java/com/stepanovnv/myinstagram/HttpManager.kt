package com.stepanovnv.myinstagram

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class HttpManager constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: HttpManager? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HttpManager(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

}
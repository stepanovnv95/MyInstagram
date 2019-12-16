package com.stepanovnv.myinstagram.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.stepanovnv.myinstagram.R


class CommentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("CommentsActivity", "onBackPressed")
    }
}

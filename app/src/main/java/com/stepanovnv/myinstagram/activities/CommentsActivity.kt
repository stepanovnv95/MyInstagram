package com.stepanovnv.myinstagram.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.adapters.CommentsListAdapter
import com.stepanovnv.myinstagram.data.Comment
import com.stepanovnv.myinstagram.data.MyInstagramDatabaseSingleton
import com.stepanovnv.myinstagram.data.UserDao
import com.stepanovnv.myinstagram.data.UserMeta
import com.stepanovnv.myinstagram.http.HttpClient
import com.stepanovnv.myinstagram.http.requests.AddCommentRequest
import com.stepanovnv.myinstagram.http.requests.GetCommentsRequest
import org.json.JSONObject


@Suppress("PrivatePropertyName")
class CommentsActivity : AppCompatActivity() {

    private val TAG = "CommentsActivity"
    private val _commentsArray = ArrayList<Comment>()
    private val _commentsListAdapter = CommentsListAdapter(_commentsArray)
    private lateinit var _commentsListView: RecyclerView
    private lateinit var _commentTextView: TextView
    private lateinit var _commentAddButton: View
    private var _postId: Int = 0
    private lateinit var _httpClient: HttpClient
    private lateinit var _userDao: UserDao
    private var _commentText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        _commentsListView = findViewById<RecyclerView>(R.id.list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = _commentsListAdapter
        }
        _commentTextView = findViewById(R.id.comment_textview)
        _commentAddButton = findViewById(R.id.add_comment_button)
        _commentAddButton.setOnClickListener { addComment() }
        _httpClient = HttpClient(TAG, applicationContext)
        _postId = intent.getIntExtra("POST_ID", -1)
        if (_postId < 0) {
            throw Exception("Invalid post id")
        }
        Log.d(TAG, "Comments for post %d".format(_postId))
        _httpClient.addRequest(GetCommentsRequest(
            this,
            _postId,
            { response -> onCommentsLoaded(response) },
            { error -> onLoadingError(error) }
        ))
        _userDao = MyInstagramDatabaseSingleton.getInstance(applicationContext).db.userDao()
    }

    private fun onCommentsLoaded(jsonData: JSONObject) {
        Log.d(TAG, jsonData.toString())
        val commentsArray = jsonData.optJSONArray("comments") ?: return
        for (i in 0 until commentsArray.length()) {
            val comment = commentsArray.optJSONObject(i) ?: continue
            val username = comment.optString("user_name") ?: continue
            val text = comment.optString("comment") ?: continue
            val date = comment.optString("date") ?: continue
            _commentsArray.add(Comment(username, text, date))
        }
        _commentsListAdapter.notifyItemRangeInserted(0, _commentsArray.size)
    }

    private fun onLoadingError(error: String) {
        Log.e(TAG, error)
    }

    private fun addComment() {
        _commentText = _commentTextView.text.toString()
        Log.d(TAG, _commentText)

        if (_commentText == "") return

        val userIdMeta: UserMeta? = _userDao.getKey("user_id")
        val userNameMeta: UserMeta? = _userDao.getKey("username")
        if (userIdMeta == null || userNameMeta == null ||
                userIdMeta.value == "" || userNameMeta.value == "") {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivityForResult(intent, 0)
            return
        }
        val userId = userIdMeta.value
        val userName = userNameMeta.value
        _httpClient.addRequest(AddCommentRequest(
            this,
            _postId,
            userId,
            userName,
            _commentText,
            { onCommentAdded(/*response*/) },
            { error -> onAddCommentError(error) }
        ))
    }

    private fun onCommentAdded(/*jsonObject: JSONObject*/) {
        val myComment = Comment(
            _userDao.getKey("username")!!.value,
            _commentText,
            "current date"
        )
        _commentsArray.add(myComment)
        _commentsListAdapter.notifyItemRangeInserted(_commentsArray.size - 1, 1)
    }

    private fun onAddCommentError(error: String) {
        Log.e(TAG, error)
        _commentTextView.text = _commentText
    }
}

package com.stepanovnv.myinstagram.activities

import android.content.Intent
import android.graphics.LightingColorFilter
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.stepanovnv.myinstagram.R
import com.stepanovnv.myinstagram.data.MyInstagramDatabaseSingleton
import com.stepanovnv.myinstagram.data.UserDao
import com.stepanovnv.myinstagram.data.UserMeta


@Suppress("PrivatePropertyName")
class ProfileActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 0
    private val TAG = "ProfileActivity"
    private lateinit var _loginUi: View
    private lateinit var _profileUi: View
    private lateinit var _logoutButton: Button
    private lateinit var _signInButton: SignInButton
    private lateinit var _googleSignInClient: GoogleSignInClient
    private lateinit var _saveButton: Button
    private lateinit var _usernameInput: TextView

    private lateinit var _userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        _userDao = MyInstagramDatabaseSingleton.getInstance(applicationContext).db.userDao()

        _loginUi = findViewById(R.id.login_ui)
        _profileUi = findViewById(R.id.profile_ui)
        _logoutButton = findViewById(R.id.logout_button)
        _signInButton = findViewById(R.id.sign_in_button)
        _saveButton = findViewById(R.id.save_button)
        _usernameInput = findViewById(R.id.username)

        _saveButton.background.colorFilter = LightingColorFilter(
            0x000000,
            ContextCompat.getColor(this, R.color.colorAccent)
        )

        _signInButton.setOnClickListener { onSingButtonClick() }
        _logoutButton.setOnClickListener { onLogoutClick() }
        _saveButton.setOnClickListener {
            val newUsername = _usernameInput.text.toString()
            if (newUsername != "")
                updateUsername(newUsername)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        _googleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
        if (!isSigned())
            updateUsername(account?.displayName)
        else
            _usernameInput.text = _userDao.getKey("username")?.value
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            _loginUi.visibility = View.GONE
            _profileUi.visibility = View.VISIBLE
        } else {
            _loginUi.visibility = View.VISIBLE
            _profileUi.visibility = View.GONE
        }
    }

    private fun isSigned(): Boolean {
        val username = _userDao.getKey("username")?.value
        return !(username == null || username == "")
    }

    private fun updateUsername(username: String?) {
        if (_userDao.getKey("username") == null)
            _userDao.insert(UserMeta(0, "username", ""))
        _userDao.updateKey("username", username ?: "")

        _usernameInput.text = _userDao.getKey("username")?.value ?: ""

        val toast = Toast.makeText(
            applicationContext, getString(R.string.saved_toast)
                .format(_usernameInput.text.toString()),
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    private fun onSingButtonClick() {
        val signInIntent = _googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun onLogoutClick() {
        _googleSignInClient.signOut()
        updateUI(null)
        updateUsername(null)
        if (_userDao.getKey("user_id") == null)
            _userDao.insert(UserMeta(1, "user_id", ""))
        _userDao.updateKey("user_id", "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task!!.getResult(ApiException::class.java)
            updateUI(account)
            updateUsername(account!!.displayName)
            if (_userDao.getKey("user_id") == null)
                _userDao.insert(UserMeta(1, "user_id", ""))
            _userDao.updateKey("user_id", account.id.toString())
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code = %s".format(e.statusCode))
            updateUI(null)
            updateUsername(null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

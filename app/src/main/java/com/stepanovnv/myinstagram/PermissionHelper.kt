package com.stepanovnv.myinstagram

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionHelper {

    companion object {

        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 322

        fun check_WRITE_EXTERNAL_STORAGE(context: Context): Boolean {
            val currentApiVersion = Build.VERSION.SDK_INT
            if (currentApiVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )) {
                        showDialog(
                            "External storage",
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    } else {
                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    }
                    return false
                } else return true
            } else return true
        }

        @Suppress("SameParameterValue")
        private fun showDialog(msg: String, context: Context, permission: String) {
            val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle("Permission necessary")
            alertBuilder.setMessage("$msg permission is necessary")
            alertBuilder.setPositiveButton(
                android.R.string.yes
            ) { _, _ ->
                ActivityCompat.requestPermissions(
                    (context as Activity), arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
            val alert: AlertDialog = alertBuilder.create()
            alert.show()
        }

    }

}

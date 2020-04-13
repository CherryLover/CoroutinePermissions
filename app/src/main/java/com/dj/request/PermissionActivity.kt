package com.dj.request

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dj.coroutines.permisstions.requestPermissionsForResult
import kotlinx.android.synthetic.main.activity_permission.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @description
 * @author: Created jiangjiwei in 2020/4/13 10:32
 */
class PermissionActivity : AppCompatActivity() {
    private val permissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    companion object {
        const val tag = "PermissionActivity"

        fun start(context: Context) {
            val stater = Intent(context, PermissionActivity::class.java)
            context.startActivity(stater)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        btnActRequire.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                requestPermissionsForResult(
                    fun(allGranted: Boolean, grantedList: MutableList<String>) {
                        Log.d(tag, "allGranted: $allGranted grantedList: $grantedList")
                    },
                    fun(allDenied: Boolean, deniedList: MutableList<String>) {
                        Log.d(tag, "allDenied: $allDenied deniedList: $deniedList")
                    },
                    fun(permissionList: MutableList<String>) {
                        permissionList.forEach {
                            Log.e(tag, "I need $it for xxxxxx")
                        }
                    },
                    *permissions
                )
            }
        }
    }
}
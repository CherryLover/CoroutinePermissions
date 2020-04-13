package com.dj.request

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dj.coroutines.permisstions.requestPermissionsForResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermissionFragment:Fragment(){
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    companion object {
        const val logt = "PermissionFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_permissions,container,false)
        initView()
        return view
    }
    private fun initView(){
        CoroutineScope(Dispatchers.Main).launch {
            requestPermissionsForResult(
                fun(allGranted: Boolean, grantedList: MutableList<String>) {
                    Log.d(logt, "allGranted: $allGranted grantedList: $grantedList")
                },
                fun(allDenied: Boolean, deniedList: MutableList<String>) {
                    Log.d(logt, "allDenied: $allDenied deniedList: $deniedList")
                },
                fun(permissionList: MutableList<String>) {
                    permissionList.forEach {
                        Log.e(logt, "I need $it for xxxxxx")
                    }
                },
                *permissions
            )
        }
    }
}
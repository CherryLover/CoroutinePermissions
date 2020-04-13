package com.dj.coroutines.permisstions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dj.coroutines.permisstions.callbacks.FailCallback
import com.dj.coroutines.permisstions.callbacks.ShowRationaleCallback
import com.dj.coroutines.permisstions.callbacks.SuccessCallback
import kotlin.coroutines.suspendCoroutine

suspend fun FragmentActivity.requestPermissionsForResult(
    permissions: Array<String>,
    granted: (allGranted: Boolean, grantedList: MutableList<String>) -> Unit,
    denied: ((allGranted: Boolean, grantedList: MutableList<String>) -> Unit?)? = null,
    showRationale: ((permissionList: MutableList<String>) -> Unit?)? = null
): Boolean =
    suspendCoroutine {
        InlinePermissionResult(this)
            .onSuccess(object : SuccessCallback {
                override fun onSuccess(
                    allPermissionGranted: Boolean,
                    grantedPermissionList: MutableList<String>
                ) {
                    granted(allPermissionGranted, grantedPermissionList)
                }
            })
            .onFail(object : FailCallback {
                override fun onFailed(
                    allPermissionDenied: Boolean,
                    deniedPermissionList: MutableList<String>
                ) {
                    denied?.invoke(allPermissionDenied, deniedPermissionList)
                }
            })
            .onShowRationale(object : ShowRationaleCallback {
                override fun showRationale(permissionList: MutableList<String>) {
                    showRationale?.invoke(permissionList)
                }
            })
            .requestPermissions(permissions)
    }

suspend fun Fragment.requestPermissionsForResult(
    permissions: Array<String>,
    granted: (allGranted: Boolean, grantedList: MutableList<String>) -> Unit,
    denied: ((allGranted: Boolean, grantedList: MutableList<String>) -> Unit?)? = null,
    showRationale: ((permissionList: MutableList<String>) -> Unit?)? = null
): Boolean =
    suspendCoroutine {
        InlinePermissionResult(this)
            .onSuccess(object : SuccessCallback {
                override fun onSuccess(
                    allPermissionGranted: Boolean,
                    grantedPermissionList: MutableList<String>
                ) {
                    granted(allPermissionGranted, grantedPermissionList)
                }
            })
            .onFail(object : FailCallback {
                override fun onFailed(
                    allPermissionDenied: Boolean,
                    deniedPermissionList: MutableList<String>
                ) {
                    denied?.invoke(allPermissionDenied, deniedPermissionList)
                }
            })
            .onShowRationale(object : ShowRationaleCallback {
                override fun showRationale(permissionList: MutableList<String>) {
                    showRationale?.invoke(permissionList)
                }
            })
            .requestPermissions(permissions)
    }
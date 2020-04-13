package com.dj.coroutines.permisstions.callbacks

interface SuccessCallback {
    fun onSuccess(allPermissionGranted: Boolean, grantedPermissionList: MutableList<String>)
}
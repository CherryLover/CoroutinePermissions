package com.dj.coroutines.permisstions.callbacks

interface FailCallback {
    fun onFailed(allPermissionDenied: Boolean, deniedPermissionList: MutableList<String>)
}
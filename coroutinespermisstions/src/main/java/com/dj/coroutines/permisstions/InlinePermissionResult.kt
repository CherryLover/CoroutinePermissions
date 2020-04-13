package com.dj.coroutines.permisstions

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dj.coroutines.permisstions.callbacks.FailCallback
import com.dj.coroutines.permisstions.callbacks.SuccessCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.Reference
import java.lang.ref.WeakReference

class InlinePermissionResult {
    private val TAG = "ACTIVITY_RESULT_FRAGMENT_WEEEEE"
    private val tag = "InlinePermissionResult"
    private var activityReference: Reference<FragmentActivity>
    private val successCallbacks=ArrayList<SuccessCallback>()
    private val failCallbacks=ArrayList<FailCallback>()
    private var listener=object : RequestPermissionFragment.RequestPermissionsListener{
        val resultGrantedPermissionList = mutableListOf<String>()
        val resultDeniedPermissionList = mutableListOf<String>()
        override fun onRequestPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            for (i in grantResults.indices) {
                if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                    resultDeniedPermissionList.add(permissions[i])
                } else {
                    resultGrantedPermissionList.add(permissions[i])
                }
            }
            successCallbacks.forEach {
                it.onSuccess(
                    resultGrantedPermissionList.size == permissions.size,
                    resultGrantedPermissionList
                )
            }
            failCallbacks.forEach {
                it.onFailed(
                    resultDeniedPermissionList.size == permissions.size,
                    resultDeniedPermissionList
                )
            }
        }
    }
    constructor(activity: FragmentActivity){
        activityReference= WeakReference(activity)
    }
    constructor(fragment: Fragment){
        val activity: FragmentActivity = fragment.requireActivity()
        activityReference= WeakReference(activity)
    }

    fun onSuccess(callback: SuccessCallback):InlinePermissionResult{
        successCallbacks.add(callback)
        return this
    }
    fun onFail(callback: FailCallback):InlinePermissionResult{
        failCallbacks.add(callback)
        return this
    }

    fun requestPermissions(vararg permissions:String){
        val activity=activityReference.get()
        if (activity==null || activity.isFinishing)return

        val oldFragment:RequestPermissionFragment?= activity.supportFragmentManager.findFragmentByTag(TAG) as RequestPermissionFragment?
        if (oldFragment!=null){
            oldFragment.setListener(listener)
        }else{
            val newFragment=RequestPermissionFragment.newInstance(*permissions)
            newFragment.setListener(listener)
            CoroutineScope(Dispatchers.Main).launch {
                activity.supportFragmentManager
                    .beginTransaction()
                    .add(newFragment, TAG)
                    .commitNowAllowingStateLoss()
            }
        }

    }
}
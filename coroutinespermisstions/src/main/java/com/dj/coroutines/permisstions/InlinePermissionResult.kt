package com.dj.coroutines.permisstions

import android.content.pm.PackageManager
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dj.coroutines.permisstions.callbacks.FailCallback
import com.dj.coroutines.permisstions.callbacks.RequestResultListener
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
    private val responseListeners=ArrayList<RequestResultListener>()
    private var listener=object : RequestPermissionFragment.RequestPermissionsListener{
        val resultGrantedPermissionArray = mutableListOf<String>()
        val resultDeniedPermissionArray = mutableListOf<String>()
        override fun onRequestPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            //如果grantResults中，0代表该权限请求成功，-1代表失败
            var isRequestSuccess=true
            Log.d(tag, " permissions size is ${permissions.size} content is  ${permissions} grantResults size is ${grantResults.size} content is $grantResults")
            for (code in grantResults){
                if (PackageManager.PERMISSION_DENIED == code) {
                    resultDeniedPermissionArray.add(permissions[grantResults.indexOf(code)])
                } else {
                    resultGrantedPermissionArray.add(permissions[grantResults.indexOf(code)])
                }
            }
            if (isRequestSuccess){
                for (callback in successCallbacks) {
                    callback.onSuccess()
                }
                for (listener in responseListeners){
                    listener.onSuccess()
                }
            }else{
                for (callback in failCallbacks){
                    callback.onFailed()
                }
                for (listener in responseListeners){
                    listener.onFailed()
                }
            }

        }
    }
    constructor(activity: FragmentActivity){
        activityReference= WeakReference(activity)
    }
    constructor(fragment: Fragment){
        var activity:FragmentActivity?=null
        fragment?.let {
            activity=fragment.activity
        }
        activityReference= WeakReference(activity)
    }

    fun onSuccess(callback: SuccessCallback):InlinePermissionResult{
        callback?.let {
            successCallbacks.add(it)
        }
        return this
    }
    fun onFail(callback: FailCallback):InlinePermissionResult{
        callback?.let {
            failCallbacks.add(it)
        }
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
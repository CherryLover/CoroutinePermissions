package com.dj.coroutines.permisstions

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment

class RequestPermissionFragment : Fragment{
    private val permissions = mutableListOf<String>()
    private var listener:RequestPermissionsListener? = null
    companion object{
        private const val INTENT_TO_START = "INTENT_TO_START"
        private const val REQUEST_CODE = 24
        fun newInstance(vararg permissions:String):RequestPermissionFragment{
            val bundle= Bundle()
            bundle.putStringArray(INTENT_TO_START,permissions)
            val fragment=RequestPermissionFragment()
            fragment.arguments=bundle
            return fragment
        }
    }

    constructor(){
        retainInstance = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var tmpList = mutableListOf<String>()
        arguments?.let {
            val receiveList = it.getStringArray(INTENT_TO_START) as Array<String>
            tmpList = receiveList.toMutableList()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tmpList.forEach {
                if (requireActivity().checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) {
                    permissions.add(it)
                }
            }
        }
    }

    private val showRationalePermissionList = mutableListOf<String>()

    override fun onResume() {
        super.onResume()
        if (permissions.isNotEmpty()) {
            permissions.forEach {
                if (!shouldShowRequestPermissionRationale(it)) {
                    showRationalePermissionList.add(it)
                }
            }
            if (showRationalePermissionList.isNotEmpty()) {
                listener?.onShowRequestPermissionRationale(showRationalePermissionList)
            }
            requestPermissions(permissions.toTypedArray(), REQUEST_CODE)
        }else{
            removeFragment()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== REQUEST_CODE){
            listener?.let {
                it.onRequestPermissions(requestCode,permissions, grantResults)
            }
            removeFragment()
        }
    }
    fun  setListener(listener: RequestPermissionsListener):RequestPermissionFragment{
        this.listener=listener
        return this
    }
    private fun removeFragment(){
        fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
    }
    interface RequestPermissionsListener{
        fun onRequestPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

        fun onShowRequestPermissionRationale(showRationalePermissionList: MutableList<String>)
    }
}
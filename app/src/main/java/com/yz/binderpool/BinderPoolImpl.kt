package com.yz.binderpool

import android.os.IBinder

class BinderPoolImpl : IBinderPool.Stub() {

    companion object {
        const val SECURITY_CENTER_CODE = 0
        const val COMPUTE_CODE = 1
    }

    override fun queryBinder(binderCode: Int): IBinder? {
        return when (binderCode) {
            SECURITY_CENTER_CODE -> SecurityCenterImpl()
            COMPUTE_CODE -> ComputeImpl()
            else -> null
        }
    }

}
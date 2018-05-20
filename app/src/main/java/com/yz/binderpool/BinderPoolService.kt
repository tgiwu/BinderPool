package com.yz.binderpool

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BinderPoolService : Service() {
    val mBinderPool = BinderPoolImpl()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinderPool
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
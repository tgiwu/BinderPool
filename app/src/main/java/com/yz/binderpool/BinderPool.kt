package com.yz.binderpool

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import java.util.concurrent.CountDownLatch

class BinderPool(context: Context) {
    val mContext: Context
    companion object {
        @Volatile
        var instance : BinderPool? = null
        fun getInstance(context: Context) : BinderPool {
            if (null == instance){
                synchronized(BinderPool::class.java) {
                    if (null == instance) {
                        instance = BinderPool(context)

                    }
                }
            }
            return instance!!
        }
    }

    var mPool: IBinderPool? = null
    var mBinderPoolCountDownLatch: CountDownLatch? = null

    val mBinderPoolConnection = object: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPool = IBinderPool.Stub.asInterface(service)
            try {
                mPool!!.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0)
            } catch (e : RemoteException) {
                e.printStackTrace()
            }
            mBinderPoolCountDownLatch?.countDown()
        }

    }
    var mBinderPoolDeathRecipient = MyBinderPoolDeathRecipient()

    init {
        mContext = context.applicationContext
        connectBinderPoolService()
    }



    @Synchronized
    private fun connectBinderPoolService() {
        mBinderPoolCountDownLatch = CountDownLatch(1)
        val intent = Intent(mContext, BinderPoolService::class.java)
        mContext.bindService(intent, mBinderPoolConnection, Context.BIND_AUTO_CREATE)
        try {
            mBinderPoolCountDownLatch!!.await()
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    fun queryBinder(code : Int) :IBinder? {
        var binder : IBinder? = null
        try {
            mPool?.let {
                binder = mPool?.queryBinder(code)
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return binder
    }

    inner class MyBinderPoolDeathRecipient : IBinder.DeathRecipient{
        override fun binderDied() {
            mPool!!.asBinder().unlinkToDeath(mBinderPoolDeathRecipient,0)
            mPool = null
            connectBinderPoolService()
        }

    }
}
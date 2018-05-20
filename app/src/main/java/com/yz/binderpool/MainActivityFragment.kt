package com.yz.binderpool

import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yz.binderpool.BinderPoolImpl.Companion.COMPUTE_CODE
import com.yz.binderpool.BinderPoolImpl.Companion.SECURITY_CENTER_CODE

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        Thread(Runnable {
            doWork()
        }).start()
    }

    private fun doWork() {
        val pool = BinderPool.getInstance(this.activity!!)
        val binderSec = pool.queryBinder(SECURITY_CENTER_CODE)
        val security = ISecurityCenter.Stub.asInterface(binderSec) as ISecurityCenter

        Log.i("MainActivity", "encrypt ${security.encrypt("asd")}")
        Log.i("MainActivity", "decrypt ${security.decrypt("asd")}")

        val computeb = pool.queryBinder(COMPUTE_CODE)
        val compute = ICompute.Stub.asInterface(computeb)

        Log.i("MainActivity", "compute  ${compute.add(1, 2)}")
    }
}

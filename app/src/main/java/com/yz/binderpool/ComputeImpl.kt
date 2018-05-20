package com.yz.binderpool

class ComputeImpl :ICompute.Stub() {
    override fun add(a: Int, b: Int): Int {
        return a + b
    }
}
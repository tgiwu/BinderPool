package com.yz.binderpool

import android.util.Log


class SecurityCenterImpl: ISecurityCenter.Stub() {

    val SECRET_CODE = '^'
    override fun encrypt(content: String?): String {
        var chars = content?.toCharArray()
        for (index in chars!!.indices) {
            chars[index] = (chars[index].toInt() xor SECRET_CODE.toInt()).toChar()
            Log.d("SecurityCenterImpl", "char ${chars[index].toString()}")
        }
        return chars.toString()
    }

    override fun decrypt(password: String?): String {
        return encrypt(password)
    }
}
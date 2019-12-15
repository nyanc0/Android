package com.nyanc0_android.mhwarmorcollection.permission

import android.Manifest
import android.text.TextUtils

enum class Permission(val permission: String) {

    // CAMERA
    P_CAMERA(Manifest.permission.CAMERA),
    P_WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    P_READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE);

    companion object {
        fun get(permission: String): Permission? {
            values().forEach {
                if (TextUtils.equals(it.permission, permission)) return it
            }
            return null
        }
    }
}
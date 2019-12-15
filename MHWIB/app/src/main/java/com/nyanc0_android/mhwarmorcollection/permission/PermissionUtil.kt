package com.nyanc0_android.mhwarmorcollection.permission

import android.app.Activity
import android.os.Build
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * すでにパーミッションが得られているかチェック(単体)
 */
fun isAuthrized(permission: Permission, activity: Activity): Boolean {
    return when (PermissionChecker.checkSelfPermission(activity.applicationContext, permission.permission)) {
        PermissionChecker.PERMISSION_GRANTED -> true
        else -> false
    }
}

fun isAuthrized(permission: Permission, fragment: Fragment): Boolean {
    return when (PermissionChecker.checkSelfPermission(fragment.context!!, permission.permission)) {
        PermissionChecker.PERMISSION_GRANTED -> true
        else -> false
    }
}

/**
 * すでにパーミッションが得られているかチェック(複数)
 */
fun isAuthrized(permissions: Array<Permission>, activity: Activity): Boolean {
    var granted = true
    permissions.forEach { granted = granted.and(isAuthrized(it, activity)) }
    return granted
}

/**
 * パーミッションの取得
 */
fun requestPermission(permission: Permission, requestCd: Int, activity: FragmentActivity) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        activity.requestPermissions(arrayOf(permission.permission), requestCd)
    }
}

fun requestPermission(permission: Permission, requestCd: Int, fragment: Fragment) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        fragment.requestPermissions(arrayOf(permission.permission), requestCd)
    }
}

/** 権限要求を「これ以上表示しない」設定にされているか確認 */
fun showRationale(permission: Permission, activity: FragmentActivity): Boolean {
    if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) return true
    return activity.shouldShowRequestPermissionRationale(permission.permission)
}

fun showRationale(permission: Permission, fragment: Fragment): Boolean {
    if (Build.VERSION_CODES.M > Build.VERSION.SDK_INT) return true
    return fragment.shouldShowRequestPermissionRationale(permission.permission)
}

/** 権限要求結果の確認 */
fun verifyGrantResults(results: IntArray): Boolean {
    if (results.isEmpty()) return false
    results.forEach { if (PermissionChecker.PERMISSION_GRANTED != it) return false }
    return true
}
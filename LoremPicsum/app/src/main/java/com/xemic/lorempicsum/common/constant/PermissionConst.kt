package com.xemic.lorempicsum.common.constant

import android.Manifest

object PermissionConst {
    const val REQUEST_EXTERNAL_STORAGE = 1
    val PERMISSION_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}
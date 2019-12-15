package com.nyanc0_android.mhwarmorcollection.common

import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.nyanc0_android.mhwarmorcollection.App
import com.nyanc0_android.mhwarmorcollection.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun deleteExistFile(file: File) {
    if (isFileExist(file)) {
        file.delete()
    }
}

fun isFileExist(file: File): Boolean {
    return file.exists()
}

fun createUri(file: File): Uri {
    return FileProvider.getUriForFile(
        App.applicationContext(),
        BuildConfig.APPLICATION_ID + ".fileprovider",
        file
    )
}

fun createFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
    val fileName = "IMG_$timeStamp.jpg"
    val filePath = getExternalStorageDirPath("/nyanc0") + "/" + fileName
    return File(filePath)
}

private fun getExternalStorageDirPath(makeDir: String?): String? {
    var makeDir = makeDir
    var dirPath = ""
    var photoDir: File? = null
    val extStorageDir = Environment.getExternalStorageDirectory()
    if (extStorageDir.canWrite()) {
        if (makeDir == null) {
            makeDir = ""
        }
        photoDir = File(extStorageDir.path + makeDir)
    }

    if (photoDir != null) {
        if (!photoDir.exists() && !photoDir.mkdirs()) {
            return null
        }
        if (photoDir.canWrite()) {
            dirPath = photoDir.path
        }
    }
    return dirPath
}
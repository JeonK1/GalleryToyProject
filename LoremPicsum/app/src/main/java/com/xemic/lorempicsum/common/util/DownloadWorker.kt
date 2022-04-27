package com.xemic.lorempicsum.common.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.xemic.lorempicsum.common.constant.DownloadWorkerConst
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class DownloadWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    companion object {
        const val TAG = "DownloadWorker"
    }

    override fun doWork(): Result {
        val url = inputData.getString(DownloadWorkerConst.IMAGE_URL) ?: ""
        var result = Result.failure()
        if(url != ""){
            result = downloadImage(url)
        }

        return if(result == Result.success()){
            Result.success(
                Data.Builder()
                    .putInt(DownloadWorkerConst.WORK_RESULT, DownloadWorkerConst.RESULT_OK)
                    .build()
            )
        } else {
            Result.failure(
                Data.Builder()
                    .putInt(DownloadWorkerConst.WORK_RESULT, DownloadWorkerConst.RESULT_FAILED)
                    .build()
            )
        }
    }

    private fun downloadImage(url: String):Result {
        try {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)

            val filePath =
                Environment.getExternalStorageDirectory().path + "/Download/" + Calendar.getInstance().time.time.toString() + ".jpg"
            val file = File(filePath)

            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            return Result.success()
        } catch (e: IOException) {
            Log.e(TAG, "${e.printStackTrace()}")
            return Result.failure()
        }
    }
}
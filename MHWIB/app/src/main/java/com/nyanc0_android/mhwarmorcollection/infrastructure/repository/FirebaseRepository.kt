package com.nyanc0_android.mhwarmorcollection.infrastructure.repository

import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.nyanc0_android.mhwarmorcollection.common.BoxGraphic
import com.nyanc0_android.mhwarmorcollection.common.Graphic
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object FirebaseRepository {
    /**
     * 解析を実施.
     *
     * @param bitmap 解析する画像
     * @param detector 解析する内容
     * @return Result<MutableList<Graphic>>
     */
    @WorkerThread
    suspend fun startFirebase(bitmap: Bitmap): MutableList<Graphic> {

        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val firebaseVision = FirebaseVision.getInstance()

        return suspendCoroutine { cont ->
            val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setModelType(FirebaseVisionCloudTextRecognizerOptions.DENSE_MODEL)
                .setLanguageHints(listOf("jp"))
                .build()

            firebaseVision.getCloudTextRecognizer(options)
                .processImage(image)
                .addOnSuccessListener { texts ->
                    val result = mutableListOf<Graphic>()
                    for (block in texts.textBlocks) {
                        for (line in block.lines) {
                            for (element in line.elements) {
                                element.boundingBox?.let {
                                    result.add(BoxGraphic(element.text, it))
                                }
                            }
                        }
                    }
                    cont.resume(result)
                }.addOnFailureListener { exception ->
                    cont.resumeWithException(exception)
                }
        }
    }
}
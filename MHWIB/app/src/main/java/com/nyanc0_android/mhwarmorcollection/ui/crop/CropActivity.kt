package com.nyanc0_android.mhwarmorcollection.ui.crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.common.createFile
import com.nyanc0_android.mhwarmorcollection.common.deleteExistFile
import kotlinx.android.synthetic.main.activity_crop.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext

class CropActivity : AppCompatActivity(), CropCallback, LoadCallback, View.OnClickListener,
    CoroutineScope {

    /** トリミング前の画像URI */
    private lateinit var tmpUri: Uri
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

        setContentView(R.layout.activity_crop)

        // トリミングする写真を取得
        tmpUri = intent.getParcelableExtra(KEY_INTENT)
        crop_image.load(tmpUri).execute(this)
        rotation_btn.setOnClickListener(this)
        crop_btn.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onSuccess() {
        // do nothing
    }

    override fun onSuccess(cropped: Bitmap?) {
        if (cropped == null) return
        returnCroppedPhoto(cropped)
    }

    override fun onError(e: Throwable?) {
        Toast.makeText(this, resources.getString(R.string.cropped_error), Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.crop_btn -> {
                cropImage()
            }
            R.id.rotation_btn -> {
                rotateImage()
            }
        }
    }

    /**
     * 画像トリミング
     */
    private fun cropImage() {
        crop_image.crop(tmpUri).execute(this)
    }

    /**
     * 画像を回転
     */
    private fun rotateImage() {
        crop_image.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
    }

    /**
     * 画像をトリミングして終了する.
     *
     * @param cropped Bitmap
     */
    private fun returnCroppedPhoto(cropped: Bitmap?) = launch(Dispatchers.Main) {
        val savedUri = saveImage(cropped).await()
        Intent().let {
            it.putExtra(KEY_RESULT_INTENT, savedUri)
            setResult(Activity.RESULT_OK, it)
        }
        finish()
    }

    /**
     * トリミング画像を保存する.
     *
     * @param cropped Bitmap
     */
    private fun saveImage(cropped: Bitmap?) = async(Dispatchers.Default) {

        val byteArrayOutputSystem = ByteArrayOutputStream()
        cropped?.let {
            it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputSystem)
            byteArrayOutputSystem.close()
        }

        val saveFile = createFile()
        val fileOutputStream = FileOutputStream(saveFile)
        fileOutputStream.write(byteArrayOutputSystem.toByteArray())
        fileOutputStream.close()

        // トリミング前の写真を削除
        deleteExistFile(File(tmpUri.path))

        return@async Uri.fromFile(saveFile)
    }

    companion object {
        const val REQUEST_CD = 2000
        const val KEY_INTENT = "key_crop_image"
        const val KEY_RESULT_INTENT = "key_cropped_image"

        fun startForResult(activity: Activity, uri: Uri) {
            val intent = Intent(activity, CropActivity::class.java)
            intent.putExtra(KEY_INTENT, uri)
            activity.startActivityForResult(intent, REQUEST_CD)
        }

        fun intent(context: Context, uri: Uri) = Intent(context, this::class.java).also {
            it.putExtra(KEY_INTENT, uri)
        }
    }
}
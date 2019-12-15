package com.nyanc0_android.mhwarmorcollection.ui.detect

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.nyanc0_android.mhwarmorcollection.App
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.FirebaseRepository
import com.nyanc0_android.mhwarmorcollection.ui.record.RecordActivity
import kotlinx.android.synthetic.main.activity_detect.*
import kotlinx.coroutines.*
import java.lang.Float.max
import kotlin.coroutines.CoroutineContext

class DetectActivity : AppCompatActivity(), CoroutineScope {

    lateinit var bitmap: Bitmap

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detect)

        main_image.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                intent.extras?.getParcelable<Uri>(KEY)?.let {
                    setBitmap(it, main_image.width, main_image.height)
                }
                main_image.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        button_start_detect.setOnClickListener {
            detect()
        }

        button_start_register.setOnClickListener {
            val result = overlay.getText()
            startActivity(RecordActivity.intent(this, ArrayList(result)))
        }
    }

    /**
     * Bitmapをリサイズしてセットする.<br>
     * Viewはbitmapをobserveして結果を受け取る.
     * @param uri 表示する画像のURI
     * @param width 表示先ImageViewの横幅
     * @param height 表示先ImageViewの縦幅
     */
    private fun setBitmap(uri: Uri, width: Int, height: Int) {
        launch {
            val result: Bitmap = resizeBitmap(uri, width, height)
            bitmap = result
            overlay.targetWidth = bitmap.width
            overlay.targetHeight = bitmap.height
            main_image.setImageBitmap(result)
        }
    }

    /**
     * Bitmapをアスペクト比を維持したままリサイズする.
     *
     * @param width 表示先ImageViewの横幅
     * @param height 表示先ImageViewの縦幅
     * @return Bitmap
     */
    private suspend fun resizeBitmap(uri: Uri, width: Int, height: Int): Bitmap =
        withContext(Dispatchers.Default) {

            val imageBitmap: Bitmap = Glide.with(App.applicationContext())
                .asBitmap()
                .load(uri)
                .submit(width, height)
                .get()

            val scaleFactor = max(
                imageBitmap.width.toFloat() / width.toFloat(),
                imageBitmap.height.toFloat() / height.toFloat()
            )

            val targetWidth = (imageBitmap.width / scaleFactor).toInt()
            val targetHeight = (imageBitmap.height / scaleFactor).toInt()

            Bitmap.createScaledBitmap(
                imageBitmap,
                targetWidth,
                targetHeight,
                true
            )
        }

    /**
     * 解析開始.
     */
    private fun detect() {
        launch {
            val result = withContext(Dispatchers.Default) {
                FirebaseRepository.startFirebase(bitmap)
            }

            for (graphic in result) {
                overlay.add(graphic)
            }
        }
    }

    companion object {
        fun intent(uri: Uri, activity: FragmentActivity) =
            Intent(activity, DetectActivity::class.java).also {
                it.putExtra(KEY, uri)
            }

        private const val KEY = "uri"
    }
}
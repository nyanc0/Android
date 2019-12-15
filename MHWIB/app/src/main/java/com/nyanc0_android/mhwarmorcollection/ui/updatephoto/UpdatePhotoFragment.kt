package com.nyanc0_android.mhwarmorcollection.ui.updatephoto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nyanc0_android.mhwarmorcollection.R
import com.nyanc0_android.mhwarmorcollection.common.createFile
import com.nyanc0_android.mhwarmorcollection.common.createUri
import com.nyanc0_android.mhwarmorcollection.infrastructure.repository.ArmorNameRepository
import com.nyanc0_android.mhwarmorcollection.permission.*
import com.nyanc0_android.mhwarmorcollection.ui.crop.CropActivity
import kotlinx.android.synthetic.main.fragment_update_photo.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UpdatePhotoFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /** カメラで撮影したURI */
    private lateinit var cameraUri: Uri

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_update_camera.setOnClickListener {
            startCamera()
        }

        btn_update_storage.setOnClickListener {
            startLibrary()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (verifyGrantResults(grantResults)) {
                    startCamera()
                }
            }
            PERMISSION_CHOOSE_IMAGE -> {
                if (verifyGrantResults(grantResults)) {
                    startLibrary()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    CropActivity.startForResult(activity!!, cameraUri)
                }
            }
            REQUEST_CHOOSE_IMAGE -> {
                data?.data?.let {
                    CropActivity.startForResult(activity!!, it)
                }
            }
        }
    }

    private fun fetch() = GlobalScope.launch(Dispatchers.Main) {
        val repository = ArmorNameRepository()
        val result = repository.fetch(listOf("EXアンガルダヘルムα", "EXアンガルダメイルα"))
    }

    /**
     * 権限チェック
     */
    private fun checkCameraPermission(requestCode: Int): Boolean {
        if (!isAuthrized(Permission.P_WRITE_EXTERNAL_STORAGE, this)) {
            if (!showRationale(Permission.P_WRITE_EXTERNAL_STORAGE, this)) {
                requestPermission(Permission.P_WRITE_EXTERNAL_STORAGE, requestCode, this)
            } else {
                Toast.makeText(context, "許可してください", Toast.LENGTH_SHORT).show()
            }
            return false
        }
        return true
    }

    /**
     * カメラ起動.
     */
    private fun startCamera() {
        if (!checkCameraPermission(PERMISSION_CAMERA)) {
            return
        }
        cameraUri = createUri(createFile())
        val intent = Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            addCategory(Intent.CATEGORY_DEFAULT)
            this.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        }
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    /**
     * ライブラリ起動.
     */
    private fun startLibrary() {

        if (!checkCameraPermission(PERMISSION_CHOOSE_IMAGE)) {
            return
        }

        val intent = Intent().apply {
            type = IMAGE_TYPE
            action = Intent.ACTION_OPEN_DOCUMENT
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, CHOOSER_TITLE), REQUEST_CHOOSE_IMAGE)
    }

    companion object {
        const val PERMISSION_CAMERA = 1000
        const val PERMISSION_CHOOSE_IMAGE = 1001
        const val REQUEST_CAMERA = 1002
        const val REQUEST_CHOOSE_IMAGE = 1003
        private const val IMAGE_TYPE = "image/*"
        private const val CHOOSER_TITLE = "Select Picture"
    }
}
package com.nyanc0_android.mhwarmorcollection

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nyanc0_android.mhwarmorcollection.ui.crop.CropActivity
import com.nyanc0_android.mhwarmorcollection.ui.detect.DetectActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropActivity.REQUEST_CD -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getParcelableExtra<Uri>(CropActivity.KEY_RESULT_INTENT)?.let {
                        startActivity(DetectActivity.intent(it, this))
                    }
                }
            }
        }
    }
}

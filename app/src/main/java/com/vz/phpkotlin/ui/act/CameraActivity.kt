package com.vz.phpkotlin.ui.act

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.evergrande.lib.cameraLib.listener.VzCameraLisenter
import com.vz.phpkotlin.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File



class CameraActivity : AppCompatActivity(), VzCameraLisenter {

    private val GET_PERMISSION_REQUEST = 100 //权限申请自定义码

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        vzCameraView.forbiddenAudio(false)
        vzCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "VzCamera")
        vzCameraView.setVzCameraLisenter(this@CameraActivity)
        /**
         * 6.0动态权限获取
         */
        getPermissions();
    }

    override fun onStart() {
        super.onStart()
        /**
         * 全屏显示
         */
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@CameraActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@CameraActivity,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED && ContextCompat
                    .checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(this@CameraActivity, arrayOf<String>(Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA),
                        GET_PERMISSION_REQUEST)
            }

        } else {
            //具有权限
        }
    }

    /**
     * 获取内存权限回调
     */
    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GET_PERMISSION_REQUEST) {
            if (grantResults.size >= 1) {
                //获取读写内存的权限
                val writeResult = grantResults[0]//读写内存权限
                val writeGranted = writeResult == PackageManager.PERMISSION_GRANTED//读写内存权限
                if (writeGranted) {
                    //具备权限
                } else {

                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    }
                }
                //录音权限
                val recordPermissionResult = grantResults[1]
                val recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED
                if (recordPermissionGranted) {
                    //具备权限
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    }
                }
                //相机权限
                val cameraPermissionResult = grantResults[2]
                val cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED
                if (cameraPermissionGranted) {
                    //具备权限
                } else {
                    //不具有相关权限
                    Toast.makeText(this, "拍照被禁止，部分功能将失效，请到设置中开启。", Toast.LENGTH_SHORT).show()

                    if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                        //如果用户勾选了不再提醒，则返回false
                        Toast.makeText(this, "请到设置-权限管理中开启", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vzCameraView.onResume()
    }

    override fun onPause() {
        super.onPause()
        vzCameraView.onPause()
    }

    override fun captureSuccess(bitmap: Bitmap?) {

    }

    override fun recordSuccess(url: String?) {

    }

    override fun quit() {
        finish()
    }
}

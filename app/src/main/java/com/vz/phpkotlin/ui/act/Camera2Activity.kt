package com.vz.phpkotlin.ui.act

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.evergrande.lib.cameraLib.cameraview.CameraView
import com.evergrande.lib.cameraLib.permission.ConfirmationDialogFragment
import com.evergrande.lib.utils.android.content.res.DensityUtils
import com.evergrande.lib.utils.java.io.FileUtils
import com.vz.phpkotlin.R
import com.vz.phpkotlin.utils.ToolUI
import kotlinx.android.synthetic.main.activity_camera2.*
import java.io.File
import java.util.*


class Camera2Activity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val REQUEST_CAMERA_PERMISSION = 1

    private val FRAGMENT_DIALOG = "dialog"

    private val FLASH_OPTIONS = intArrayOf(CameraView.FLASH_AUTO, CameraView.FLASH_OFF, CameraView.FLASH_ON)

    private val FLASH_ICONS = intArrayOf(R.drawable.ic_flash_auto, R.drawable.ic_flash_off, R.drawable.ic_flash_on)

    private val FLASH_TITLES = intArrayOf(R.string.flash_auto, R.string.flash_off, R.string.flash_on)

    private var mBackgroundHandler: Handler? = null

    private val mList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)
        take_picture.setOnClickListener {
            if (v_camera != null) {
                v_camera.takePicture()
            }
        }
        v_camera.addCallback(mCallback)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            v_camera.start()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(supportFragmentManager, FRAGMENT_DIALOG)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onPause() {
        v_camera.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler!!.getLooper().quitSafely()
            } else {
                mBackgroundHandler!!.getLooper().quit()
            }
            mBackgroundHandler = null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (permissions.size != 1 || grantResults.size != 1) {
                    throw RuntimeException("Error on requesting camera permission.")
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show()
                }
            }
        }// No need to start camera here; it is handled by onResume
    }

    private fun getBackgroundHandler(): Handler {
        if (mBackgroundHandler == null) {
            val thread = HandlerThread("background")
            thread.start()
            mBackgroundHandler = Handler(thread.looper)
        }
        return mBackgroundHandler!!
    }

    private val mCallback = object : CameraView.Callback() {

        override fun onCameraOpened(cameraView: CameraView) {
            Log.d(TAG, "onCameraOpened")
        }

        override fun onCameraClosed(cameraView: CameraView) {
            Log.d(TAG, "onCameraClosed")
        }

        override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
            Log.d(TAG, "onPictureTaken " + data.size)
            Toast.makeText(cameraView.context, R.string.picture_taken, Toast.LENGTH_SHORT).show()
            getBackgroundHandler().post {


                try {
                    var name = System.currentTimeMillis()
                    val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath + "/hdtest", name.toString() + ".jpg")
                    if (!FileUtils.isExists(file))
                        FileUtils.createFile(file)
                    FileUtils.writeByte(file, data, false)// 写到内存
                    mList.add(file.absolutePath.toString())
                    refreshPhotoView(mList)
                    Log.w(TAG, file.absolutePath.toString())
                } catch (e: Exception) {

                }

//                var os: OutputStream? = null
//                try {
//                    os = FileOutputStream(file)
//                    os!!.write(data)
//                    os!!.close()
//                    mList.add(file.absolutePath.toString())
//                    refreshPhotoView(mList)
//                    Log.w(TAG, file.absolutePath.toString())
//                } catch (e: IOException) {
//                    Log.w(TAG, "Cannot write to " + file, e)
//                } finally {
//                    if (os != null) {
//                        try {
//                            os!!.close()
//                        } catch (e: IOException) {
//                            // Ignore
//                        }
//
//                    }
//                }
            }
        }
    }

    private var position = 0

    private fun refreshPhotoView(list: List<String>) {
        ToolUI.postTaskDelay(Runnable {
            ll_container.removeAllViews()
            position = 0
            var layoutParams = ViewGroup.LayoutParams(DensityUtils.dp2px(this@Camera2Activity, 50f), DensityUtils.dp2px(this@Camera2Activity, 50f))
            var rlLayoutParams = ViewGroup.LayoutParams(-2, -2)
            var img: Bitmap? = null
            for (p in list) {
                var rl = RelativeLayout(this)
                rl.layoutParams = rlLayoutParams
                var iv = ImageView(this)
                img = BitmapFactory.decodeFile(p!!)
                iv.setImageBitmap(img)
                iv.layoutParams = layoutParams
                rl.addView(iv)
                ll_container.addView(rl)
                position++
            }
        }, 0)
    }
}

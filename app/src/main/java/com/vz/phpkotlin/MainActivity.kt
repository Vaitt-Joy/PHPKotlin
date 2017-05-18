package com.vz.phpkotlin

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.vz.phpkotlin.ui.act.Camera2Activity
import com.vz.phpkotlin.ui.act.CameraActivity
import com.vz.phpkotlin.ui.act.TestActivity
import com.vz.phpkotlin.ui.base.VzBaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : VzBaseActivity(), View.OnClickListener {
    override fun initView() {
        setContentView(R.layout.activity_main)
        btn_php_check_version.setOnClickListener(this@MainActivity)
        tv_hello.setOnClickListener(this@MainActivity)//MyConstants
        btn_test.setOnClickListener (this@MainActivity)
        btn_camera.setOnClickListener(this@MainActivity)
        btn_camera2.setOnClickListener(this@MainActivity)
    }

    override fun resume() {
    }

    override fun destroy() {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_php_check_version -> {
                Toast.makeText(this@MainActivity, "获取版本号！", Toast.LENGTH_SHORT).show()
            }
            R.id.tv_hello -> {
                Toast.makeText(this@MainActivity, "hello,php！", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_test ->{
                startActivity(Intent(this@MainActivity, TestActivity::class.java))
            }
            R.id.btn_camera ->{
                startActivity(Intent(this@MainActivity, CameraActivity ::class.java))
            }R.id.btn_camera2 ->{
                startActivity(Intent(this@MainActivity, Camera2Activity ::class.java))
            }
            else -> {

            }

        }
    }

}


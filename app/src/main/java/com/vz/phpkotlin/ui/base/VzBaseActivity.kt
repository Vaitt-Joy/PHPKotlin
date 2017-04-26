package com.vz.phpkotlin.ui.base

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vz.phpkotlin.utils.ActivityManager
import java.lang.ref.WeakReference

/**
 * Created by huangwz on 2017/4/25.
 */

abstract class VzBaseActivity : AppCompatActivity(), IBaseActivity {

    protected var context: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = WeakReference<Activity>(this)
        ActivityManager.pushTask(context!!)
        initView()
        initData()
        initEvent()
    }

    protected fun initData() {}

    protected fun initEvent() {

    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }
}

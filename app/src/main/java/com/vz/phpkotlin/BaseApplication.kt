package com.vz.phpkotlin

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.tencent.bugly.Bugly
import com.vz.phpkotlin.constants.MyConstants

/**
 * Created by huangwz on 2017/4/25.
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        mainThread = Thread.currentThread()
        mainTreadId = android.os.Process.myTid().toLong()
        mMainLooper = mainLooper
        handler = Handler()
        initData()
    }

    /**初始化数据*/
    private fun initData() {
        Bugly.init(context, MyConstants.BUGLY_APP_ID,false);
    }

    companion object {

        /**
         * 对外提供整个应用生命周期的Context
         */
        /**
         * 对外提供Application Context

         * @return
         */
        var context: Context? = null
            private set
        var mainThread: Thread? = null
            private set
        var mMainLooper: Looper? = null
            private set
        var handler: Handler? = null
            private set
        var mainTreadId: Long = 0
            private set
    }
}

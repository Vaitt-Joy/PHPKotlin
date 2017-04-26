package com.vz.phpkotlin.utils

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.view.WindowManager
import com.vz.phpkotlin.BaseApplication

/**
 * @author: Vaitt_joy <br></br> Email:vaitt_joy@163.com
 * *
 * @Des:UI 工具集
 */
object ToolUI {
    /**
     * 得到上下文
     */
    val context: Context?
        get() = BaseApplication.context

    /**
     * 得到Resouce对象
     */
    val resource: Resources
        get() = context!!.resources

    /**
     * 得到String.xml中的字符串
     */
    fun getString(resId: Int): String {
        return resource.getString(resId)
    }

    /**
     * 得到String.xml中的字符串数组
     */
    fun getStringArr(resId: Int): Array<String> {
        return resource.getStringArray(resId)
    }

    /**
     * 得到colors.xml中的颜色
     */
    fun getColor(colorId: Int): Int {
        return resource.getColor(colorId)
    }

    /**
     * 得到应用程序的包名
     */
    val packageName: String
        get() = context!!.packageName

    /**
     * 得到主线程id
     */
    val mainThreadid: Long
        get() = BaseApplication.mainTreadId

    /**
     * 得到主线程Handler
     */
    val mainThreadHandler: Handler?
        get() = BaseApplication.handler

    /**
     * 把任务跑在UI线程中
     */
    fun runInMainUI(task: Runnable) {
        val curThreadId = android.os.Process.myTid()

        if (curThreadId.toLong() == mainThreadid) {// 如果当前线程是主线程
            task.run()
        } else {// 如果当前线程不是主线程
            mainThreadHandler!!.post(task)
        }
    }

    /**
     * 延迟执行任务
     */
    fun postTaskDelay(task: Runnable, delayMillis: Int) {
        mainThreadHandler!!.postDelayed(task, delayMillis.toLong())
    }

    /**
     * 移除任务
     */
    fun removeTask(task: Runnable) {
        mainThreadHandler!!.removeCallbacks(task)
    }

    /**
     * dip-->px
     */
    fun dip2Px(dip: Float): Int {
        // px/dip = density;
        val density = resource.displayMetrics.density
        val px = (dip * density + .5f).toInt()
        return px
    }

    /**
     * px-->dip
     */
    fun px2Dip(px: Float): Int {
        // px/dip = density;
        val density = resource.displayMetrics.density
        val dip = (px / density + .5f).toInt()
        return dip
    }

    /**
     * 将px值转换为sp值，保证文字大小不变

     * @param pxValue
     * *
     * @return
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = resource.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变

     * @param spValue
     * *
     * @return
     */
    fun sp2px(spValue: Float): Int {
        val fontScale = resource.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /*
     * 屏幕宽度
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 屏幕高度
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getDisplayHeight(context: Context):Int{
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.defaultDisplay.height
    }

    /**
     * 获取状态栏的高度

     * @param context
     * *
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val obj = clazz.newInstance()
            val field = clazz.getField("status_bar_height")
            val temp = Integer.parseInt(field.get(obj).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(temp)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight
    }
}

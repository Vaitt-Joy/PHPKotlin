package com.vz.phpkotlin.utils

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by huangwz on 2017/4/25.
 * app activity 管理
 */

class ActivityManager {

    /**
     * 根据指定位置从栈中移除Activity

     * @param taskIndex Activity栈索引
     */
    fun removeTask(taskIndex: Int) {
        if (activitys.size > taskIndex)
            activitys.removeAt(taskIndex)
    }

    /**
     * 将栈中Activity移除至栈顶
     */
    fun removeToTop() {
        val end = activitys.size
        val start = 1
        for (i in end - 1 downTo start) {
            if (!activitys[i].get()!!.isFinishing()) {
                activitys[i].get()!!.finish()
            }
        }
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    fun removeAll() {
        //finish所有的Activity
        for (task in activitys) {
            if (!task.get()!!.isFinishing()) {
                task.get()!!.finish()
            }
        }
    }

    companion object {

        val activitys = Stack<WeakReference<Activity>>()

        /**
         * 将Activity压入Application栈

         * @param task 将要压入栈的Activity对象
         */
        fun pushTask(task: WeakReference<Activity>) {
            activitys.push(task)
        }

        /**
         * 将传入的Activity对象从栈中移除

         * @param task
         */
        fun removeTask(task: WeakReference<Activity>) {
            activitys.remove(task)
        }
    }
}

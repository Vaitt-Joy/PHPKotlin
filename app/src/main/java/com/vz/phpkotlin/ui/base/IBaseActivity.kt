package com.vz.phpkotlin.ui.base

/**
 * Created by huangwz on 2017/4/25.
 */

interface IBaseActivity {

    /**
     * 初始化控件
     */
    fun initView()

    /**
     * 暂停恢复刷新相关操作（onResume方法中调用）
     */
    fun resume()

    /**
     * 销毁、释放资源相关操作（onDestroy方法中调用）
     */
    fun destroy()
}

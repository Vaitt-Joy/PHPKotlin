package com.eleven.baselibrary.activity;

import android.os.Bundle;

/**
 * Created by huangwz on 2017/6/3.
 *
 * @Link: vaitt_joy@163.com
 */
interface IBaseActivity {
    /**
     * 设置view 和初始view控件
     *
     * @param savedInstanceState
     */
    void initView(Bundle savedInstanceState);

    /**
     * 暂停恢复刷新相关操作（onResume方法中调用）
     */
    public void resume();

    /**
     * 销毁、释放资源相关操作（onDestroy方法中调用）
     */
    public void destroy();
}

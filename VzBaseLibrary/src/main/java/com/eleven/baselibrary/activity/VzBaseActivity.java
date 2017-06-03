package com.eleven.baselibrary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.eleven.baselibrary.utils.ActivityManager;

import java.lang.ref.WeakReference;

/**
 * Created by huangwz on 2017/6/3.
 *
 * @Link: vaitt_joy@163.com
 *
 */

public abstract class VzBaseActivity extends AppCompatActivity implements IBaseActivity{

    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    protected WeakReference<Activity> mContext = null;
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = new WeakReference<Activity>(this);
        ActivityManager.pushTask(mContext);
        initView(savedInstanceState);
        initData();
        initEvent();
    }

    /**
     * 初始数据
     */
    protected void initData(){}

    /**
     * 初始化控件的事件
     */
    protected void initEvent(){}

    @Override
    protected void onResume() {
        super.onResume();
        resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
        ActivityManager.removeTask(mContext);
    }
}

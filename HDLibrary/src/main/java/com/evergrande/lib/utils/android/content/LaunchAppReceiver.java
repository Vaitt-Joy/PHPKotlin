package com.evergrande.lib.utils.android.content;

/**
 * Created by huangwz on 2017/5/9.
 *
 * @Link: vaitt_joy@163.com
 *
 * 启动程序广播接收器
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class LaunchAppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
    }
}

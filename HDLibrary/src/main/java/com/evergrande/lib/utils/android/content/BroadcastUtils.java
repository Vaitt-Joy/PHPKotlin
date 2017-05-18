package com.evergrande.lib.utils.android.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.evergrande.lib.utils.java.lang.StringUtils;

/**
 * Created by huangwz on 2017/5/9.
 *
 * @Link: vaitt_joy@163.com
 */

public class BroadcastUtils {
    /**
     * 发送广播
     * @param context 上下文
     * @param filterAction 广播过滤器
     * @param bundle 数据
     * @return true：发送成功；false：发送失败，原因是context或者filterAction参数不正确
     */
    public static final boolean sendBroadcast(Context context, String filterAction, Bundle bundle){
        boolean result = false;
        if(context != null && StringUtils.isNotEmpty(filterAction)){
            Intent intent = new Intent(filterAction);
            if(bundle != null){
                intent.putExtras(bundle);
            }
            context.sendBroadcast(intent);
            result = true;
        }
        return result;
    }

    /**
     * 发送广播
     * @param context 上下文
     * @param filterAction 广播过滤器
     * @return true：发送成功；false：发送失败，原因是context或者filterAction参数不正确
     */
    public static final boolean sendBroadcast(Context context, String filterAction){
        return sendBroadcast(context, filterAction, null);
    }
}

package com.evergrande.lib.utils.android.os;

import android.os.Build;

import java.io.File;

/**
 * Created by huangwz on 2017/5/9.
 *
 * @Link: vaitt_joy@163.com
 * <p>
 * Android 系统工具类
 */

public class OSUtils {

    /**
     * 判断当前系统是否是Android4.0
     *
     * @return 0：是；小于0：小于4.0；大于0：大于4.0
     */
    public static int isAPI14() {
        return Build.VERSION.SDK_INT - 14;
    }

    /**
     * 根据/system/bin/或/system/xbin目录下是否存在su文件判断是否已ROOT
     *
     * @return true：已ROOT
     */
    public static boolean isRoot() {
        try {
            return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            return false;
        }
    }
}

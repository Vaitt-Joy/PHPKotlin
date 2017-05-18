package com.evergrande.lib.cameraLib.listener;

import android.graphics.Bitmap;

/**
 * Created by huangwz on 2017/5/8.
 */

public interface VzCameraLisenter {

    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url);

    void quit();

}
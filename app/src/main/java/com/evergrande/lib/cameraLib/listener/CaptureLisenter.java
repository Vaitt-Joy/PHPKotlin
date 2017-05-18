package com.evergrande.lib.cameraLib.listener;

/**
 * Created by huangwz on 2017/5/8.
 */

public interface CaptureLisenter {
    void takePictures();

    void recordShort(long time);

    void recordStart();

    void recordEnd(long time);

    void recordZoom(float zoom);
}

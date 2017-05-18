package com.evergrande.lib.cameraLib.api23;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.params.StreamConfigurationMap;

import com.evergrande.lib.cameraLib.api21.Camera2;
import com.evergrande.lib.cameraLib.base.PreviewImpl;
import com.evergrande.lib.cameraLib.base.Size;
import com.evergrande.lib.cameraLib.base.SizeMap;

/**
 * Created by huangwz on 2017/5/8.
 */


@TargetApi(23)
public class Camera2Api23 extends Camera2 {

    public Camera2Api23(Callback callback, PreviewImpl preview, Context context) {
        super(callback, preview, context);
    }

    @Override
    protected void collectPictureSizes(SizeMap sizes, StreamConfigurationMap map) {
        // Try to get hi-res output sizes
        android.util.Size[] outputSizes = map.getHighResolutionOutputSizes(ImageFormat.JPEG);
        if (outputSizes != null) {
            for (android.util.Size size : map.getHighResolutionOutputSizes(ImageFormat.JPEG)) {
                sizes.add(new Size(size.getWidth(), size.getHeight()));
            }
        }
        if (sizes.isEmpty()) {
            super.collectPictureSizes(sizes, map);
        }
    }

}

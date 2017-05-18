package com.evergrande.lib.tuyaLib;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by huangwz on 2017/5/8.
 * 涂鸦控件
 */

public class TuyaView extends View {
    private int[] paintColor = new int[]{
            Color.RED, 0xff99cc00, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK
    };
    private int screenWidth;
    private int screenHeight;
    private Paint mPaint;
    private Paint mBitmapPaint;
    private Paint mMaskPaint;
    private Path mPath;
    private Bitmap mBitmapInit;// 原图像
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private List<DrawPath> savePath;
    private List<DrawPath> deletePath;
    private Context context;
    private int currentStyle = 1;
    private int currentSize = 5;
    private int currentColor = Color.RED;
    private DrawPath dp;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;

    private boolean needTuYaTime = true, startDraw = false;
    private boolean needZoomView = false;// 手势放大

    private int mode = 0;// 触摸的模式
    private static final int DRAW = 1;//拖动
    private static final int ZOOM = 2;//放大

    private float oldDist = 1f;
    private PointF zoomMidPoint;// 放大的中心点
    private float mScale = 1f;// 放大的倍数（1-5）

    private OnSaveFinishListener mOnSaveFinishListener;

    public TuyaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = manager.getDefaultDisplay().getWidth();
        screenHeight = manager.getDefaultDisplay().getHeight();
        init();
    }

    public TuyaView(Context context, int width, int height) {
        super(context);
        this.context = context;
        screenWidth = width;
        screenHeight = height;
        init();
    }

    public void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initCanvas();
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }

    public void setmOnSaveFinishListener(OnSaveFinishListener mOnSaveFinishListener) {
        this.mOnSaveFinishListener = mOnSaveFinishListener;
    }

    private void initCanvas() {
        initPaint();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        if (currentStyle == 1) {
            mPaint.setStrokeWidth(currentSize);
            mPaint.setColor(currentColor);
        } else {
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(50);
        }

        mMaskPaint = new Paint();
        mMaskPaint.setStrokeWidth(3);
        mMaskPaint.setTextAlign(Paint.Align.RIGHT);
        mMaskPaint.setColor(Color.GREEN);
        mMaskPaint.setTextSize(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAW;
                startDraw = true;
                break;
            case MotionEvent.ACTION_MOVE://移动事件
                if (mode == DRAW) {//图片拖动事件
                    if (startDraw) {
                        mPath = new Path();
                        dp = new DrawPath();
                        dp.paint = mPaint;
                        dp.path = mPath;
                        touch_start(x, y);
                        startDraw = false;
                    }
                    touch_move(x, y);
                } else if (mode == ZOOM) {//图片放大事件
                    if (needZoomView) {
                        handleZoom(event);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mode == DRAW) {
                    touch_up();
                }
                mode = 0;
                break;
            //有手指离开屏幕，但屏幕还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                if (needZoomView)
                    mode = 0;
                break;
            //当屏幕上已经有触点（手指）,再有一个手指压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                if (!needZoomView) break;
                startDraw = false;
                if (mode == DRAW) {
                    mPath = new Path();
                    dp = new DrawPath();
                    dp.paint = mPaint;
                    dp.path = mPath;
                    invalidate();
                }
                mode = ZOOM;
                oldDist = getFingerSpacing(event);
                zoomMidPoint = mid(event);
                break;
        }
        return true;

    }

    private void touch_start(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(mY - y);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(dp);
        mPath = null;
    }

    /**
     * 放大缩小
     *
     * @param event
     */
    private void handleZoom(MotionEvent event) {
        float newDist = getFingerSpacing(event);
        if (newDist > 20f) {
            if (newDist > oldDist) {
                mScale += 0.05f;
            } else if (newDist < oldDist) {
                mScale -= 0.05f;
            }

            if (mScale <= 1f) {
                mScale = 1f;
            } else if (mScale >= 5f) {
                mScale = 5f;
            }

            if (mScale >= 1f && mScale <= 5) {
                setPivotX(zoomMidPoint.x);
                setPivotY(zoomMidPoint.y);
                setScaleX(mScale);
                setScaleY(mScale);
            }
            oldDist = newDist;
        }
    }

    /**
     * 计算两点的距离
     *
     * @param event
     * @return
     */
    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算两点之间中心点的距离
     *
     * @param event
     * @return
     */
    private static PointF mid(MotionEvent event) {
        float midx = event.getX(1) + event.getX(0);
        float midy = event.getY(1) + event.getY(0);

        return new PointF(midx / 2, midy / 2);
    }

    public void saveToSDCard(String filePath) {

        mBitmap = toConformBitmap(mBitmap, rectF);

        File file = new File(filePath);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        if (mOnSaveFinishListener != null) {
            mOnSaveFinishListener.onSaveFinish();
        }
        Log.e("TAG", "图片已保存");
    }

    private Bitmap toConformBitmap(Bitmap foreground, RectF imageRF) {
        if (imageRF == null) {
            int bgWidth = foreground.getWidth();
            int bgHeight = foreground.getHeight();
            Canvas cv = new Canvas(foreground);
            cv.drawBitmap(foreground, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
            if (needTuYaTime) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String curTime = formatter.format(curDate);
                cv.drawText(curTime, bgWidth - 30, bgHeight - 30, mMaskPaint);
            }
            cv.save(Canvas.ALL_SAVE_FLAG);//保存
            cv.restore();//存储
            return foreground;

        } else {
            int bgWidth = (int) imageRF.width();
            int bgHeight = (int) imageRF.height();
            Bitmap bitmap = Bitmap.createBitmap(foreground, (int) imageRF.left, (int) imageRF.top, bgWidth, bgHeight);
            Canvas cv = new Canvas(bitmap);
            cv.drawBitmap(bitmap, 0, 0, null);//在 0，0坐标开始画入fg ，可以从任意位置画入
            if (needTuYaTime) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String curTime = formatter.format(curDate);
                cv.drawText(curTime, bgWidth - 30, bgHeight - 30, mMaskPaint);
            }
            cv.save(Canvas.ALL_SAVE_FLAG);//保存
            cv.restore();//存储
            return bitmap;
        }
    }

    //撤销
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            redrawOnBitmap();
        }

    }

    //重做
    public void redo() {
        if (savePath != null && savePath.size() > 0) {
            savePath.clear();
            redrawOnBitmap();
        }
    }

    //恢复
    public void recover() {
        if (deletePath.size() > 0) {
            DrawPath drawPath = deletePath.get(deletePath.size() - 1);
            savePath.add(drawPath);
            mCanvas.drawPath(drawPath.path, drawPath.paint);
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }

    }


    private void redrawOnBitmap() {
        initCanvas();
        Iterator<DrawPath> iter = savePath.iterator();
        while (iter.hasNext()) {
            DrawPath drawPath = iter.next();
            mCanvas.drawPath(drawPath.path, drawPath.paint);
        }
        invalidate();
    }

    public void selectPaintStyle(int which) {
        if (which == 0) {
            currentStyle = 1;
            initPaint();
        }

        if (which == 1) {
            currentStyle = 2;
            initPaint();
        }
    }

    public void selectPaintSize(int which) {
        currentSize = which;
        initPaint();
    }

    public void selectPaintColor(int which) {
        currentColor = paintColor[which];
        initPaint();
    }

    public class DrawPath {
        public Path path;
        public Paint paint;
    }

    /**
     * @param uri get the uri of a picture
     * @author
     */
    public void setmBitmap(Uri uri) {
        Log.e("图片路径", String.valueOf(uri));
        ContentResolver cr = context.getContentResolver();
        try {
            mBitmapInit = BitmapFactory.decodeStream(cr.openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    public void setmBitmap(Bitmap bitmap) {
        mBitmapInit = bitmap;
        invalidate();
    }

    public boolean isNeedTuYaTime() {
        return needTuYaTime;
    }

    public void setNeedTuYaTime(boolean needTuYaTime) {
        this.needTuYaTime = needTuYaTime;
    }

    public boolean isNeedZoomView() {
        return needZoomView;
    }

    public void setNeedZoomView(boolean needZoomView) {
        this.needZoomView = needZoomView;
    }

    /**
     * 原图片区域
     */
    private RectF rectF;

    private void drawBitmapToCanvas(Bitmap bitmap) {
        // 图片宽高比
        float bScale = (float) bitmap.getWidth() / bitmap.getHeight();
        // 以屏幕宽为准，计算图片缩放后的高度
        float bitmapHeight = screenWidth / bScale;
        if (bitmapHeight < screenHeight) {
            // 以屏幕宽为准
            float whiteHeight = screenHeight - bitmapHeight;
            rectF = new RectF(0, whiteHeight / 2, screenWidth, bitmapHeight);
            mCanvas.drawBitmap(bitmap, null, rectF, mBitmapPaint);
        } else if (bitmapHeight > screenHeight) {
            // 以屏幕高度为准，计算图片缩放后的宽度
            float bitmapWidth = screenHeight * bScale;
            float whiteHeight = screenWidth - bitmapWidth;
            rectF = new RectF(whiteHeight / 2, 0, bitmapWidth, screenHeight);
            mCanvas.drawBitmap(bitmap, null, rectF, mBitmapPaint);
        } else {
            mCanvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        }

        if (rectF != null) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = (int) rectF.width();
            params.height = (int) rectF.height();
            setLayoutParams(params);
        }
    }

}

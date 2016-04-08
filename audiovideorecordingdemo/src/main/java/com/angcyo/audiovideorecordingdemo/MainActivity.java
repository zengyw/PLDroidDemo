package com.angcyo.audiovideorecordingdemo;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements CameraWrapper.CamOpenOverCallback {

    CameraTexturePreview mCameraTexturePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCameraTexturePreview = (CameraTexturePreview) findViewById(R.id.camera_textureview);

        Paint blurPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        blurPaint.setColorFilter(new ColorMatrixColorFilter(cm));
//        blurPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL));
//        blurPaint.setShader(new RadialGradient(0.5f, 0.5f, 0.2f, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.MIRROR));
        mCameraTexturePreview.setLayerType(View.LAYER_TYPE_HARDWARE, blurPaint);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void switchCamera(View view) {
        CameraWrapper.getInstance().doStopCamera();
        CameraWrapper.getInstance().switchCameraId();
        openCamera();
    }

    public void startAndStop(View view) {
        String tag = (String) view.getTag();
        if (tag.equalsIgnoreCase("stop")) {
            CameraWrapper.getInstance().doStopCamera();
            view.setTag("start");
        } else {
            openCamera();
            view.setTag("stop");
        }
    }

    public void crash(View view) {
        throw new IllegalArgumentException("崩溃测试...");
    }


    @Override
    protected void onStart() {
        super.onStart();

        openCamera();
    }

    private void openCamera() {
        Thread openThread = new Thread() {
            @Override
            public void run() {
                CameraWrapper.getInstance().doOpenCamera(MainActivity.this);
            }
        };
        openThread.start();
    }

    @Override
    public void cameraHasOpened() {
        SurfaceTexture surface = this.mCameraTexturePreview.getSurfaceTexture();
        CameraWrapper.getInstance().doStartPreview(surface);
    }
}
package com.example.chatapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceOrientedMeteringPointFactory;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.chatapp.MainActivity;
import com.example.chatapp.Models.app.FocusCircle;
import com.example.chatapp.R;
import com.example.chatapp.SendPictureActivity;
import com.google.common.util.concurrent.ListenableFuture;
import com.lukelorusso.verticalseekbar.VerticalSeekBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


@SuppressWarnings("deprecation")
public class CameraTabFragment extends Fragment implements View.OnTouchListener {

    private View cameraTabView;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private MainActivity mainAct;

    private Camera camera;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;

    private PreviewView cameraView;
    private ImageView cameraFlipButton, flashButton, imageCaptureButton;
    //private VerticalSeekBar zoomSeekBar;

    private ImageView capturedImage, sendButton;

    private boolean canFocus = true;

    private ConstraintLayout cameraLayout;
    public RelativeLayout saveImageLayout;

    int lensFacing = CameraSelector.LENS_FACING_BACK;
    int flashMode = ImageCapture.FLASH_MODE_OFF;

    public static boolean isPicture = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cameraTabView = inflater.inflate(R.layout.fragment_camera_tab, container, false);
        init();

        return cameraTabView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainAct = (MainActivity) context;
    }

    public void init() {

        cameraView = cameraTabView.findViewById(R.id.texture_for_camera);

        flashButton = cameraTabView.findViewById(R.id.flash_button);
        cameraFlipButton = cameraTabView.findViewById(R.id.flip_camera_button);
        imageCaptureButton = cameraTabView.findViewById(R.id.image_capture_button);

        //TODO- Check if seekbar is needed
        //zoomSeekBar = cameraTabView.findViewById(R.id.zoom_bar);

        capturedImage = cameraTabView.findViewById(R.id.captured_image);
        sendButton = cameraTabView.findViewById(R.id.save_button);

        cameraLayout = cameraTabView.findViewById(R.id.camera_view_layout);
        saveImageLayout = cameraTabView.findViewById(R.id.save_image_layout);

        if(isPicture)
            hideCamera();

        else
            showCamera();

        cameraFlipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lensFacing == CameraSelector.LENS_FACING_BACK)
                    lensFacing = CameraSelector.LENS_FACING_FRONT;

                else
                    lensFacing = CameraSelector.LENS_FACING_BACK;

                startCamera();
            }
        });

        if(hasAllPermissions()){
            startCamera();
        }

        else{
            ActivityCompat.requestPermissions(mainAct, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    public boolean hasAllPermissions(){

        for(String permission: REQUIRED_PERMISSIONS){

            if(ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }

        return true;
    }

    public void startCamera(){

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {

            try {

                ProcessCameraProvider processCameraProvider = cameraProviderFuture.get();
                bindPreview(processCameraProvider, false);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(getContext()));
    }

    public void bindPreview(ProcessCameraProvider processCameraProvider, boolean picTaken){

        Size size = new Size(cameraView.getWidth() * 2, cameraView.getHeight() * 2);
        cameraView.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);

        Preview preview = new Preview.Builder()
                .setTargetResolution(size)
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();

        ImageCapture imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setTargetResolution(size)
                .setTargetRotation(Surface.ROTATION_0)
                .setFlashMode(flashMode)
                .build();

        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flashMode == ImageCapture.FLASH_MODE_ON){

                    flashMode = ImageCapture.FLASH_MODE_OFF;
                    imageCapture.setFlashMode(flashMode);
                    flashButton.setImageResource(R.drawable.flash_off);
                }

                else{
                    flashMode = ImageCapture.FLASH_MODE_ON;
                    imageCapture.setFlashMode(flashMode);
                    flashButton.setImageResource(R.drawable.flash_on);
                }
            }
        });
        imageCaptureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                File dir = new File(Environment.getExternalStorageDirectory() + "/Emotext/");

                if(!dir.exists())
                    dir.mkdirs();

                File file = new File(Environment.getExternalStorageDirectory() + "/Emotext/" + System.currentTimeMillis() + ".jpg");

                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file)
                        .build();

                Bitmap image = cameraView.getBitmap();
                capturedImage.setImageBitmap(image);

                hideCamera();

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {*/

                        startActivity(new Intent(getContext(), SendPictureActivity.class));

                        //}
                    }
                });

                cameraTabView.findViewById(R.id.close_picture).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showCamera();
                    }
                });
            }
        });

        processCameraProvider.unbindAll();
        preview.setSurfaceProvider(cameraView.createSurfaceProvider());
        camera = processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

        scaleGestureDetector = new ScaleGestureDetector(getContext(),scaleGestureDetectorListener);
        gestureDetector = new GestureDetector(getContext(), gestureListener);

        /*zoomSeekBar.setOnProgressChangeListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {

                Log.i("TestingCamera", "Progress: " + integer);
                camera.getCameraControl().setLinearZoom((float)integer/(float)100);
                return null;
            }
        });*/

        cameraView.setOnTouchListener(this);
    }


    public void convertBitToString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
    }


    private void hideCamera(){

        cameraLayout.setVisibility(View.GONE);
        MainActivity.peopleButtonLayout.setVisibility(View.GONE);
        MainActivity.chatButtonLayout.setVisibility(View.GONE);

        saveImageLayout.setVisibility(View.VISIBLE);
        MainActivity.mainViewPager.setSwipeEnabled(false);
    }

    private void showCamera(){

        cameraLayout.setVisibility(View.VISIBLE);
        MainActivity.chatButtonLayout.setVisibility(View.VISIBLE);
        MainActivity.peopleButtonLayout.setVisibility(View.VISIBLE);

        saveImageLayout.setVisibility(View.GONE);
        MainActivity.mainViewPager.setSwipeEnabled(true);
    }


    private ScaleGestureDetector.OnScaleGestureListener scaleGestureDetectorListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float zoomRatio = camera.getCameraInfo().getZoomState().getValue().getZoomRatio();
            float delta = detector.getScaleFactor();
            float multipler = delta >= 1 ? 1.05f : 0.95f;

            float finalZoom = zoomRatio * delta * multipler;

            int zoomProgress = Math.min((int)(finalZoom * 12.5), 100);
            Log.i("TestingCamera", "zoomProgress: " + zoomProgress);

            //zoomSeekBar.setProgress(zoomProgress);

            camera.getCameraControl().setZoomRatio(finalZoom);

            return  true;
        }
    };

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            lensFacing = lensFacing == CameraSelector.LENS_FACING_BACK ? CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK;
            startCamera();

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            MeteringPointFactory factory = new SurfaceOrientedMeteringPointFactory((float) cameraView.getWidth(), (float) cameraView.getHeight());
            MeteringPoint point = factory.createPoint(e.getX(), e.getY());

            if(canFocus){

                FocusCircle focusCircle = new FocusCircle(getContext());
                Canvas canvas = new Canvas();

                cameraView.addView(focusCircle);

                focusCircle.setXY(e.getX(), e.getY());
                focusCircle.draw(canvas);
                focusCircle.setElevation(2);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        cameraView.removeView(focusCircle);
                    }
                }, 400);


                FocusMeteringAction action = new FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AE)
                        .setAutoCancelDuration(15, TimeUnit.SECONDS)
                        .build();

                camera.getCameraControl().startFocusAndMetering(action);

                canFocus = false;

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        toggleCanFocus();
                    }
                }, 600);
            }
            return true;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        return true;
    }


    private void toggleCanFocus(){
        canFocus = true;
    }

}
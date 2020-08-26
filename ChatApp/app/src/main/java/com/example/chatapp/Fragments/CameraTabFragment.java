package com.example.chatapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("deprecation")
public class CameraTabFragment extends Fragment {

    private View cameraTabView;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private MainActivity mainAct;


    private PreviewView cameraView;
    private ImageView cameraFlipButton, flashButton, imageCaptureButton;

    int lensFacing = CameraSelector.LENS_FACING_BACK;

    private ImageCapture imageCapture;
    int flashMode = ImageCapture.FLASH_MODE_ON;

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
            Log.d("success", "Camera Started");
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
            Log.d("success", "Permissions request");
        }

        return true;
    }

    public void startCamera(){


        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {

            try {

                ProcessCameraProvider processCameraProvider = cameraProviderFuture.get();
                bindPreview(processCameraProvider, "");

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(getContext()));
    }

    public void bindPreview(ProcessCameraProvider processCameraProvider, String check){

        cameraView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);

        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
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

                imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.i("Testing", "It works " + file.getAbsolutePath());
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.i("Testing", exception.toString());
                    }
                });
            }
        });

        processCameraProvider.unbindAll();
        preview.setSurfaceProvider(cameraView.createSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    /*public void updateTransform(){

        Matrix matrix = new Matrix();
        float w = cameraView.getMeasuredWidth();
        float h = cameraView.getMeasuredHeight();

        float cw = w / 2f;
        float ch = h / 2f;

        int rotationDgr;
        int rotation = (int) cameraView.getRotation();

        switch (rotation){

            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;

            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;

            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;

            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;

            default:
                return;
        }
        matrix.postRotate(rotationDgr, cw, ch);
        //cameraView.setTransform(matrix);
    }*/
}
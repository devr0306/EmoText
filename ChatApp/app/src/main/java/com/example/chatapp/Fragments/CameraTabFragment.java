package com.example.chatapp.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


@SuppressWarnings("deprecation")
public class CameraTabFragment extends Fragment {

    private View cameraTabView;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private MainActivity mainAct;


    private PreviewView cameraView;
    private ImageView cameraFlipButton, flashButton;

    int lensFacing = CameraSelector.LENS_FACING_BACK;

    private ImageCapture imageCapture;
    int flashMode = ImageCapture.FLASH_MODE_OFF;

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
                bindPreview(processCameraProvider);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(getContext()));
    }

    public void bindPreview(ProcessCameraProvider processCameraProvider){

        processCameraProvider.unbindAll();

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

        preview.setSurfaceProvider(cameraView.createSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    public void captureImage(){

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(Surface.ROTATION_0)
                .setFlashMode(flashMode)
                .build();
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
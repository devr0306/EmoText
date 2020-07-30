package com.example.chatapp.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;

import java.io.File;


@SuppressWarnings("deprecation")
public class CameraTabFragment extends Fragment {

    private View cameraTabView;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final String[] REQUIRED_PERMISSIONS = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private MainActivity mainAct;

    private static ImageCaptureConfig imageCaptureConfig;
    private static ImageCapture imageCap;
    private static String filePath;
    private static int imageNumber;

    private TextureView cameraView;
    private ImageView cameraFlipButton, flashButton;


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

        imageNumber = 0;

        imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).
                setTargetRotation(mainAct.getWindowManager().getDefaultDisplay().getRotation()).build();
        imageCap = new ImageCapture(imageCaptureConfig);

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

        CameraX.unbindAll();

        Rational aspectRatio = new Rational(cameraView.getWidth(), cameraView.getHeight());
        Size screen = new Size(cameraView.getWidth(), cameraView.getHeight());

        PreviewConfig pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {

                ViewGroup parent = (ViewGroup) cameraView.getParent();
                parent.removeView(cameraView);
                parent.addView(cameraView);

                cameraView.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });

        CameraX.bindToLifecycle(this, preview, imageCap);
    }

    public static void captureImage(MainActivity mainActivity){

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).
                setTargetRotation(mainActivity.getWindowManager().getDefaultDisplay().getRotation()).build();
        imageCap = new ImageCapture(imageCaptureConfig);

        filePath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/";

        String fileName = "IMAGE" + imageNumber++ + ".jpg";

        File file = new File(filePath + fileName);
        Log.i("Success", "File created:  " + file.getAbsolutePath());

        imageCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
            @Override
            public void onImageSaved(@NonNull File file) {

                //Toast.makeText(getContext(), "Image saved at " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                Log.i("Success", "Image saved at " + file.getAbsolutePath());
            }

            @Override
            public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {

                //Toast.makeText(getContext(), "Unsuccessful" + message, Toast.LENGTH_SHORT).show();
                Log.i("Success", cause.toString());
            }
        });
    }

    public void updateTransform(){

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
        cameraView.setTransform(matrix);
    }
}
package com.example.chatapp.Models.app;

import android.Manifest;

import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VoiceRecorder {

    private final String AUDIO_REQUEST = Manifest.permission.RECORD_AUDIO;
    private final int REQUEST_CODE = 21;
    private Context context;

    private MediaRecorder mediaRecorder;
    private String recordFile;
    private String filePath;

    public VoiceRecorder(Context context){

        this.context = context;
    }

    public boolean checkPermissions(){

        if(hasPermissions()){
            return true;
        }

        else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{AUDIO_REQUEST}, REQUEST_CODE);
            return false;
        }
    }

    public boolean hasPermissions(){

        if(ActivityCompat.checkSelfPermission(context, AUDIO_REQUEST) == PackageManager.PERMISSION_GRANTED){
            return true;
        }

            return false;
    }

    public void startRecording(){

        filePath = context.getExternalFilesDir("/").getAbsolutePath();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US);
        Date now = new Date();
        recordFile = "Recording_" + formatter.format(now) + ".3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(filePath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mediaRecorder.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopRecording(){

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }
}

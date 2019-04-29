package com.hfad.shottimer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.Handler;
import android.widget.TextView;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class timerActivity extends AppCompatActivity {
    //Variables
    Button btnRecord, btnStopRecording, btnPlay, btnStop;
    //    String pathSaved = "";
    MediaPlayer mediaPlayer;
    final int REQUEST_PERMISSION_CODE = 1000;

    MediaRecorder mRecorder;
    Thread thread;
    long oldTime = 0;
    long newTime = 0;
    static long startTime = 0;
    ;
    final Runnable updater = new Runnable() {
        public void run() {
            recordShots();
        }
    };
    final Handler handler = new Handler();

    RecyclerView recyclerView;

    ShotRecyclerAdapter adapter;

    public static long getTimer() {
        return startTime;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Shot.shotList.clear();
        Shot.COUNTER = 1;
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShotRecyclerAdapter(this, Shot.shotList);
        recyclerView.setAdapter(adapter);

        //Buttons for Playing and stopping
        btnPlay = findViewById(R.id.playAudio);
        btnRecord = findViewById(R.id.start);
        btnStop = findViewById(R.id.stopAudio);
        btnStopRecording = findViewById(R.id.stop);

        if(checkPermissionFromDevice()){
            btnRecord.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (thread == null) {
                        thread = new Thread() {
                            public void run() {
                                while (thread != null) {
                                    try {
                                        Thread.sleep(20);
                                    } catch (InterruptedException e) {
                                    }
                                    handler.post(updater);
                                }
                            }
                        };
                        thread.start();
                    }
////                    pathSaved = Environment.getExternalStorageDirectory().getAbsolutePath()
////                            +"/"+ UUID.randomUUID().toString()+"_audio_record.3gp";
                    btnStopRecording.setEnabled(true);
//                    btnPlay.setEnabled(false);
//                    btnStop.setEnabled(false);
                    Toast.makeText(timerActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                }

            });
            btnStopRecording.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    thread=null;
                    btnStopRecording.setEnabled(false);
//                    btnPlay.setEnabled(true);
                    btnRecord.setEnabled(true);
//                    btnStop.setEnabled(false);

                    //Crate Statistics object upon ending the session.
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    Stats tempStat = new Stats(Shot.shotList);
                    tempStat.setDate(currentDateandTime);
                    tempStat.setStatNumber(Stats.COUNTERSTAT);
                    Stats.statList.add(tempStat);

                    //Shot.shotList.clear();
                    Stats.COUNTERSTAT++;

                }
            });
//            btnPlay.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                    btnStop.setEnabled(true);
//                    btnStopRecording.setEnabled(false);
//                    btnStopRecording.setEnabled(false);
//                    Toast.makeText(timerActivity.this, "Playing", Toast.LENGTH_SHORT).show();
//                }
//            });
//            btnStop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    btnStopRecording.setEnabled(false);
//                    btnRecord.setEnabled(true);
//                    btnStop.setEnabled(false);
//                    btnPlay.setEnabled(true);
//
//                    if(mediaPlayer != null){
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        startRecorder();
//                    }
//                }
//            });
        }else{
            requestPermissions();
        }
    }

    public void onResume() {
        super.onResume();
        startRecorder();
    }

    public void onPause() {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (java.io.IOException ioe) {
            } catch (java.lang.SecurityException e) {
            }
            try {
                mRecorder.start();
            } catch (java.lang.SecurityException e) {
            }
        }
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void recordShots() {
        double db = decibels();
        if (db > 70) {
            newTime = getCurrentTime();
            double timeDiff = (timeDifference(oldTime,newTime));
            if (oldTime == 0) {
                startTime = newTime;
                Shot shot = new Shot((double)(timeDifference(startTime, newTime)));
                Shot.shotList.add(shot);
                adapter.notifyDataSetChanged();
                oldTime = newTime;
            }
            else if (timeDiff > 50) {
                Shot shot = new Shot((double)(timeDifference(startTime, newTime)));
                Shot.shotList.add(shot);
                adapter.notifyDataSetChanged();
                oldTime = newTime;
            }
        }
    }

    public double decibels() {
        return 20 * Math.log10(getAmplitude() / getAmplitudeFactor());
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return (mRecorder.getMaxAmplitude());
        else
            return 0;
    }

    public double getAmplitudeFactor() {
        double factor = .8;
        return factor;
    }

    public String getTimeStr(long milli, long startTime) {
        milli = milli - startTime;
        int milliseconds = (int)(milli % 1000);
        int seconds = (int)(Math.floor((milli / 1000) % 60));
        int minutes = (int)(Math.floor((milli / (60 * 1000)) % 60));
        return minutes + ":" + seconds + "." + milliseconds;
    }

    public long getCurrentTime() {
        return  System.currentTimeMillis();
    }

    public double timeDifference(double oldTime, double newTime) {
        return newTime - oldTime;
    }

//    private void setupMRecorder() {
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        mRecorder.setOutputFile(pathSaved);
//        TextView text = findViewById(R.id.pathSaved);
//        text.setText(this.pathSaved);
//    }

//    private void setupMediaRecorder() {
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        mediaRecorder.setOutputFile(pathSaved);
//        TextView text = findViewById(R.id.pathSaved);
//        text.setText(this.pathSaved);
//    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO}
                ,REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    public boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
}

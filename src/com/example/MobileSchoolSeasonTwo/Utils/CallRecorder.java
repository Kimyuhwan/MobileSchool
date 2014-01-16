package com.example.MobileSchoolSeasonTwo.Utils;

import android.media.MediaRecorder;
import android.util.Log;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;

/**
 * User: yuhwan
 * Date: 13. 11. 5
 * Time: 오후 8:34
 */
public class CallRecorder {
    private String TAG = Constants.TAG;
    private MediaRecorder mediaRecorder;
    private String recorder_path = Constants.RECODER_PATH;
    private Boolean isRecording = false;

    public CallRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    public void startRecording() {
        Log.d(TAG, "CallRecorder startRecording");
        if(!isRecording) {
            String path = recorder_path + _getFileName();
            try {
                Log.d(TAG, "CallRecorder path : " + path);
                _makeFile(path);
                mediaRecorder.setOutputFile(path);
                mediaRecorder.prepare();
                mediaRecorder.start();
                isRecording = true;
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void stopRecording() {
        if(isRecording) {
            isRecording = false;
            mediaRecorder.stop();
            Log.d(TAG, "CallRecorder stopRecording");
        }
    }

    private String _getFileName() {
        String result = "";
        DateTime dateTime = new DateTime();
        result = dateTime.getYear() + "_" + dateTime.getMonthOfYear() + "_" + dateTime.getDayOfMonth() + "_";
        result += dateTime.getHourOfDay() + "_" + dateTime.getMinuteOfHour() + "_" +  dateTime.getSecondOfMinute();
        result += ".m4a";
        return result;
    }

    private void _makeFile(String path) {
        try {
            File recordFile = new File(path);
            if(!recordFile.getParentFile().exists())
                recordFile.getParentFile().mkdirs();
            if(!recordFile.exists())
                recordFile.createNewFile();
        } catch (IOException e) { e.printStackTrace();}

    }
}

package com.foscam.ipc.util;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.ipc.sdk.FSApi;

/**
 * @author amouly on 11/15/15.
 */
public class Talk implements Runnable {

    private AudioRecord mAudioRecord;                          // AudioTrack����
    private boolean hasRecordStart = false;
    private boolean isThreadRun = true;
    private byte[] buffer = new byte[960];
    private int bytesRead = 0;
    private int sendTalk = 0;
    private int deviceType = 1;

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        if (hasRecordStart) {
            hasRecordStart = false;
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }

        isThreadRun = false;
    }

    public void startTalk(int devType) {
        int minBufSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT);


        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize);

        try {
            mAudioRecord.startRecording();
        } catch (Exception e) {
        }

        hasRecordStart = true;
        deviceType = devType;

        FSApi.startTalk(0);
        sendTalk = 1;
    }

    public void stopTalk() {
        FSApi.stopTalk(0);
        if (hasRecordStart) {
            hasRecordStart = false;
            try {
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            } catch (Exception e) {
            }
        }
        sendTalk = 0;
    }

    public void run() {

        int bufLen = 960;

        while (isThreadRun) {
            // Record
            if (sendTalk == 1) {
                if (deviceType == 0) //MJ
                {
                    bufLen = 640;
                } else if (deviceType == 1) // H264
                {
                    bufLen = 960;
                } else {
                    bufLen = 960;
                }

                try {
                    bytesRead = mAudioRecord.read(buffer, 0, bufLen);
                    if (bytesRead > 0) {
                        FSApi.sendTalkFrame(buffer, bufLen, 0);
                    }

                } catch (Exception e) {
                }
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
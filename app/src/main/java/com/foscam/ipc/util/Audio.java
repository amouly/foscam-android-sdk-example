package com.foscam.ipc.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.ipc.sdk.AVStreamData;
import com.ipc.sdk.FSApi;

/**
 * @author amouly on 11/15/15.
 */
public class Audio implements Runnable {

    private AVStreamData audioStreamData = new AVStreamData();
    private AudioTrack mAudioTrack;                          // AudioTrack����
    private boolean hasPlayStart = false;
    private boolean isThreadRun = true;

    public void start() {

        int minBufSize = AudioTrack.getMinBufferSize(8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                8000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize * 2,
                AudioTrack.MODE_STREAM);

        mAudioTrack.play();

        hasPlayStart = true;

        new Thread(this).start();
    }

    public void stop() {
        if (hasPlayStart) {
            hasPlayStart = false;
            mAudioTrack.stop();
        }

        isThreadRun = false;
    }

    public void run() {

        while (isThreadRun) {
            // Play
            try {
                FSApi.getAudioStreamData(audioStreamData, 0);
            } catch (Exception e) {
                continue;
            }
            if (audioStreamData.dataLen > 0) {
                try {
                    mAudioTrack.write(audioStreamData.data, 0, audioStreamData.dataLen);
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
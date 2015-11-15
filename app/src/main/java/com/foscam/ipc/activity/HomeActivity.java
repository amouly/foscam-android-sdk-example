package com.foscam.ipc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foscam.ipc.util.Audio;
import com.foscam.ipc.R;
import com.foscam.ipc.util.Talk;
import com.foscam.ipc.view.VideoView;
import com.ipc.sdk.FSApi;
import com.ipc.sdk.StatusListener;
import com.foscam.ipc.util.ActivtyUtil;
import com.foscam.ipc.util.IPCameraInfo;

import java.io.File;
import java.lang.reflect.Field;

public class HomeActivity extends AppCompatActivity {

    /*
     * sdk support max 4 channel ,this demo use only one ,so ID set 0.
     *
     * Record only support H264 model.
     *
     */
    Context mContext;
    boolean IsRun = false;
    boolean isInLiveViewPage = true;
    VideoView vv;
    Audio mAudio = new Audio();
    Talk mTalk = new Talk();

    RelativeLayout containerLayout;
    ImageView btn_snap;
    ImageView btn_audio;
    private int audioState = 0;
    ImageView btn_talk;
    private int talkState = 0;

    ImageView btn_Record;

    public static Handler mStatusMsgHandler;

    private IPCameraInfo lastConnectIpcInfo = new IPCameraInfo();
    private IPCameraInfo lastConnectIpcInfoTemp = new IPCameraInfo();
    private boolean hasConnected = false;
    private boolean IsRecord = false;

    private boolean isVVStarted = false;
    private boolean isVideoStreamStarted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        containerLayout = (RelativeLayout) findViewById(R.id.activity_home_container);

        mContext = this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        FSApi.Init();
        IsRun = true;

        isInLiveViewPage = true;

        lastConnectIpcInfo.devType = 0;
        lastConnectIpcInfo.devName = "";
        lastConnectIpcInfo.ip = "";
        lastConnectIpcInfo.streamType = 0;
        lastConnectIpcInfo.webport = 0;
        lastConnectIpcInfo.mediaport = 0;
        lastConnectIpcInfo.uid = "";
        lastConnectIpcInfo.userName = "";
        lastConnectIpcInfo.password = "";

        lastConnectIpcInfoTemp.devType = 0;
        lastConnectIpcInfoTemp.devName = "";
        lastConnectIpcInfoTemp.ip = "";
        lastConnectIpcInfoTemp.streamType = 0;
        lastConnectIpcInfoTemp.webport = 0;
        lastConnectIpcInfoTemp.mediaport = 0;
        lastConnectIpcInfoTemp.uid = "";
        lastConnectIpcInfoTemp.userName = "";
        lastConnectIpcInfoTemp.password = "";


        mStatusMsgHandler = new Handler() {
            public void handleMessage(Message msg) {
                String promoteString = "";
                switch (msg.arg1) {
                    case StatusListener.STATUS_LOGIN_SUCCESS:
                        promoteString = mContext.getResources().getString(R.string.login_promote_success);
                        break;
                    case StatusListener.STATUS_LOGIN_FAIL_USR_PWD_ERROR:
                        promoteString = mContext.getResources().getString(R.string.login_promote_fail_usr_pwd_error);
                        break;
                    case StatusListener.STATUS_LOGIN_FAIL_ACCESS_DENY:
                        promoteString = mContext.getResources().getString(R.string.login_promote_fail_access_deny);
                        break;
                    case StatusListener.STATUS_LOGIN_FAIL_EXCEED_MAX_USER:
                        promoteString = mContext.getResources().getString(R.string.login_promote_fail_exceed_max_user);
                        break;
                    case StatusListener.STATUS_LOGIN_FAIL_CONNECT_FAIL:
                        promoteString = mContext.getResources().getString(R.string.login_promote_fail_connect_fail);
                        break;
                    case StatusListener.FS_API_STATUS_OPEN_TALK_SUCCESS:
                        promoteString = mContext.getResources().getString(R.string.open_talk_promote_success);
                        break;
                    case StatusListener.FS_API_STATUS_OPEN_TALK_FAIL_USED_BY_ANOTHER_USER:
                        promoteString = mContext.getResources().getString(R.string.open_talk_promote_fail);
                        break;
                    case StatusListener.FS_API_STATUS_CLOSE_TALK_SUCCESS:
                        if (hasConnected) {
                            promoteString = mContext.getResources().getString(R.string.close_talk_promote_success);
                        }
                        break;
                    case StatusListener.FS_API_STATUS_CLOSE_TALK_FAIL:
                        promoteString = mContext.getResources().getString(R.string.close_talk_promote_fail);
                        break;
                    case 1000:
                        hasConnected = true;
                        vv.startVideoStream();

                        //save device info
                        lastConnectIpcInfo.devType = lastConnectIpcInfoTemp.devType;
                        lastConnectIpcInfo.devName = lastConnectIpcInfoTemp.devName;
                        lastConnectIpcInfo.streamType = lastConnectIpcInfoTemp.streamType;
                        lastConnectIpcInfo.ip = lastConnectIpcInfoTemp.ip;
                        lastConnectIpcInfo.webport = lastConnectIpcInfoTemp.webport;
                        lastConnectIpcInfo.mediaport = lastConnectIpcInfoTemp.mediaport;
                        lastConnectIpcInfo.uid = lastConnectIpcInfoTemp.uid;
                        lastConnectIpcInfo.userName = lastConnectIpcInfoTemp.userName;
                        lastConnectIpcInfo.password = lastConnectIpcInfoTemp.password;

                        break;
                    case 1001:

                        hasConnected = false;
                        vv.clearScreen();

                        break;


                    default:
                        promoteString = "";
                        break;
                }

                if (isInLiveViewPage) {
                    if (!"".equals(promoteString)) {
                        ActivtyUtil.openToast(mContext, promoteString);
                    }
                }
            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {
                int id;
                int StatusID;
                while (IsRun) {
                    for (id = 0; id < 4; id++) {
                        StatusID = FSApi.getStatusId(id);

                        if (StatusID < 0) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (StatusID != -1) {
                                Message msg = HomeActivity.mStatusMsgHandler.obtainMessage();
                                msg.arg1 = StatusID;
                                HomeActivity.mStatusMsgHandler.sendMessage(msg);
                            }
                            if (StatusID == StatusListener.STATUS_LOGIN_SUCCESS) {

                                Message msg = HomeActivity.mStatusMsgHandler.obtainMessage();
                                msg.arg1 = 1000;
                                HomeActivity.mStatusMsgHandler.sendMessage(msg);
                            } else if (StatusID == StatusListener.STATUS_LOGIN_FAIL_CONNECT_FAIL) {
                                Message msg = HomeActivity.mStatusMsgHandler.obtainMessage();
                                msg.arg1 = 1001;
                                HomeActivity.mStatusMsgHandler.sendMessage(msg);
                            }
                        }
                    }
                }

            }
        }).start();


        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        int width = metric.widthPixels;
        int height = metric.heightPixels;

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        vv = new VideoView(this, width, height - statusBarHeight);
        containerLayout.addView(vv, width, height - statusBarHeight);


        btn_snap = (ImageView) findViewById(R.id.btn_snap);
        btn_snap.setOnClickListener(new ClickEvent());
        btn_audio = (ImageView) findViewById(R.id.btn_audio);
        btn_audio.setOnClickListener(new ClickEvent());
        btn_talk = (ImageView) findViewById(R.id.btn_talk);
        btn_talk.setOnClickListener(new ClickEvent());
        btn_Record = (ImageView) findViewById(R.id.btn_Record);
        btn_Record.setOnClickListener(new ClickEvent());

        if (!isVVStarted) {
            isVVStarted = true;
            vv.start();
            mAudio.start();
            mTalk.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_devices: {
                onOpenDeviceList();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void onOpenDeviceList() {
        final Intent intent = new Intent(this, DeviceListActivity.class);

        this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isInLiveViewPage = true;

        SharedPreferences sharedata = getSharedPreferences("CONNECT_DEV_INFO", 0);

        int devType = sharedata.getInt("DEV_TYPE", 0);
        String devName = sharedata.getString("DEV_NAME", "");
        String ip = sharedata.getString("IP", "");
        int streamType = sharedata.getInt("STREAM_TYPE", 0);
        int webPort = sharedata.getInt("WEB_PORT", 0);
        int mediaPort = sharedata.getInt("MEDIA_PORT", 0);
        String userName = sharedata.getString("USER_NAME", "admin");
        String password = sharedata.getString("PASSWORD", "");
        String uid = sharedata.getString("UID", "");

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    if ("".equals(ip) && "".equals(uid)) {
                        mTalk.stopTalk();
                        talkState = 0;

                        btn_audio.setActivated(true);
                        audioState = 0;
                        FSApi.stopAudioStream(0);
                        vv.stopVideoStream();

                        android.os.SystemClock.sleep(500);

                        vv.clearScreen();

                        FSApi.usrLogOut(0);
                    }


                    boolean isNeedReconnect = false;

                    if ("".equals(uid)) {
                        if ((!"".equals(ip)) && (!"".equals(userName)) && (webPort > 0)) {
                            isNeedReconnect = true;
                        }
                    } else {
                        if ((!"".equals(uid)) && (!"".equals(userName))) {
                            isNeedReconnect = true;
                        }
                    }

                    if (isNeedReconnect) {
                        mTalk.stopTalk();
                        btn_talk.setActivated(false);
                        talkState = 0;

                        btn_audio.setActivated(true);
                        audioState = 0;
                        FSApi.stopAudioStream(0);
                        vv.stopVideoStream();

                        android.os.SystemClock.sleep(500);

                        vv.clearScreen();

                        FSApi.usrLogOut(0);

                        //  ��½
                        FSApi.usrLogIn(devType, ip, userName, password, streamType, webPort, mediaPort, uid, 0);
                        if (uid.length() > 0) {
                            //tv_devName.setText(devName + "(" + uid + ")");
                        } else {
                            //tv_devName.setText(devName + "(" + ip + ")");
                        }
                        hasConnected = false;

                        lastConnectIpcInfoTemp.devType = devType;
                        lastConnectIpcInfoTemp.devName = devName;
                        lastConnectIpcInfoTemp.streamType = streamType;
                        lastConnectIpcInfoTemp.ip = ip;
                        lastConnectIpcInfoTemp.webport = webPort;
                        lastConnectIpcInfoTemp.mediaport = mediaPort;
                        lastConnectIpcInfoTemp.uid = uid;
                        lastConnectIpcInfoTemp.userName = userName;
                        lastConnectIpcInfoTemp.password = password;

                        isVideoStreamStarted = true;
                    }

                } catch (Exception e) {
                    ActivtyUtil.showAlert(HomeActivity.this, "Error", e.getMessage(),
                            getResources().getString(R.string.btn_OK));
                }
            } else {
                if ("".equals(ip) && "".equals(uid)) {
                    btn_audio.setActivated(false);
                    audioState = 0;
                    FSApi.stopAudioStream(0);
                    vv.stopVideoStream();

                    android.os.SystemClock.sleep(500);

                    vv.clearScreen();

                    FSApi.usrLogOut(0);

                }
            }
        }
    }

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == btn_audio) {
                if (hasConnected) {
                    if (audioState == 0) {
                        btn_audio.setActivated(true);
                        FSApi.startAudioStream(0);
                        audioState = 1;
                        ActivtyUtil.openToast(HomeActivity.this, getResources().getString(R.string.open_audio_promote_success));
                    } else {
                        btn_audio.setActivated(false);
                        FSApi.stopAudioStream(0);
                        audioState = 0;
                        ActivtyUtil.openToast(HomeActivity.this, getResources().getString(R.string.close_audio_promote_success));
                    }
                }
            } else if (v == btn_talk) {
                if (hasConnected) {
                    if (talkState == 0) {
                        btn_talk.setActivated(true);
                        mTalk.startTalk(lastConnectIpcInfoTemp.devType);
                        talkState = 1;
                    } else {
                        btn_talk.setActivated(false);
                        mTalk.stopTalk();
                        talkState = 0;
                    }
                }
            } else if (v == btn_Record) {
                if (!IsRecord) {
                    IsRecord = true;
                    btn_Record.setActivated(true);
                    String SDPATH = Environment.getExternalStorageDirectory().toString();
                    String filepath = SDPATH + "/IPC/Video";
                    File file = new File(filepath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String fileName = System.currentTimeMillis() + ".avi";

                    FSApi.StartRecord(filepath + "/", fileName, 0);
                    Toast.makeText(HomeActivity.this, "start record", Toast.LENGTH_SHORT).show();
                } else {
                    IsRecord = false;
                    btn_Record.setActivated(false);
                    FSApi.StopRecord(0);
                    Toast.makeText(HomeActivity.this, "stop record", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}








package com.foscam.ipc.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.foscam.ipc.R;
import com.foscam.ipc.util.ActivtyUtil;
import com.foscam.ipc.util.DatabaseHelper;

import java.util.ArrayList;


public class DeviceConfigActivity extends AppCompatActivity {

    private static final String TAG = "CamMonitorConfigActivity";

    private Spinner sp_devType;
    private EditText ed_devName;
    private EditText ed_ip;
    private Spinner sp_streamType;
    private EditText ed_httpPort;
    private EditText ed_mediaPort;
    private EditText ed_userName;
    private EditText ed_password;
    private EditText ed_uid;

    private ArrayList<String> devTypeList = new ArrayList<String>();
    private ArrayAdapter<String> adapterDevType;
    private ArrayList<String> streamTypeList = new ArrayList<String>();
    private ArrayAdapter<String> adapterStreamType;

    private boolean isModify = false;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);

        devTypeList.add(getResources().getString(R.string.ipc_info_config_device_type_mj));
        devTypeList.add(getResources().getString(R.string.ipc_info_config_device_type_h264));
        adapterDevType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, devTypeList);
        adapterDevType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        streamTypeList.add(getResources().getString(R.string.ipc_info_config_stream_type_sub));
        streamTypeList.add(getResources().getString(R.string.ipc_info_config_stream_type_main));
        adapterStreamType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, streamTypeList);
        adapterStreamType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setContentView(R.layout.activity_device_config);

        sp_devType = (Spinner) findViewById(R.id.sp_camera_type);
        ed_devName = (EditText) findViewById(R.id.ed_camera_name);
        ed_ip = (EditText) findViewById(R.id.ed_ip);
        sp_streamType = (Spinner) findViewById(R.id.sp_stream_type);
        ed_httpPort = (EditText) findViewById(R.id.ed_http_port);
        ed_mediaPort = (EditText) findViewById(R.id.ed_media_port);
        ed_userName = (EditText) findViewById(R.id.ed_user_name);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_uid = (EditText) findViewById(R.id.ed_uid);


        fillView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm_device: {
                this.onConfirmClick();

                return true;
            }
            case android.R.id.home: {
                this.onCancelClick();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillView() {
        if (this.getIntent().getExtras() != null && this.getIntent().getExtras().containsKey("id")) {
            isModify = false;
            try {
                isModify = true;

                int id = this.getIntent().getExtras().getInt("id");
                this.id = id;
                DatabaseHelper helper = new DatabaseHelper(this);
                Cursor cursor = helper.query(id);
                cursor.moveToFirst();

                sp_devType.setAdapter(adapterDevType);
                if (cursor.getInt(0) == 0) //MJ
                {
                    sp_devType.setSelection(0, true);
                } else if (cursor.getInt(0) == 1) //H264
                {
                    sp_devType.setSelection(1, true);
                } else {
                    sp_devType.setSelection(0, true);
                }

                ed_devName.setText(cursor.getString(1));
                ed_ip.setText(cursor.getString(2));
                sp_streamType.setAdapter(adapterStreamType);
                sp_streamType.setSelection(cursor.getInt(3), true);
                ed_httpPort.setText(String.valueOf(cursor.getInt(4)));
                ed_mediaPort.setText(String.valueOf(cursor.getInt(5)));
                ed_uid.setText(cursor.getString(6));
                ed_userName.setText(cursor.getString(7));
                ed_password.setText(cursor.getString(8));
                cursor.close();
                helper.close();
            } catch (Exception e) {
                Log.d("moon", e.getMessage());
                //ActivtyUtil.openToast(IPCameraInfoConfig.this, "errorCauses of error:"+e.getMessage());
            }
        } else // Come from add device
        {
            sp_devType.setAdapter(adapterDevType);
            sp_devType.setSelection(1, true);
            sp_streamType.setAdapter(adapterStreamType);
            isModify = false;
        }
    }

    private void SaveData() {
        try {
            ContentValues contentValue = new ContentValues();

            String devName = ed_devName.getText().toString();
            if (devName == null)
                devName = "";
            String ip = ed_ip.getText().toString();
            if (ip == null)
                ip = "";
            String webPort = ed_httpPort.getText().toString();
            if (webPort == null)
                webPort = "";
            String mediaPort = ed_mediaPort.getText().toString();
            if (mediaPort == null)
                mediaPort = "";
            String uid = ed_uid.getText().toString();
            if (uid == null)
                uid = "";
            String userName = ed_userName.getText().toString();
            if (userName == null)
                userName = "";
            String password = ed_password.getText().toString();
            if (password == null)
                password = "";

            contentValue.put("devType", sp_devType.getSelectedItemId());
            contentValue.put("devName", devName.trim());
            contentValue.put("ip", ip.trim());
            contentValue.put("streamType", sp_streamType.getSelectedItemId());
            if ("".equals(webPort.trim())) {
                contentValue.put("webPort", 0);
            } else {
                contentValue.put("webPort", Integer.parseInt(webPort.trim()));
            }
            if ("".equals(mediaPort.trim())) {
                contentValue.put("mediaPort", 0);
            } else {
                contentValue.put("mediaPort", Integer.parseInt(mediaPort.trim()));
            }
            contentValue.put("uid", uid.trim());
            contentValue.put("userName", userName.trim());
            contentValue.put("password", password.trim());

            if (isModify) {
                DatabaseHelper.update(DeviceConfigActivity.this, "tb_device_list", contentValue, id);
            } else {
                // check if alread exist
                boolean isAlreadyExist = false;
                if ("".equals(uid)) {
                    Cursor cur = DatabaseHelper.QueryDevice(this, ip, Integer.parseInt(webPort.trim()));
                    if (cur.getCount() > 0) {
                        isAlreadyExist = true;
                    }
                    cur.close();
                } else {
                    Cursor cur = DatabaseHelper.QueryDevice(this, uid);
                    if (cur.getCount() > 0) {
                        isAlreadyExist = true;
                    }
                    cur.close();
                }
                if (isAlreadyExist == false) {
                    DatabaseHelper.insert(DeviceConfigActivity.this, "tb_device_list", contentValue);
                } else {
                    ActivtyUtil.openToast(DeviceConfigActivity.this, getResources().getString(R.string.ipc_info_config_info_already_exist));
                    return;
                }
            }
        } catch (Exception e) {
            ActivtyUtil.openToast(DeviceConfigActivity.this, "Error, error causes:" + e.getMessage());
        }
    }


    private void onConfirmClick() {
        int webPort = 0;
        int mediaPort = 0;
        String ip = ed_ip.getText().toString();
        String strWebPort = ed_httpPort.getText().toString();
        String strMediaPort = ed_mediaPort.getText().toString();
        String uid = ed_uid.getText().toString();

        try {
            if ((strWebPort != null) && (!"".equals(strWebPort))) {
                webPort = Integer.parseInt(strWebPort.trim());
            }
            if ((strMediaPort != null) && (!"".equals(strMediaPort.trim()))) {
                mediaPort = Integer.parseInt(strMediaPort.trim());
            }
        } catch (Exception e) {
            ActivtyUtil.openToast(DeviceConfigActivity.this, getResources().getString(R.string.ipc_info_config_info_port_format_error));
            return;
        }

        Log.d("moon", "ip:" + ip + ",webPort:" + webPort + ",mediaPort:" + mediaPort);

        boolean isInfoCorrect = true;
        if (((ip == null) || ("".equals(ip)))) {

            if (((uid == null) || ("".equals(uid)))) {
                isInfoCorrect = false;
            }
        } else {
            if (webPort == 0) {
                isInfoCorrect = false;
            }
        }


        if (isInfoCorrect) {
            SaveData();

            setResult(Activity.RESULT_OK);
            finish();

            if (isModify) {
                ActivtyUtil.openToast(DeviceConfigActivity.this, getResources().getString(R.string.ipc_info_config_save_success));
            } else {
                ActivtyUtil.openToast(DeviceConfigActivity.this, getResources().getString(R.string.ipc_info_config_add_success));
            }
        } else {
            ActivtyUtil.openToast(DeviceConfigActivity.this, getResources().getString(R.string.ipc_info_config_info_not_correct));
        }
    }

    private void onCancelClick() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}

package com.ipc.sdk;

public interface StatusListener {
    public static final int STATUS_LOGIN_SUCCESS = 0x00;
    public static final int STATUS_LOGIN_FAIL_USR_PWD_ERROR = 0x01;
    public static final int STATUS_LOGIN_FAIL_ACCESS_DENY = 0x02;
    public static final int STATUS_LOGIN_FAIL_EXCEED_MAX_USER = 0x03;
    public static final int STATUS_LOGIN_FAIL_CONNECT_FAIL = 0x04;
    public static final int FS_API_STATUS_LOGIN_ERROR_UNKNOW = 0x05;
    public static final int FS_API_STATUS_SNAP_SUCCESS = 0x06;


    // open video status
    public static final int FS_API_STATUS_OPEN_VIDEO_SUCCESS = 0x10;
    public static final int FS_API_STATUS_OPEN_VIDEO_CONNECTING = 0x11;
    public static final int FS_API_STATUS_OPEN_VIDEO_FAIL = 0x12;
    // IR mode
    public static final int FS_API_STATUS_IR_MODE_AUTO = 0x13;
    public static final int FS_API_STATUS_IR_MODE_MANUAL_ON = 0x14;
    public static final int FS_API_STATUS_IR_MODE_MANUAL_OFF = 0x15;
    public static final int FS_API_STATUS_CHANGE_USER_PWD_OK = 0x16;
    public static final int FS_API_STATUS_CHANGE_USER_PWD_ERROR = 0x17;


    // talk
    public static final int FS_API_STATUS_OPEN_TALK_SUCCESS = 0x30;
    public static final int FS_API_STATUS_OPEN_TALK_FAIL_ACCESS_DENY = 0x31;
    public static final int FS_API_STATUS_OPEN_TALK_FAIL_USED_BY_ANOTHER_USER = 0x32;
    public static final int FS_API_STATUS_CLOSE_TALK_SUCCESS = 0x33;
    public static final int FS_API_STATUS_CLOSE_TALK_FAIL = 0x34;


    // get device state
    public static final int FS_API_STATUS_GET_DEV_STATE_SUCCESS = 0x40;
    public static final int FS_API_STATUS_GET_IR_MODE_SUCCESS = 0x41;
    public static final int FS_API_STATUS_GET_MOTION_DETECT_CONFIG_SUCCESS = 0x42;
    public static final int FS_API_STATUS_GET_DEV_INFO_SUCCESS = 0x43;
    public static final int FS_API_STATUS_GET_USER_LIST_SUCCESS = 0x44;
    public static final int FS_API_STATUS_GET_PRESET_POINT_LIST_SUCCESS = 0x45;
    public static final int FS_API_STATUS_GET_MUSICS_NAME_SUCCESS = 0x46;
    public static final int FS_API_STATUS_GET_PTZ_SPEED_SUCCESS = 0x47;
    public static final int FS_API_STATUS_GET_PTZ_SELF_TEST_SUCCESS = 0x48;
    public static final int FS_API_STATUS_GET_PTZ_SELF_TEST_PRESET_SUCCESS = 0x49;
    public static final int FS_API_STATUS_GET_PTZ_CRUISE_MAP_LIST_SUCCESS = 0x4a;
    public static final int FS_API_STATUS_GET_PTZ_CRUISE_MAP_INFO_SUCCESS = 0x4b;
    public static final int FS_API_STATUS_GET_USER_PERMISSION_MJ_SUCCESS = 0x4c;

    //alarm signal
    public static final int FS_API_STATUS_MOTION_DETECT_ALARM_SIGNAL = 0x50;
    public static final int FS_API_STATUS_MJ_ALARM_SIGNAL_START = 0x51;
    public static final int FS_API_STATUS_MJ_ALARM_SIGNAL_END = 0x52;


    //data
    public static final int FS_API_STATUS_GET_SESSION_LIST_SUCCESS = 0x60;
    public static final int FS_API_STATUS_GET_LOG_SUCCESS = 0x61;
    public static final int FS_API_STATUS_REFRESH_WIFI_LIST_SUCCESS = 0x62;
    public static final int FS_API_STATUS_REFRESH_WIFI_LIST_FAILT = 0x63;
    public static final int FS_API_STATUS_GET_WIFI_LIST_SUCCESS = 0x64;
    public static final int FS_API_STATUS_GET_SYSTEM_TIME_SUCCESS = 0x65;
    public static final int FS_API_STATUS_DELETE_PRESET_POINT_SUCCESS = 0x66;


    public static final int FS_API_STATUS_DELETE_PRESET_POINT_FAIL_ON_START = 0x70;
    public static final int FS_API_STATUS_DELETE_PRESET_POINT_FAIL_IN_CRUISE_MAP = 0x71;


    public void OnStatusCbk(int statusID, int reserve1, int reserve2, int reserve3, int reserve4);
}

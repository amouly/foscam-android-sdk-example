/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ipc.sdk;

public class FSApi {

    private static StatusListener mListener;

    /**
     * You must start a new thread to get the statusCallback constantly.
     */


    private static void StatusCbk(int statusID, int reserve1, int reserve2, int reserve3, int reserve4) {

        mListener.OnStatusCbk(statusID, reserve1, reserve2, reserve3, reserve4);
    }

    public static void setStatusListener(StatusListener listener) {
        mListener = listener;
    }

    /**
     * Initialize SDK when application start.
     */
    public static native int Init();

    /**
     * Release the SDK resources.
     */
    public static native int Uninit();

    /**
     * Start search cameras in your LAN .
     */
    public static native int searchDev();

    /**
     * Get the Device List in you LAN.
     *
     * @return the DevInfo Array.
     */
    public static native DevInfo[] getDevList();

    public static native int getStatusId(int id);

    /**
     * Login to Camera.
     *
     * @param devType    the device type(0 is MJPEG,1 is H264).
     * @param ip         the device IP address.
     * @param userName   the device UserName.
     * @param password   the device PassWord.
     * @param streamType the StreamType you want.(0 is sub , 1 is main)
     * @param webPort    the device WebPort.
     * @param mediaPort  the device MediaPort.(In new models ,this port is canceled)
     * @param uid        the device UID.
     * @param id         the device channel id.
     * @return Return immediately, login result will be found in StatusCallback
     */
    public static native int usrLogIn(int devType, String ip, String userName, String password, int streamType, int webPort, int mediaPort, String uid, int id);

    /**
     * Logout to Camera.
     *
     * @param id the device channel id.
     */
    public static native int usrLogOut(int id);

    /**
     * Control the camera to move up.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveUp(int id);

    /**
     * Control the camera to move down.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveDown(int id);

    /**
     * Control the camera to move left.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveLeft(int id);

    /**
     * Control the camera to move right.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveRight(int id);

    /**
     * Control the camera to move topLeft.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveTopLeft(int id);

    /**
     * Control the camera to move topRight.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveTopRight(int id);

    /**
     * Control the camera to move bottomLeft.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveBottomLeft(int id);

    /**
     * Control the camera to move bottomRight.
     *
     * @param id the device channel id.
     */
    public static native int ptzMoveBottomRight(int id);

    /**
     * Stop the camera move.
     *
     * @param id the device channel id.
     */
    public static native int ptzStopRun(int id);

    /**
     * Start  video .
     *
     * @param id the device channel id.
     */
    public static native int startVideoStream(int id);

    /**
     * Get the video stream data.
     *
     * @param streamData the AVStreamData object to store the video data.
     * @param id         the device channel id.
     */
    public static native int getVideoStreamData(AVStreamData streamData, int id);

    /**
     * Stop  video .
     *
     * @param id the device channel id.
     */
    public static native int stopVideoStream(int id);

    /**
     * start  audio .
     *
     * @param id the device channel id.
     */
    public static native int startAudioStream(int id);

    /**
     * Get the audio stream data .
     *
     * @param streamData the AVStreamData object to store the audio data.
     * @param id         the device channel id.
     */
    public static native int getAudioStreamData(AVStreamData streamData, int id);

    /**
     * Stop  audio .
     *
     * @param id the device channel id.
     */
    public static native int stopAudioStream(int id);

    /**
     * Start  talk .
     *
     * @param id the device channel id.
     */
    public static native int startTalk(int id);

    /**
     * Send the talk Frame to device.
     *
     * @param frame    the talk data.
     * @param frameLen the length of talk data.
     * @param id       the device channel id.
     */
    public static native int sendTalkFrame(byte[] frame, int frameLen, int id);

    /**
     * Stop  Talk .
     *
     * @param id the device channel id.
     */
    public static native int stopTalk(int id);

    /**
     * Snap picture and save to SD card.
     *
     * @param saveDir the snap save path(must contains the picture suffix).
     * @param id      the device channel id.
     */
    public static native int snapPic(String saveDir, int id);


    /**
     * Start Record .
     *
     * @param dir     the save folder where you will save.(The SD card available  size must be more than 256 MB)
     * @param saveDir the Record file name.(Should be like *.AVI)
     * @param id      the device channel id.
     */
    public static native int StartRecord(String dir, String fileName, int id);

    /**
     * Stop Record.
     *
     * @param id the device channel id.
     */
    public static native int StopRecord(int id);


    /**
     * Control the camera to move Center.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int ptzCenter(int id);

    /**
     * Control the camera Zoom In.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int ZoomIn(int id);

    /**
     * Control the camera Zoom Out.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int ZoomOut(int id);

    /**
     * Control the camera Zoom Stop.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int ZoomStop(int id);


    /**
     * Change the device UserName and Password.
     *
     * @param OldName     the old userName.
     * @param NewName     the new userName.
     * @param OldPassword the old Password.
     * @param NewPassWord the new Password.
     * @param id          the device channel id.
     * @return
     */
    public static native int ChangeUserInfo(String OldName, String NewName, String OldPassword, String NewPassWord, int id);


    /**
     * Device FactoryReset.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int FactoryReset(int id);

    /**
     * Device Reboot.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int Reboot(int id);


    /**
     * Set IRMode . H264 have auto,manual,on and off ,MJ have auto and off.
     *
     * @param type the command type.{auto: 0 , manual: 1 ,on: 2 , off: 3}.
     * @param id   the device channel id.
     * @return
     */
    public static native int SetIRMode(int type, int id);

    /**
     * Request the MotionDetectConfig.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestMotionDetectConfig(int id);

    /**
     * After request the MotionDetectConfig,Receive the FS_API_STATUS_GET_MOTION_DETECT_CONFIG_SUCCESS callBack,
     * then to get MotionDetectConfig.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetMotionDetectConfig(int id);


    /**
     * Set PresetPoint . MJ use point ,H264 use name.
     *
     * @param point MJ use this function,point is 1-8,default is 0.
     * @param name  H264 use this function, name is the PresetPoint name.
     * @param id    the device channel id.
     * @return
     */
    public static native int SetPresetPoint(int point, String name, int id);


    /**
     * Goto PresetPoint . MJ use point ,H264 use name.
     *
     * @param point MJ use this function,point is 1-8,default is 0.
     * @param name  H264 use this function, name is the PresetPoint name.
     * @param id    the device channel id.
     * @return
     */
    public static native int GoPresetPoint(int point, String name, int id);

    /**
     * RefreshWifiList . First must call this, after receive FS_API_STATUS_REFRESH_WIFI_LIST_SUCCESS to RequestWifiList.
     * This maybe need 20-30 seconds.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RefreshWifiList(int id);

    /**
     * RequestWifiList .
     *
     * @param offset The start number of the list you want to get.
     *               For example:The are 30 aps around you, you want to look at the
     *               last ten aps, then offset=20
     * @param id     the device channel id.
     * @return
     */
    public static native int RequestWifiList(int offset, int id);

    /**
     * Get the WifiList. Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetWifiList(int id);

    /**
     * Set the WirelessSetting.
     */
    public static native int SetWirelessSetting(String SSID, int Encrypt, String Password,
                                                int AuthType, int KeyFormat, int DefaultKey, String Key1, String Key2,
                                                String Key3, String Key4, int Key1Len, int Key2Len, int Key3Len, int Key4Len, int id);
    
    
    
    
    /*
     * only use in MJ  
     * 
     */

    /**
     * Request current user permission.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestUserPermissionMJ(int id);

    /**
     * Get current user permission. Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetUserPermissionMJ(int id);


    /**
     * Request Device Info.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestDevState(int id);

    /**
     * Get Device Info. Called after receive request success callBack.
     * Contain IRState , PresetPoint Switcher State and PTZ State.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetDevState(int id);

    /**
     * Set MJ Cruise.
     *
     * @param cmd command type.vertical:26 , vertical stop:27 , horizontal: 28 , horizontal stop: 29.
     * @param id  the device channel id.
     * @return
     */
    public static native int SetCruiseMJ(int cmd, int id);

    /**
     * Set MJ MotionDetect.
     */
    public static native int SetMotionDetectMJ(int motion_armed, int motion_sensitivity,
                                               int motion_compensation, int input_armed, int ioin_level, int sounddetect_armed,
                                               int sounddetect_sensitivity, int iolinkage, int preset, int ioout_level,
                                               int mail, int upload_interval, int http, int schedule_enable,
                                               long schedule_sun_0, long schedule_sun_1, long schedule_sun_2,
                                               long schedule_mon_0, long schedule_mon_1, long schedule_mon_2,
                                               long schedule_tue_0, long schedule_tue_1, long schedule_tue_2,
                                               long schedule_wed_0, long schedule_wed_1, long schedule_wed_2,
                                               long schedule_thu_0, long schedule_thu_1, long schedule_thu_2,
                                               long schedule_fri_0, long schedule_fri_1, long schedule_fri_2,
                                               long schedule_sat_0, long schedule_sat_1, long schedule_sat_2, int id);


    /**
     * Set MJ PTZSettings
     */
    public static native int SetPTZSettingMJ(int ptz_center_onstart, int ptz_patrol_h_rounds,
                                             int ptz_patrol_v_rounds, int ptz_patrol_rate, int ptz_patrol_up_rate,
                                             int ptz_patrol_down_rate, int ptz_patrol_left_rate,
                                             int ptz_patrol_right_rate, int ptz_disable_preset, int id);
    
    
    /*
     * 
     */
    
    /*
     * only use h264
     */

    /**
     * Get current user permission.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int GetUserPermissionH264(int id);

    /**
     * Request Device Info.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestDevInfo(int id);

    /**
     * Get Device Info.Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetDevInfo(int id);

    /**
     * Set H264 MotionDetect .
     */
    public static native int SetMotionDetectH264(String isEnable, String linkage, String snapInterval,
                                                 String sensitivity, String triggerInterval, String[] schedule, String[] area, int id);


    /**
     * Request User List.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestUserList(int id);

    /**
     * Get User List.Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetUserList(int id);


    /**
     * Request PresetPointList.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestPresetPointList(int id);

    /**
     * Get PresetPointList. Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetPresetPointList(int id);

    /**
     * Delete Preset Point.
     *
     * @param name the name of PresetPoint.
     * @param id   the device channel id.
     * @return
     */
    public static native int DeletePresetPoint(String name, int id);

    /**
     * Request PTZ Speed Info.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestPTZSpeed(int id);

    /**
     * Get PTZ Speed Info.Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetPTZSpeed(int id);

    /**
     * Set PTZ Speed.
     *
     * @param mode the PTZ speed mode.
     * @param id   the device channel id.
     * @return
     */
    public static native int SetPTZSpeed(int mode, int id);

    /**
     * Request the PTZ self test mode.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestPTZSelfTest(int id);

    /**
     * Get the PTZ self test mode.Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetPTZSelfTest(int id);

    /**
     * Set the PTZ self test mode.
     *
     * @param mode the mode: 0(no self test) , 1(normal self test) , 2(after normal self test,then goto preset point)
     * @param id   the device channel id.
     * @return
     */
    public static native int SetPTZSelfTest(int mode, int id);

    /**
     * Request the PTZ preset point for self test .
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestPTZSelfTestPreset(int id);

    /**
     * Get the PTZ preset point for self test.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetPTZSelfTestPreset(int id);

    /**
     * Set the PTZ preset point for self test.
     *
     * @param preset the preset point name.
     * @param id     the device channel id.
     * @return
     */
    public static native int SetPTZSelfTestPreset(String preset, int id);

    /**
     * Request PTZ Cruise Map List.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int RequestPTZCruiseMapList(int id);

    /**
     * Get PTZ Cruise Map List.Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetPTZCruiseMapList(int id);

    /**
     * Request PTZ Cruise Map Info.
     *
     * @param name the Cruise Map name.
     * @param id   the device channel id.
     * @return
     */
    public static native int RequestPTZCruiseMapInfo(String name, int id);

    /**
     * Get PTZ Cruise Map Info.Called after receive request success callBack.
     *
     * @param id the device channel id.
     * @return
     */
    public static native String GetPTZCruiseMapInfo(int id);

    /**
     * Delete PTZ Cruise Map.
     *
     * @param name the Cruise Map name.
     * @param id   the device channel id.
     * @return
     */
    public static native int DeletePTZCruiseMap(String name, int id);

    /**
     * Set PTZ Cruise Map .
     *
     * @param name Cruise Map name.
     * @param n1   PresetPoint1 name.
     * @param n2   PresetPoint2 name.
     * @param n3   PresetPoint3 name.
     * @param n4   PresetPoint4 name.
     * @param n5   PresetPoint5 name.
     * @param n6   PresetPoint6 name.
     * @param n7   PresetPoint7 name.
     * @param n8   PresetPoint8 name.
     * @param id   the device channel id.
     * @return
     */
    public static native int SetPTZCruiseMap(String name, String n1, String n2, String n3, String n4,
                                             String n5, String n6, String n7, String n8, int id);

    /**
     * Start PTZ Cruise.
     *
     * @param name the Cruise Map name.
     * @param id   the device channel id.
     * @return
     */
    public static native int SetCruiseStart(String name, int id);

    /**
     * Stop PTZ Cruise.
     *
     * @param id the device channel id.
     * @return
     */
    public static native int SetCruiseStop(int id);


    static {
        try {
            System.loadLibrary("IOTCAPIs");
        } catch (UnsatisfiedLinkError ule) {
        }
        try {
            System.loadLibrary("RDTAPIs");
        } catch (UnsatisfiedLinkError ule) {
        }
        try {
            System.loadLibrary("iconv");
            System.loadLibrary("FSApi");
        } catch (UnsatisfiedLinkError ule) {
        }
    }

}

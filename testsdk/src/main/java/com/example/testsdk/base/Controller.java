package com.example.testsdk.base;

import android.content.Context;
import android.content.ServiceConnection;

import com.example.testsdk.callback.RemoteSDKStatusCallBack;

public interface Controller extends ServiceConnection {
    /**
     * init sdk
     *
     * @param context context
     * @return result
     */
    int init(Context context);

    /**
     * setStateCallback  设置状态回调
     *
     * @param remoteSDKStatusCallBack
     */

    void setStateCallback(RemoteSDKStatusCallBack remoteSDKStatusCallBack);


    void setMessage(String msg);

    /***
     * release sdk
     */
    void release();
}

// IMyRemoteCtrl.aidl
package com.example.testsdk;
import com.example.testsdk.IMySDKStatusCallBack;

// Declare any non-default types here with import statements

interface IMyRemoteCtrl {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void sendMessage(String msg);
     void linkToDeath(IBinder binder);
     void unlinkToDeath(IBinder binder);
     void registerMySDKStatusCallBack(IMySDKStatusCallBack mySDKStatusCallBack);
     void unregisterMySDKStatusCallBack(IMySDKStatusCallBack mySDKStatusCallBack);
}
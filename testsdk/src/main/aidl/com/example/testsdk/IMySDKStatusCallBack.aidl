// IMyRemoteCtrl.aidl
package com.example.testsdk;

// Declare any non-default types here with import statements

interface IMySDKStatusCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      void sdkStatusCallBackVisible();

      void sdkStatusCallBackInvisible();

      void sdkSendMessage(String message);
}
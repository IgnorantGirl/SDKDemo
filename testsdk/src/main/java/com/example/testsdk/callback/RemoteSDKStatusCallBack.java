package com.example.testsdk.callback;

public interface RemoteSDKStatusCallBack {
    /**
     * 状态回调可见
     */
    void statusCallBackVisible();

    /**
     * 状态回调不可见接口
     */
    void statusCallBackInvisible();

    /**
     * 发送消息
     * @param message 消息
     */
    void sendMessage(String message);
}

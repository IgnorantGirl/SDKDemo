package com.example.serviceapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.testsdk.IMyRemoteCtrl;
import com.example.testsdk.IMySDKStatusCallBack;


public class MySDKService extends Service {

    private MyRemoteCtrlImpl myRemoteCtrl = new MyRemoteCtrlImpl();
    private IMySDKStatusCallBack mMySDKStatusCallBack = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //返回内部代理对象给调用端（客户端）
        return myRemoteCtrl.asBinder();
    }

    public class MyRemoteCtrlImpl extends IMyRemoteCtrl.Stub {
        private IBinder mBinder = null;
        private Object deathRecipient;
        @Override
        public void sendMessage(String msg) throws RemoteException {
            if (mMySDKStatusCallBack != null) {
                mMySDKStatusCallBack.sdkSendMessage(" 我是服务端你发送的消息是: =  " + msg);
            }
        }

        @Override
        public void linkToDeath(IBinder binder) throws RemoteException {
            Log.d("mysdk"," 服务端 建立死亡链接  ");
            mBinder = binder;
            binder.linkToDeath(mDeathRecipient, 0);
        }

        @Override
        public void unlinkToDeath(IBinder binder) throws RemoteException {
            Log.d("mysdk"," 服务端 断开客户端死亡链接  ");
            binder.unlinkToDeath(mDeathRecipient, 0);
            mBinder = null;
        }

        @Override
        public void registerMySDKStatusCallBack(IMySDKStatusCallBack mySDKStatusCallBack) throws RemoteException {
            Log.d("mysdk"," 服务端 接收到 registerMySDKStatusCallBack ");
            mMySDKStatusCallBack = mySDKStatusCallBack;
            // mMySDKStatusCallBack 为第三方通过SDK传递过来的 对象 调用 mySDKStatusCallBack.statusCallBackInvisible()
            // 相当于持有  MySDKStatusCallBack mMySDKStatusCallBack

            //这里不做操作直接返回即可
            if (mMySDKStatusCallBack != null) {
                mySDKStatusCallBack.sdkStatusCallBackVisible();
            }
        }

        @Override
        public void unregisterMySDKStatusCallBack(IMySDKStatusCallBack mySDKStatusCallBack) throws RemoteException {
            Log.d("mysdk"," 服务端 接收到 unregisterMySDKStatusCallBack ");
            mMySDKStatusCallBack = null;
        }
    }

    IBinder.DeathRecipient mDeathRecipient =  new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
              /*  if (mMySDKStatusCallBack != null) {
                    try {
                        mMySDKStatusCallBack.statusCallBackInvisible();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }*/

            //客户端可以执行释放操作
            Log.d("mysdk","服务端打印 client_pp端已经死亡");
        }

    };
}

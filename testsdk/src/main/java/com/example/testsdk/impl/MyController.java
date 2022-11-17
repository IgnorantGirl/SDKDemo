package com.example.testsdk.impl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.testsdk.IMyRemoteCtrl;
import com.example.testsdk.IMySDKStatusCallBack;
import com.example.testsdk.base.Controller;
import com.example.testsdk.callback.RemoteSDKStatusCallBack;


public class MyController implements Controller {

    private IMyRemoteCtrl mMyRemoteCtrl;
    private RemoteSDKStatusCallBack mRemoteSDKStatusCallBack;
    private volatile static Controller mInstance;
    private boolean isBind;
    protected Context mContext;

    //死亡链接
    private IMySDKStatusCallBack mMySDKStatusCallBack = new IMySDKStatusCallBack.Stub() {

        @Override
        public void sdkStatusCallBackVisible() throws RemoteException {
            mRemoteSDKStatusCallBack.statusCallBackVisible();
            Log.d("mysdk", " sdklib  已调用 sdkStatusCallBackVisible ");

        }

        @Override
        public void sdkStatusCallBackInvisible() throws RemoteException {
            mRemoteSDKStatusCallBack.statusCallBackInvisible();
        }

        @Override
        public void sdkSendMessage(String message) throws RemoteException {
            mRemoteSDKStatusCallBack.sendMessage(message);
        }
    };

    private MyController() {
    }

    @Override
    public int init(Context context) {
        //初始化服务
        Log.d("mysdk", " sdk  初始化服务  ");
        mContext = context;
        return initService();
    }

    private int initService() {
        Log.d("mysdk", " sdk  initService  ");
        Intent intent = new Intent("com.example.serviceapp.service.MySDKService");
        intent.setClassName("com.example.serviceapp", "com.example.serviceapp.service.MySDKService");
        isBind = mContext.bindService(intent, this, Service.BIND_AUTO_CREATE);
        Log.d("mysdk", " isBind:  " + isBind);

        return 0;
    }

    @Override
    public void setStateCallback(RemoteSDKStatusCallBack remoteSDKStatusCallBack) {
        Log.d("mysdk", " sdk  setStateCallback  ");
        this.mRemoteSDKStatusCallBack = remoteSDKStatusCallBack;
    }

    @Override
    public void setMessage(String msg) {
        try {
            Log.d("mysdk", " sdk  setMessage  ");
            mMyRemoteCtrl.sendMessage(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        Log.d("mysdk", " sdk  release  ");
        //SDK内部做释放操作
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Log.d("mysdk", " sdk  onServiceConnected  ");
        if (service == null) {
            if (mMyRemoteCtrl != null) {
                try {
                    mMyRemoteCtrl.unlinkToDeath(mMySDKStatusCallBack.asBinder());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mMyRemoteCtrl = null;
        } else {
            mMyRemoteCtrl = IMyRemoteCtrl.Stub.asInterface(service);
            if (mMyRemoteCtrl != null) {
                try {
                    mMyRemoteCtrl.linkToDeath(mMySDKStatusCallBack.asBinder());
                   // mMyRemoteCtrl.registerMySDKStatusCallBack(mMySDKStatusCallBack);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                try {
                    mMyRemoteCtrl.registerMySDKStatusCallBack(mMySDKStatusCallBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d("mysdk", " sdk  onServiceDisconnected  ");
        if (mMyRemoteCtrl != null) {
            try {
                mMyRemoteCtrl.unregisterMySDKStatusCallBack(mMySDKStatusCallBack);
                mMyRemoteCtrl.unlinkToDeath(mMySDKStatusCallBack.asBinder());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mMyRemoteCtrl = null;
    }

    public static Controller getInstance() {
        if (mInstance == null) {
            synchronized (MyController.class) {
                if (mInstance == null) {
                    mInstance = new MyController();
                }
            }
        }
        return mInstance;
    }
}

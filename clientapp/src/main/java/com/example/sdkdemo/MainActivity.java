package com.example.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.testsdk.MyLibSDK;
import com.example.testsdk.callback.RemoteSDKStatusCallBack;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化SDK
        initMySDK();
    }

    private void initMySDK() {
        //链接SDK
        MyLibSDK.getInstance().init(this);
        //注册服务端的CallBack
        MyLibSDK.getInstance().setStateCallback(new RemoteSDKStatusCallBack() {


            @Override
            public void statusCallBackVisible() {
                Log.d("mysdk", " clienapp 已经注册 RemoteSDKStatusCallBack 成功 ");
            }

            @Override
            public void statusCallBackInvisible() {
                Log.d("mysdk", " clienapp ~~注销~~ RemoteSDKStatusCallBack 成功 ");
            }

            @Override
            public void sendMessage(String s) {
                Log.d("mysdk", "发给服务端的消息返回给 clienapp 成功 " + s);
            }
        });
    }

    public void sedmessage(View view) {
        Log.d("mysdk", "clienapp  sendMessage 我是客户端  ");
        MyLibSDK.getInstance().setMessage("我是客户端");
    }
}
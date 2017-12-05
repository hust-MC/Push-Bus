package com.imooc.push;

import android.app.Application;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import cn.jpush.android.api.JPushInterface;

public class ImoocApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                Log.d("MC", "Umeng Log in ");
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.setPushIntentServiceClass(UMessgeService.class);
    }
}

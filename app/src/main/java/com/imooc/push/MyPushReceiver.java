package com.imooc.push;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.JPushMessageReceiver;


public class MyPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED, intent.getAction())) {
            String content = intent.getExtras().getString(JPushInterface.EXTRA_ALERT);
            Message m = new Message();
            m.setContent(content);
            EventBus.getDefault().post(m);
        }
    }

    public static class JPushReceiver extends JPushMessageReceiver {
    }
}


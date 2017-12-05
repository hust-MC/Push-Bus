package com.imooc.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class UMessgeService extends UmengMessageService {

    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        try {
            UMessage msg = new UMessage(new JSONObject(message));
            final Message content = new Message();
            content.setContent(msg.text);

            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    MessageBus.getInstance().post(content);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

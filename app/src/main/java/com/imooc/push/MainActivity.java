package com.imooc.push;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.InstrumentedActivity;

import static com.imooc.push.MessageDbHelper.CONTENT;
import static com.imooc.push.MessageDbHelper.TableName.JPush;
import static com.imooc.push.MessageDbHelper.TableName.UPush;

public class MainActivity extends InstrumentedActivity {

    ListView mUPush;
    ListView mJPush;
    SimpleAdapter mJAdapter, mUAdapter;
    List<Map<String, String>> mJList = new ArrayList<>();
    List<Map<String, String>> mUList = new ArrayList<>();

    MessageDbHelper mDBHelper = new MessageDbHelper(this, "push", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUPush = findViewById(R.id.upush);
        mJPush = findViewById(R.id.jpush);

        mJAdapter = new SimpleAdapter(this, getData(JPush), R.layout.list,
                new String[]{CONTENT}, new int[]{R.id.list_title});
        mUAdapter = new SimpleAdapter(this, getData(UPush), R.layout.list,
                new String[]{CONTENT}, new int[]{R.id.list_title});
        mJPush.setAdapter(mJAdapter);
        mUPush.setAdapter(mUAdapter);

        OnBtClick listener = new OnBtClick();
        findViewById(R.id.clear_jpush).setOnClickListener(listener);
        findViewById(R.id.clear_upush).setOnClickListener(listener);

        EventBus.getDefault().register(this);
        MessageBus.getInstance().register(this);
        mDBHelper.getWritableDatabase();
    }

    private List<Map<String, String>> getData(MessageDbHelper.TableName table) {
        if (table.equals(JPush)) {
            mJList = mDBHelper.query(JPush);
            return mJList;
        } else {
            mUList = mDBHelper.query(UPush);
            return mUList;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageBus.getInstance().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(Message event) {
        String content = event.getContent();
        Log.d("MC", content);
        mDBHelper.insert(JPush, content);
        Map<String, String> map = new HashMap<>();
        map.put(CONTENT, content);
        mJList.add(map);
        mJAdapter.notifyDataSetChanged();
    }

    @com.squareup.otto.Subscribe
    public void onReceiveOTTO(Message msg) {
        String content = msg.getContent();
        Log.d("MC", "OTTO : " + content);

        mDBHelper.insert(UPush, content);
        Map<String, String> map = new HashMap<>();
        map.put(CONTENT, content);
        mUList.add(map);
        mUAdapter.notifyDataSetChanged();

    }

    private class OnBtClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.clear_jpush) {
                mDBHelper.clear(JPush);
                mJList.removeAll(mJList);
                mJAdapter.notifyDataSetChanged();
            } else if (v.getId() == R.id.clear_upush) {
                mDBHelper.clear(UPush);
                mUList.removeAll(mUList);
                mUAdapter.notifyDataSetChanged();
            }
        }
    }
}

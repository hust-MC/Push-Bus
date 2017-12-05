package com.imooc.push;

import com.squareup.otto.Bus;

public class MessageBus extends Bus {
    private static MessageBus mBus;

    public static MessageBus getInstance() {
        if (mBus == null) {
            synchronized (MessageBus.class) {
                if (mBus == null) {
                    mBus = new MessageBus();
                }
            }
        }
        return mBus;
    }
}

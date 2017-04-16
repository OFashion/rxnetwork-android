package com.laimiux.rxnetwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

class OnSubscribeBroadcastRegister implements FlowableOnSubscribe<Intent> {


    private final Context context;
    private final IntentFilter intentFilter;
    private final String broadcastPermission;
    private final Handler schedulerHandler;
    private FlowableEmitter<Intent> flowableEmitter;

    OnSubscribeBroadcastRegister(Context context, IntentFilter intentFilter, String broadcastPermission, Handler schedulerHandler) {
        this.context = context;
        this.intentFilter = intentFilter;
        this.broadcastPermission = broadcastPermission;
        this.schedulerHandler = schedulerHandler;
    }


    @Override
    public void subscribe(FlowableEmitter<Intent> emitter) throws Exception {
        this.flowableEmitter = emitter;

        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (flowableEmitter != null) {
                    flowableEmitter.onNext(intent);
                }
            }
        };
        context.registerReceiver(broadcastReceiver, intentFilter, broadcastPermission, schedulerHandler);
        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                context.unregisterReceiver(broadcastReceiver);
                flowableEmitter = null;

            }
        });

    }


}

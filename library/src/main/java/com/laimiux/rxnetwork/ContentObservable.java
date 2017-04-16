package com.laimiux.rxnetwork;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;


final class ContentObservable {
    private ContentObservable() {
        throw new AssertionError("No instances");
    }

    static Flowable<Intent> fromBroadcast(Context context, IntentFilter filter) {

        return Flowable.create(new OnSubscribeBroadcastRegister(context, filter, null, null), BackpressureStrategy.LATEST);

    }
}
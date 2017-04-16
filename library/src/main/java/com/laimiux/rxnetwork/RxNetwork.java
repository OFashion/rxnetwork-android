package com.laimiux.rxnetwork;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class RxNetwork {
    private RxNetwork() {
    }

    private static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return null != activeNetwork && activeNetwork.isConnected();

    }

    public static Flowable<Boolean> stream(Context context) {
        final Context applicationContext = context.getApplicationContext();
        final IntentFilter action = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        return ContentObservable.fromBroadcast(context, action)
                .startWith(new Intent())
                .map(new Function<Intent, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull Intent intent) throws Exception {
                        return getConnectivityStatus(applicationContext);
                    }
                }).distinctUntilChanged();
    }
}

package com.laimiux.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.laimiux.rxnetwork.RxNetwork;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SampleActivity extends Activity {
    Button sendButton;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_view);


        sendButton = (Button) findViewById(R.id.send_button);

        final Flowable<ButtonState> sendStateStream =
                RxNetwork.stream(this).map(new Function<Boolean, ButtonState>() {
                    @Override
                    public ButtonState apply(@NonNull Boolean hasInternet) throws Exception {
                        if (!hasInternet) {
                            return new ButtonState(R.string.not_connected, false);
                        }

                        return new ButtonState(R.string.send, true);
                    }


                });

        disposable =
                sendStateStream.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ButtonState>() {
                            @Override
                            public void accept(@NonNull ButtonState buttonState) throws Exception {
                                sendButton.setText(buttonState.textId);
                                sendButton.setEnabled(buttonState.isEnabled);
                            }
                        });
    }


    @Override
    protected void onDestroy() {
        disposable.dispose();
        disposable = null;

        super.onDestroy();
    }

    private static class ButtonState {
        final int textId;
        final boolean isEnabled;

        ButtonState(int textId, boolean isEnabled) {
            this.textId = textId;
            this.isEnabled = isEnabled;
        }
    }
}

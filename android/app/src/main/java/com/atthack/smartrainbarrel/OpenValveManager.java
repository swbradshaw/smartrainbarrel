package com.atthack.smartrainbarrel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.client.protocol.RequestDefaultHeaders;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by scott on 4/16/2016.
 */
public class OpenValveManager extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        final PendingResult result = goAsync();
        final PowerManager.WakeLock wl = createPartialWakeLock(context);
        wl.acquire();
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                handleIntent(context, intent);
                result.finish();
                wl.release();
            }
        });
    }

    public static PowerManager.WakeLock createPartialWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LogUtils.LOGTAG);
    }

    private void handleIntent(Context context, Intent intent) {
        final String action = intent.getAction();
        LogUtils.v("received intent " + intent);
        if ("openvalve".equals(action)) {
            LogUtils.d("Send flag to M2X");
            Helper.sendM2Xrequest(context);
        }
    }


}

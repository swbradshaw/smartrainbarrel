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
            sendM2Xrequest(context);
        }
    }

    private void sendM2Xrequest(Context context) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        // https://github.com/AsyncHttpClient/async-http-client


        String bodyAsJson = "{ \"value\" : 1}";

        ByteArrayEntity entity = null;
        try {
            entity =new ByteArrayEntity(bodyAsJson.getBytes("UTF-8"));
        } catch (Exception e) {}

        client.addHeader("X-M2X-KEY", "a26469fcf6bae973bab9e01766e6f0b7");
        client.post(context, "http://api-m2x.att.com/v2/devices/c18123714d4993d81cf0da9b0f1d2446/streams/Valve/value", entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                LogUtils.d("Sent trigger");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                LogUtils.d("error in trigger");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}

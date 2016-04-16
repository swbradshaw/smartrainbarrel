package com.atthack.smartrainbarrel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean dataChanged  = false;
        int weight = 0;
        String sWeight = data.getString("weight");
        if (sWeight != null) {
            // weight provided
            weight = Integer.parseInt(sWeight);
            sharedPreferences.edit().putInt(QuickstartPreferences.WEIGHT, weight).apply();
            dataChanged= true;

        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        if (weight > 95) {
            sendNotification();
        }
        if (dataChanged) {
            Intent i = new Intent(Constants.SENSOR_DATA);
            //i.setAction(Constants.WEIGHT);
            i.putExtra("weight", sWeight);
            sendBroadcast(i);
           // LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Intent openValveIntent = new Intent(this, OpenValveManager.class);
        openValveIntent.setAction("openvalve");
        Context context = this.getApplicationContext();

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender()
                .addAction(new NotificationCompat.Action(R.drawable.faucet_white,
                        context.getString(R.string.open_valve),
                        PendingIntent.getBroadcast(context, 0,
                                openValveIntent, PendingIntent.FLAG_UPDATE_CURRENT)));

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.faucet_white)
                .setContentTitle("Rain barrel full")
                .setContentText("The barrel has reached full capacity.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        notificationBuilder.addAction(R.drawable.faucet_black,
                context.getString(R.string.open_valve),
                PendingIntent.getBroadcast(context,0,
                        openValveIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0 /* ID of notification */, notification);
    }
}
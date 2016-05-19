package fr.neamar.lolgamedata.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import fr.neamar.lolgamedata.GameActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Account;

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
        String gameMode = data.getString("gameMode");
        String summonerName = data.getString("summonerName");
        String region = data.getString("region");
        int mapId = Integer.parseInt(data.getString("mapId"));

        Account account = new Account(summonerName,region, "");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Game mode: " + gameMode);

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
        displayNotification(account, mapId, gameMode);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     */
    private void displayNotification(Account account, int mapId, String gameMode) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("account", account);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_transparent))
                .setContentTitle(String.format("Welcome to %s!", getString(GameActivity.getMapName(mapId))))
                .setContentText(String.format("%s is in game. Touch for information", account.summonerName))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
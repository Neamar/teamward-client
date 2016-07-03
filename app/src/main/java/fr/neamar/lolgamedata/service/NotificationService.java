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
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import fr.neamar.lolgamedata.GameActivity;
import fr.neamar.lolgamedata.LolApplication;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Account;

public class NotificationService extends GcmListenerService {

    private static final String TAG = "NotificationService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        if(data.containsKey("gameId")) {
            int gameId = data.getInt("gameId");
            String gameMode = data.getString("gameMode");
            String summonerName = data.getString("summonerName");
            String region = data.getString("region");
            int mapId = Integer.parseInt(data.getString("mapId"));

            Account account = new Account(summonerName, region, "");

            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Game mode: " + gameMode);

            displayNotification(account, gameId, mapId);
        }

    }

    /**
     * Create and show a simple notification containing the received GCM message.
     */
    private void displayNotification(Account account, int gameId, int mapId) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("source", "notification");

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

        notificationManager.notify(gameId, notificationBuilder.build());

        MixpanelAPI.getInstance(this, LolApplication.MIXPANEL_TOKEN).track("Notification displayed", account.toJsonObject());
    }
}
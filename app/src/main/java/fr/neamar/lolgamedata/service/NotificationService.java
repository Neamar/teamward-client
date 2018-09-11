package fr.neamar.lolgamedata.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import fr.neamar.lolgamedata.GameActivity;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.Tracker;
import fr.neamar.lolgamedata.pojo.Account;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";
    private static final String CHANNEL_ID = "gameNotification";

    /**
     * Called when message is received.
     */
    @Override
    public void onMessageReceived(RemoteMessage message) {
        Map<String, String> data = message.getData();
        Log.i(TAG, "Received notification from " + message.getFrom());
        if (data.containsKey("gameId")) {
            long gameId = Long.parseLong(data.get("gameId"));
            String summonerName = data.get("summonerName");
            String region = data.get("region");
            int mapId = Integer.parseInt(data.get("mapId"));

            Account account = new Account(summonerName, region, "");
            displayNotification(account, gameId, mapId);
        } else if (data.containsKey("removeGameId")) {
            long gameId = Long.parseLong(data.get("removeGameId"));

            Log.i(TAG, "End of game, hiding notification.");
            getNotificationManager().cancel(Long.toString(gameId).hashCode());
        } else if (data.containsKey("mp_message")) {
            String title = data.containsKey("mp_title") ? data.get("mp_title") : getString(R.string.app_name);
            String content = data.get("mp_message");
            String url = data.containsKey("mp_url") ? data.get("mp_url") : "";
            displayCustomNotification(title, content, url);
        } else {
            Log.i(TAG, "Received unknown notification:" + data.toString());
        }
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     */
    private void displayNotification(Account account, long gameId, int mapId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("source", "notification");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_transparent_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_transparent))
                .setContentTitle(String.format(getString(R.string.welcome_to), getString(GameActivity.getMapName(mapId))))
                .setContentText(String.format(getString(R.string.player_is_in_game), account.summonerName))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        if (prefs.getBoolean("notifications_new_game_vibrate", true)) {
            notificationBuilder.setVibrate(new long[]{1000, 1000});
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            Uri notificationUri = Uri.parse(prefs.getString("notifications_new_game_ringtone", Settings.System.DEFAULT_NOTIFICATION_URI.toString()));
            notificationBuilder.setSound(notificationUri);
        }

        NotificationManager notificationManager = getNotificationManager();
        boolean unableToDisplay = false;
        if (prefs.getBoolean("notifications_new_game", true)) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                            getString(R.string.channel_in_game_notifications),
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                if (prefs.getLong("last_viewed_game", -1) == gameId) {
                    Log.i(TAG, "Skipping notification display, game is already on screen.");
                }
                else {
                    notificationManager.notify(Long.toString(gameId).hashCode(), notificationBuilder.build());
                }

            } catch (RuntimeException e) {
                // Most likely, the ringtone doesn't exist anymore.
                // Used to happen only in Android 6.0, so the notification part is skipped on API M.
                // I'm keeping the try/catch just in case ;)
                unableToDisplay = true;
            }

            Tracker.trackNotificationDisplayed(this, account, mapId, getString(GameActivity.getMapName(mapId)), gameId, unableToDisplay);
        }
    }

    private void displayCustomNotification(String title, String message, String url) {
        Intent intent;

        if (url == null || url.isEmpty()) {
            intent = new Intent(this, GameActivity.class);
            intent.putExtra("source", "custom_notification");
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_transparent_white)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_transparent))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        getNotificationManager().notify(message.hashCode(), notificationBuilder.build());
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
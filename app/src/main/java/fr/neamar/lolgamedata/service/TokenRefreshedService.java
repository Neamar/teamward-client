package fr.neamar.lolgamedata.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class TokenRefreshedService extends FirebaseInstanceIdService {
    private static final String TOKEN_UPDATE_REQUIRED = "tokenUpdateRequired";

    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        try {
            // Resync with server
            Intent intent = new Intent(this, SyncTokenService.class);
            this.startService(intent);
        }
        catch(IllegalStateException e) {
            // This will happen when the token changes remotely, but the app isn't in the foreground yet.
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(TOKEN_UPDATE_REQUIRED, true).apply();
        }
    }

    public static boolean tokenUpdateRequired(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.contains(TOKEN_UPDATE_REQUIRED)) {
            prefs.edit().remove(TOKEN_UPDATE_REQUIRED).apply();
            return true;
        }

        return false;
    }
}
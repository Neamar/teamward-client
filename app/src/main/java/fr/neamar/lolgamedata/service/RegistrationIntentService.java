package fr.neamar.lolgamedata.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.ArrayList;

import fr.neamar.lolgamedata.AccountManager;
import fr.neamar.lolgamedata.R;
import fr.neamar.lolgamedata.pojo.Account;

/**
 * Created by neamar on 16/05/16.
 */
public class RegistrationIntentService extends IntentService {
    private static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "onHandleIntent");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();

            AccountManager accountManager = new AccountManager(this);
            ArrayList<Account> accounts = accountManager.getAccounts();
            if(accounts.size() > 0) {
                sendTokenToServer(token, accounts.get(0));
            }
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().remove(SENT_TOKEN_TO_SERVER).apply();
        }
    }

    private void sendTokenToServer(String token, Account account) {
        Log.e("WTF", token);
    }
}
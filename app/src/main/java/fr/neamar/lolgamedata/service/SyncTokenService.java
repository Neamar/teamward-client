package fr.neamar.lolgamedata.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import fr.neamar.lolgamedata.AccountManager;
import fr.neamar.lolgamedata.LolApplication;
import fr.neamar.lolgamedata.network.VolleyQueue;
import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.volley.NoCacheRetryJsonRequest;

public class SyncTokenService extends IntentService {
    private static final String TAG = "SyncTokenService";

    public SyncTokenService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AccountManager accountManager = new AccountManager(this);
        ArrayList<Account> accounts = accountManager.getAccounts();
        if (accounts.isEmpty()) {
            Log.i(TAG, "No account yet, skipping token registration");
            return;
        }
        Account account = accounts.get(0);

        String token = FirebaseInstanceId.getInstance().getToken();

        if(token == null || token.isEmpty()) {
            Log.i(TAG, "Firebase token not ready yet, skipping token registration");
            return;
        }

        // We have both a token and an account, send that to the server
        sendTokenToServer(token, account);
    }

    private void sendTokenToServer(final String token, final Account account) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = VolleyQueue.newRequestQueue(this);
        String url;
        try {
            url = ((LolApplication) getApplication()).getApiUrl() + "/push?token=" + token + "&summoner=" + URLEncoder.encode(account.summonerName, "UTF-8") + "&region=" + account.region;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        NoCacheRetryJsonRequest jsonRequest = new NoCacheRetryJsonRequest(Request.Method.GET, url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Token registered with server for user " + account.summonerName);
                        queue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.i(TAG, responseBody);
                } catch (UnsupportedEncodingException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        queue.add(jsonRequest);
    }
}
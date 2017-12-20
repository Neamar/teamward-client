package fr.neamar.lolgamedata;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import fr.neamar.lolgamedata.network.VolleyQueue;
import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.volley.NoCacheRetryJsonRequest;

public class LolApplication extends Application {
    private static final String TAG = "LolApplication";

    private MixpanelAPI mixpanel = null;
    private AmplitudeClient amplitude = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheFileCount(300)
                .threadPoolSize(5)
                .memoryCache(new WeakMemoryCache())
                .build();


        ImageLoader.getInstance().init(config);

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        // Tracking initialization
        final Runnable r = new Runnable() {
            public void run() {
                identifyOnAmplitude();
                identifyOnTrackers();
            }
        };

        Handler handler = new Handler();
        handler.post(r);
    }

    public MixpanelAPI getMixpanel() {
        if (mixpanel == null) {
            mixpanel = MixpanelAPI.getInstance(this, getString(R.string.MIXPANEL_TOKEN));
            mixpanel.getPeople().identify(mixpanel.getDistinctId());
        }

        return mixpanel;
    }

    public AmplitudeClient getAmplitude() {
        if (amplitude == null) {
            Amplitude.getInstance().initialize(this, getString(R.string.AMPLITUDE_TOKEN)).enableForegroundTracking(this);
            amplitude = Amplitude.getInstance();
        }

        return amplitude;
    }

    public JSONArray getJSONArrayFromSingleItem(String item) {
        JSONArray a = new JSONArray();
        a.put(item);
        return a;
    }

    public void identifyOnTrackers() {
        AccountManager accountManager = new AccountManager(getApplicationContext());
        List<Account> accounts = accountManager.getAccounts();
        Log.i(TAG, "Current size for accounts is " + accounts.size());

        if (!accountManager.getAccounts().isEmpty()) {
            Log.i(TAG, "Identifying as " + accounts.get(0).summonerName);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Tracker.trackUserProperties(getApplicationContext(), accounts.get(0), accounts.size(), sp);
        }
    }

    public void identifyOnAmplitude() {
    }

    public String getApiUrl() {
        return getString(R.string.API_URL);
    }

    /*
        Synchronizes Firebase token with the main user account
     */
    public void syncTokenToServer() {
        AccountManager accountManager = new AccountManager(this);
        ArrayList<Account> accounts = accountManager.getAccounts();
        if (accounts.isEmpty()) {
            Log.i(TAG, "No account yet, skipping token registration");
            return;
        }
        final Account account = accounts.get(0);

        final String token = FirebaseInstanceId.getInstance().getToken();

        if(token == null || token.isEmpty()) {
            Log.i(TAG, "Firebase token not ready yet, skipping token registration");
            return;
        }

        // We have both a token and an account, send that to the server
        // Instantiate the RequestQueue.
        final RequestQueue queue = VolleyQueue.newRequestQueue(this);
        String url;
        try {
            url = getApiUrl() + "/push?token=" + token + "&summoner=" + URLEncoder.encode(account.summonerName, "UTF-8") + "&region=" + account.region;
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
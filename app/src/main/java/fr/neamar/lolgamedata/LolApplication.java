package fr.neamar.lolgamedata;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;

import java.util.List;

import fr.neamar.lolgamedata.pojo.Account;
import fr.neamar.lolgamedata.service.RegistrationIntentService;

public class LolApplication extends Application {
    private static final String TAG = "LolApplication";

    private MixpanelAPI mixpanel = null;

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
                identifyOnMixpanel();
            }
        };

        Handler handler = new Handler();
        handler.post(r);

        // Register for push notifications, send token again in case it changed
        Intent intent = new Intent(this, RegistrationIntentService.class);
        Log.i(TAG, "Starting Service");
        startService(intent);
    }

    public MixpanelAPI getMixpanel() {
        if (mixpanel == null) {
            mixpanel = MixpanelAPI.getInstance(this, getString(R.string.MIXPANEL_TOKEN));
            mixpanel.getPeople().identify(mixpanel.getDistinctId());
        }

        return mixpanel;
    }

    public JSONArray getJSONArrayFromSingleItem(String item) {
        JSONArray a = new JSONArray();
        a.put(item);
        return a;
    }

    public void identifyOnMixpanel() {
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
        Amplitude.getInstance().initialize(this, getString(R.string.AMPLITUDE_TOKEN)).enableForegroundTracking(this);
    }

    public String getApiUrl() {
        return getString(R.string.API_URL);
    }
}
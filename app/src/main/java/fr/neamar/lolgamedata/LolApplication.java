package fr.neamar.lolgamedata;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

import fr.neamar.lolgamedata.pojo.Account;

public class LolApplication extends Application {
    private static final String TAG = "LolApplication";

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

        /*
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads() // Required for sync access to SharedPreferences.
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
        */

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

    public AmplitudeClient getAmplitude() {
        if (amplitude == null) {
            Amplitude.getInstance().initialize(this, getString(R.string.AMPLITUDE_TOKEN)).enableForegroundTracking(this);
            amplitude = Amplitude.getInstance();
        }

        return amplitude;
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
}
package fr.neamar.lolgamedata;

import android.app.Application;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by neamar on 29/03/16.
 */
public class LolApplication extends Application {
    public static final String API_URL = "https://teamward.herokuapp.com";
    public static final String MIXPANEL_TOKEN = "1a7075d95ff6db6d08714db52edb706a";

    private MixpanelAPI mixpanel = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();


        ImageLoader.getInstance().init(config);
    }

    public MixpanelAPI getMixpanel() {
        if (mixpanel == null) {
            mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);
            mixpanel.getPeople().identify(mixpanel.getDistinctId());
        }

        return mixpanel;

    }
}
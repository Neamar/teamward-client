package fr.neamar.lolgamedata;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import android.view.WindowManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.content.Context.KEYGUARD_SERVICE;

@LargeTest
@RunWith(AndroidJUnit4.class)
public abstract class FlowTest {

    @Rule
    public ActivityTestRule<GameActivity> mActivityTestRule = new ActivityTestRule<>(GameActivity.class, false, false);

    @UiThreadTest
    @Before
    public void setUp() throws Throwable {
        // Clean up all accounts
        InstrumentationRegistry.getTargetContext().getSharedPreferences("accounts", Context.MODE_PRIVATE).edit().clear().commit();

        mActivityTestRule.launchActivity(null);

        final Activity activity = mActivityTestRule.getActivity();
        mActivityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyguardManager mKG = (KeyguardManager) activity.getSystemService(KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock mLock = mKG.newKeyguardLock(KEYGUARD_SERVICE);
                mLock.disableKeyguard();

                //turn the screen on
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
            }
        });
    }

    void pause() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

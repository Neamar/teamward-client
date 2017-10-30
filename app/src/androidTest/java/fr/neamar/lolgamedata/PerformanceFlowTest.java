package fr.neamar.lolgamedata;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PerformanceFlowTest {

    @Rule
    public ActivityTestRule<GameActivity> mActivityTestRule = new ActivityTestRule<>(GameActivity.class, false, false);

    @Test
    public void performanceFlowTest() {
        // Clean up all accounts
        InstrumentationRegistry.getTargetContext().getSharedPreferences("accounts", Context.MODE_PRIVATE).edit().clear().commit();

        mActivityTestRule.launchActivity(null);

        final GameActivity activity = mActivityTestRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);

        // Create account
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.summonerText),
                        isDisplayed()));
        editText.perform(replaceText("MOCK"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.save), withText("Add account"),
                        isDisplayed()));
        button.perform(click());

        // Load current game (mocked)
        wait(1000);

        ViewInteraction championName = onView(
                allOf(withText("Illaoi"), isDisplayed()));
        championName.perform(click());

        wait(1000);

        ViewInteraction textView = onView(
                allOf(withId(R.id.championMasteryText),
                        isDisplayed()));
        textView.check(matches(withText("Champion mastery 7")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.recentMatchesTitle),
                        isDisplayed()));
        textView4.check(matches(withText("Recent matches with Illaoi")));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_champion_details),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        wait(1000);

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.abilityName),
                        withText("Q — Tentacle Smash"),
                        isDisplayed()));
        textView5.check(matches(withText("Q — Tentacle Smash")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private void wait(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

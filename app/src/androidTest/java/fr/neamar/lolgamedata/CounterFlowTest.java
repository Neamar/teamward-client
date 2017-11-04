package fr.neamar.lolgamedata;


import android.annotation.SuppressLint;
import android.support.test.espresso.ViewInteraction;
import android.view.WindowManager;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class CounterFlowTest extends FlowTest {
    @SuppressLint("CommitPrefEdits")
    @Test
    public void counterFlowTest() {
        final GameActivity activity = mActivityTestRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);

        // Create an account
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.coordinatorLayout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.summonerText), isDisplayed()));
        editText.perform(replaceText("MOCK"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.save), withText(R.string.add_account), isDisplayed()));
        button.perform(click());

        pause();

        // Open draft tools
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_counter), isDisplayed()));
        actionMenuItemView.perform(click());

        pause();

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.sectionTitle), withText("Your good Mid counters"), isDisplayed()));
        textView.check(matches(withText("Your good Mid counters")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.sectionTitle), withText("Your bad Mid counters")));
        textView2.check(matches(withText("Your bad Mid counters")));
    }
}

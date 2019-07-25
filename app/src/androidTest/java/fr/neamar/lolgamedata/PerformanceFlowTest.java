package fr.neamar.lolgamedata;


import androidx.test.espresso.ViewInteraction;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class PerformanceFlowTest extends FlowTest {
    @Test
    public void performanceFlowTest() {
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
        pause();

        ViewInteraction championName = onView(
                allOf(withText("Illaoi"), isDisplayed()));
        championName.perform(click());

        pause();

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

        pause();

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.abilityName),
                        withText("Q — Tentacle Smash")));
        textView5.check(matches(withText("Q — Tentacle Smash")));
    }
}

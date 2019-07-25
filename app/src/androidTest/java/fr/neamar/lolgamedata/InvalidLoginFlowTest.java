package fr.neamar.lolgamedata;


import android.annotation.SuppressLint;
import androidx.test.espresso.ViewInteraction;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class InvalidLoginFlowTest extends FlowTest {
    @SuppressLint("CommitPrefEdits")
    @Test
    public void invalidLoginTest() {
        String fakeAccount = "riot neamar 404";
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.coordinatorLayout),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.summonerText), isDisplayed()));
        editText.perform(replaceText(fakeAccount), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.save), withText("Add account"), isDisplayed()));
        button.perform(click());

        pause();

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.summonerText), withText(fakeAccount),
                        isDisplayed()));

        // Ideally, the string should come from the resource file
        editText3.check(matches(hasErrorText("Error adding account. Please double check your summoner name and region!")));

    }
}

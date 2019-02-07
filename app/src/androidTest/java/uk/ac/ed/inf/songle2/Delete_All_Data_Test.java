package uk.ac.ed.inf.songle2;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Delete_All_Data_Test {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void delete_All_Data_Test() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button3), withText("OPTIONS"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        pressBack();

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button), withText("Start"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.list_view),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                1)))
                .atPosition(1);
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.btnOpen), withText("GO!"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        withParent(withId(R.id.container))),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction button = onView(
                allOf(withId(R.id.guess), withText("Guess"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.edit_text_guess),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.edit_text_guess),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        editText2.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.edit_text_guess),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        editText3.perform(replaceText("song 2"), closeSoftKeyboard());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.btnOpen), withText("GO!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        button2.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.button7), withText("return to main menu"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.button), withText("Start"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton5.perform(click());

        pressBack();

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.button3), withText("OPTIONS"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                3),
                        isDisplayed()));
        appCompatButton6.perform(click());

        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(android.R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .atPosition(5);
        linearLayout.perform(click());

        ViewInteraction button3 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));
        button3.perform(click());

        pressBack();

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.button), withText("Start"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton7.perform(click());

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
}

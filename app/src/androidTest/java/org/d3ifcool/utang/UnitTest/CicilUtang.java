package org.d3ifcool.utang.UnitTest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.d3ifcool.utang.MainActivity;
import org.d3ifcool.utang.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class CicilUtang {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void cicilUtang() {
        onView(withText(R.string.dipinjam)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_dipinjam))
                .atPosition(0).perform(click());

        onView(withId(R.id.button_detail_cicil)).perform(click());

        onView(withId(R.id.editText_cicil_total)).perform(typeText("1000"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());

        onView(withId(R.id.action_history)).perform(click());
    }
}

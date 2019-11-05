package org.d3ifcool.utang.UnitTest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.d3ifcool.utang.MainActivity;
import org.d3ifcool.utang.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ExportUtang {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void exportUtang() {
        //script untuk export ke csv

        onView(withId(R.id.action_setting)).perform(click());
        onView(withId(R.id.textView_export_button_csv)).perform(click());
    }
}

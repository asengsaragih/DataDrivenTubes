package org.d3ifcool.utang.UiTest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import org.d3ifcool.utang.MainActivity;
import org.d3ifcool.utang.R;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class UiApplicationTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void plotUiTest() {
        //main activity
        onView(withText(R.string.dipinjam)).perform(click());
        onView(withText(R.string.meminjam)).perform(click());
        onView(withText(R.string.dipinjam)).perform(click());
        onView(withId(R.id.floatingbutton_main_utang)).perform(click());

        //form activity (insert data)
        onView(withId(R.id.edittext_form_tanggal_peminjaman)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 10+1, 21));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_form_nama)).perform(typeText("Aseng"), closeSoftKeyboard());
        onView(withId(R.id.edittext_form_nama)).perform(clearText());
        onView(withId(R.id.edittext_form_nama)).perform(typeText("Roline"), closeSoftKeyboard());
        onView(withId(R.id.edittext_form_jumlah)).perform(typeText("50000"), closeSoftKeyboard());
        onView(withId(R.id.edittext_form_jumlah)).perform(clearText());
        onView(withId(R.id.edittext_form_jumlah)).perform(typeText("75000"), closeSoftKeyboard());
        onView(withId(R.id.editText_form_phone)).perform(typeText("082165607625"), closeSoftKeyboard());
        onView(withId(R.id.editText_form_phone)).perform(clearText());
        onView(withId(R.id.editText_form_phone)).perform(typeText("082281538368"), closeSoftKeyboard());
        onView(withId(R.id.edittext_form_jatuh_tempo)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 10+1, 28));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_form_keterangan)).perform(typeText("Sepatu"), closeSoftKeyboard());
        onView(withId(R.id.edittext_form_keterangan)).perform(clearText());
        onView(withId(R.id.edittext_form_keterangan)).perform(typeText("Baju Kaos"), closeSoftKeyboard());
        onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());
        onView(withId(R.id.radioButton_form_meminjam)).perform(click());
        onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());
        onView(withId(R.id.button_form_tambah)).perform(click());

        //Cicil activity (Dipinjam)
        onView(withText(R.string.dipinjam)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_dipinjam))
                .atPosition(0).perform(click());
        onView(withId(R.id.button_detail_cicil)).perform(click());
        onView(withId(R.id.editText_cicil_total)).perform(typeText("10000"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());

        //form activity (edit data)
        onView(withText(R.string.dipinjam)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_dipinjam))
                .atPosition(0).perform(click());
        onView(withId(R.id.button_detail_edit)).perform(click());
        onView(withId(R.id.radioButton_form_meminjam)).perform(click());
        onView(withId(R.id.button_form_perbaruhi)).perform(click());

        //Cicil activity (Dipinjam)
        onView(withText(R.string.meminjam)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.listview_meminjam))
                .atPosition(0).perform(click());
        onView(withId(R.id.button_detail_cicil)).perform(click());
        onView(withId(R.id.editText_cicil_total)).perform(typeText("10000"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());

        //History activity
        onView(withId(R.id.action_history)).perform(click());
        onView(withText(R.string.dipinjam)).perform(click());
        onView(withText(R.string.meminjam)).perform(click());
        onView(isRoot()).perform(pressBack());

        //Chart Activity
        onView(withDrawable(R.drawable.ic_chart_24dp)).perform(click());
        onView(withId(R.id.textView_date_picker_chart)).perform(click());
        onView(withText("Nov")).perform(click());
        onView(withText("OK")).perform(click());
        onView(withId(R.id.textView_date_picker_chart)).perform(click());
        onView(withText("Des")).perform(click());
        onView(withText("OK")).perform(click());
        onView(isRoot()).perform(pressBack());

        //Setting Activity
        onView(withId(R.id.action_setting)).perform(click());
        onView(withId(R.id.textView_export_button_csv)).perform(click());

        //About Activity
        onView(withId(R.id.textView_about_aplication)).perform(click());
        onView(withId(R.id.imageView_button_right)).perform(click());
        onView(withId(R.id.imageView_button_left)).perform(click());
        onView(isRoot()).perform(pressBack());
        onView(isRoot()).perform(pressBack());
    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("with drawable from resource id: " + resourceId);
            }

            @Override
            public boolean matchesSafely(View view) {
                try {
                    final Drawable resourcesDrawable = view.getResources().getDrawable(resourceId);
                    if (view instanceof ImageView) {
                        final ImageView imageView = (ImageView) view;
                        if (imageView.getDrawable() == null) {
                            return resourcesDrawable == null;
                        }
                        return imageView.getDrawable().getConstantState()
                                .equals(resourcesDrawable.getConstantState());
                    }
                } catch (Resources.NotFoundException ignored) {

                }
                return false;
            }
        };
    }
}

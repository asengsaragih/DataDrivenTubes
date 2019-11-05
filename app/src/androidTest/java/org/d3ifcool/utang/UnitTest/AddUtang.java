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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AddUtang {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addUtang() {
        //script untuk menambah data

        onView(withId(R.id.floatingbutton_main_utang)).perform(click());

        onView(withId(R.id.edittext_form_tanggal_peminjaman)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_form_nama)).perform(typeText("Aldi"), closeSoftKeyboard());
        onView(withId(R.id.edittext_form_jumlah)).perform(typeText("500000"), closeSoftKeyboard());
        onView(withId(R.id.editText_form_phone)).perform(typeText("082165607625"), closeSoftKeyboard());
        onView(withId(R.id.edittext_form_jatuh_tempo)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.edittext_form_keterangan)).perform(typeText("Beli Pulsa"), closeSoftKeyboard());
        onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());

        onView(withId(R.id.button_form_tambah)).perform(click());
    }
}

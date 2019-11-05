package org.d3ifcool.utang.UnitTest;

import org.d3ifcool.utang.FormActivity;
import org.d3ifcool.utang.MainActivity;
import org.d3ifcool.utang.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class Driven {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void dataDriven() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFile("data.csv")));

        String line = "";

        int iteration = 0;

        while ((line = bufferedReader.readLine()) != null) {
            String[] str = line.split(",");

//            if (iteration == 0) {
//                iteration++;
//                continue;
//            }

            String create = str[1].toString().replace("\"", "");
            String deadline = str[2].toString().replace("\"", "");
            String pembayaran = str[3].toString().replace("\"", "");
            String nama = str[4].toString().replace("\"", "");
            String phone = str[5].toString().replace("\"", "");
            String jumlah = str[6].toString().replace("\"", "");
            String keterangan = str[7].toString().replace("\"", "");
            String kategori = str[8].toString().replace("\"", "");
            String status = str[9].toString().replace("\"", "");

            onView(withId(R.id.floatingbutton_main_utang)).perform(click());

            onView(withId(R.id.edittext_form_nama)).perform(typeText(nama), closeSoftKeyboard());
            onView(withId(R.id.edittext_form_jumlah)).perform(typeText(jumlah), closeSoftKeyboard());

            onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());
            onView(withId(R.id.button_form_tambah)).perform(click());

        }
    }

    private InputStream openFile(String filename) throws IOException {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }
}

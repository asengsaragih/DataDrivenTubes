package org.d3ifcool.utang;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DataDriven {

    @Rule
    public ActivityTestRule<FormActivity> formActivityActivityTestRule =
            new ActivityTestRule<>(FormActivity.class);

    @Test
    public void dataDriven() {
        Context context = ApplicationProvider.getApplicationContext();
        Helper helper = new Helper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        BufferedReader bufferedReader = null;
        String line = "";
        ContentValues values = new ContentValues();

        try {
//            InputStream inputStream = getClass().getResourceAsStream(String.valueOf(R.raw.data));
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            FileReader reader = new FileReader(String.valueOf(R.raw.data));
            bufferedReader = new BufferedReader(reader);
            int iteration = 0;

            database.beginTransaction();

            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split(",");

                if (iteration == 0) {
                    iteration++;
                    continue;
                }

                String create = str[1].toString().replace("\"", "");
                String deadline = str[2].toString().replace("\"", "");
                String pembayaran = str[3].toString().replace("\"", "");
                String nama = str[4].toString().replace("\"", "");
                String phone = str[5].toString().replace("\"", "");
                String jumlah = str[6].toString().replace("\"", "");
                String keterangan = str[7].toString().replace("\"", "");
                String kategori = str[8].toString().replace("\"", "");
                String status = str[9].toString().replace("\"", "");

                onView(withId(R.id.edittext_form_nama)).perform(typeText(nama), closeSoftKeyboard());

            }

        } catch (IOException e) {

        }
    }
}

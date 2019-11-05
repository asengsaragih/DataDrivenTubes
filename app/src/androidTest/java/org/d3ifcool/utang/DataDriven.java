package org.d3ifcool.utang;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.d3ifcool.utang.base.CsvReader;
import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class DataDriven {

    @Rule
    public ActivityTestRule<FormActivity> formActivityActivityTestRule =
            new ActivityTestRule<>(FormActivity.class);

//    @Test
//    public void dataDriven() {
//        File file = new File(String.valueOf(getClass().getResourceAsStream("data.csv")));
//        CsvReader csvReader = new CsvReader();
//
//        try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
//            CsvRow row;
//            while ((row = csvParser.nextRow()) != null) {
//                System.out.println("Read line: " + row);
//                System.out.println("First column of line: " + row.getField(0));
//            }
//        }
//    }

//    @Test
//    public void dataDriven() {
//        try {
//            String line = "";
//            BufferedReader bufferedReader;
//            ContentValues values = new ContentValues();
//
//            Context context = getInstrumentation().getContext();
//            Resources resources = context.getResources();
//
//
//            InputStream inputStream = getClass().getResourceAsStream("data.csv");
//            CsvReader reader = new CsvReader(new FileReader(String.valueOf(inputStream)));
//            String[] nextLine;
//            while ((nextLine = reader.readNext()) != null) {
//                // nextLine[] is an array of values from the line
//                onView(withId(R.id.edittext_form_nama)).perform(typeText("Aseng"), closeSoftKeyboard());
//            }
////            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
////
////            int iteration = 0;
////
////            while ((line = bufferedReader.readLine()) != null) {
////                String[] str = line.split(",");
////
////                if (iteration == 0) {
////                    iteration++;
////                    continue;
////                }
////
////                String create = str[1].toString().replace("\"", "");
////                String deadline = str[2].toString().replace("\"", "");
////                String pembayaran = str[3].toString().replace("\"", "");
////                String nama = str[4].toString().replace("\"", "");
////                String phone = str[5].toString().replace("\"", "");
////                String jumlah = str[6].toString().replace("\"", "");
////                String keterangan = str[7].toString().replace("\"", "");
////                String kategori = str[8].toString().replace("\"", "");
////                String status = str[9].toString().replace("\"", "");
////
////                onView(withId(R.id.edittext_form_nama)).perform(typeText("Aseng"), closeSoftKeyboard());
////                onView(withId(R.id.edittext_form_jumlah)).perform(typeText("50000"), closeSoftKeyboard());
////
////                onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());
////                onView(withId(R.id.button_form_tambah)).perform(click());
////
////            }
//
//
//        }catch (Exception e) {
//
//        }
//
//
//    }

//    @Test
//    public void dataDriven() {
//
//        BufferedReader bufferedReader = null;
//        String line = "";
//        ContentValues values = new ContentValues();
//
//        try {
//            Context context = getInstrumentation().getContext();
//            Resources resources = context.getResources();
//
//            InputStream inputStream = resources.openRawResource(R.raw.data);
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            int iteration = 0;
//
//            while ((line = bufferedReader.readLine()) != null) {
//                String[] str = line.split(",");
//
//                if (iteration == 0) {
//                    iteration++;
//                    continue;
//                }
//
//                String create = str[1].toString().replace("\"", "");
//                String deadline = str[2].toString().replace("\"", "");
//                String pembayaran = str[3].toString().replace("\"", "");
//                String nama = str[4].toString().replace("\"", "");
//                String phone = str[5].toString().replace("\"", "");
//                String jumlah = str[6].toString().replace("\"", "");
//                String keterangan = str[7].toString().replace("\"", "");
//                String kategori = str[8].toString().replace("\"", "");
//                String status = str[9].toString().replace("\"", "");
//
//                onView(withId(R.id.edittext_form_nama)).perform(typeText(nama), closeSoftKeyboard());
//                onView(withId(R.id.edittext_form_jumlah)).perform(typeText(jumlah), closeSoftKeyboard());
//
//                onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());
//                onView(withId(R.id.button_form_tambah)).perform(click());
//
//            }
//
//        } catch (IOException e) {
//
//        }
//    }
}

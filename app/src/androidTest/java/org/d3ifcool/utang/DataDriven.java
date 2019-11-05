package org.d3ifcool.utang;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.github.mikephil.charting.utils.EntryXComparator;

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
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DataDriven {

//    @Rule
//    public ResourceFile res = new ResourceFile("/res.txt");
//
//    @Test
//    public void test() throws Exception
//    {
//        assertTrue(res.getContent().length() > 0);
//        assertTrue(res.getFile().exists());
//    }

//    @Test
//    public void dataDriven() {
//
//        try {
//            URL url = this.getClass().getResource("data.csv");
//            File testWsdl = new File(url.getFile());
//
////            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFile("data.csv")));
//
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
////            onView(withId(R.id.edittext_form_nama)).perform(typeText("Aseng"), closeSoftKeyboard());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private InputStream openFile(String filename) throws IOException {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

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
//
//            String[] nextLine;
//            while ((nextLine = reader.readNext()) != null) {
//                // nextLine[] is an array of values from the line
//                onView(withId(R.id.edittext_form_nama)).perform(typeText("Aseng"), closeSoftKeyboard());
//            }
////            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
////
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
//                onView(withId(R.id.edittext_form_nama)).perform(typeText("Aseng"), closeSoftKeyboard());
//                onView(withId(R.id.edittext_form_jumlah)).perform(typeText("50000"), closeSoftKeyboard());
//
//                onView(withId(R.id.radiobutton_form_dipinjam)).perform(click());
//                onView(withId(R.id.button_form_tambah)).perform(click());
//
//            }
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

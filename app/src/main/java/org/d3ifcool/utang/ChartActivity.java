package org.d3ifcool.utang;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.util.ArrayList;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity {

    MethodeFunction methodeFunction = new MethodeFunction();

    private PieChart mPaidOffChart;
    private PieChart mNotPaidOffChart;

    private TextView mPickerMonthTextView;
    private TextView mTotalDipinjamPaidOffTextView;
    private TextView mTotalMeminjamPaidOffTextView;
    private TextView mTotalDipinjamNotPaidOffTextView;
    private TextView mTotalMeminjamNotPaidOffTextView;
    private ConstraintLayout mChartContentConstraint;

    private SQLiteDatabase database;
    private Cursor cursor;

    private String CURRENT_MONTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mPaidOffChart = findViewById(R.id.pie_chart_paid_off);
        mNotPaidOffChart = findViewById(R.id.pie_chart_not_paid_off);

        mPickerMonthTextView = findViewById(R.id.textView_date_picker_chart);

        mTotalDipinjamPaidOffTextView = findViewById(R.id.textView_paid_off_dipinjam);
        mTotalMeminjamPaidOffTextView = findViewById(R.id.textView_paid_off_meminjam);
        mTotalDipinjamNotPaidOffTextView = findViewById(R.id.textView_not_paid_off_dipinjam);
        mTotalMeminjamNotPaidOffTextView = findViewById(R.id.textView_not_paid_off_meminjam);

        mChartContentConstraint = findViewById(R.id.constraint_content_chart);

        mDetailClick();
    }

    private void mDetailClick() {
        mPickerMonthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowMonthPicker();
            }
        });
    }

    private void mShowMonthPicker() {
        new RackMonthPicker(this)
                .setLocale(new Locale("in", "ID"))
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        String monthString = null;
                        if (month == 1) {
                            monthString = getString(R.string.month_jan);
                        } else if (month == 2) {
                            monthString = getString(R.string.month_feb);
                        } else if (month == 3) {
                            monthString = getString(R.string.month_mar);
                        } else if (month == 4) {
                            monthString = getString(R.string.month_apr);
                        } else if (month == 5) {
                            monthString = getString(R.string.month_may);
                        } else if (month == 6) {
                            monthString = getString(R.string.month_jun);
                        } else if (month == 7) {
                            monthString = getString(R.string.month_jul);
                        } else if (month == 8) {
                            monthString = getString(R.string.month_aug);
                        } else if (month == 9) {
                            monthString = getString(R.string.month_sep);
                        } else if (month == 10) {
                            monthString = getString(R.string.month_oct);
                        } else if (month == 11) {
                            monthString = getString(R.string.month_nov);
                        } else if (month == 12) {
                            monthString = getString(R.string.month_dec);
                        }
                        CURRENT_MONTH = monthString + " " + year;
                        mPickerMonthTextView.setText(CURRENT_MONTH);

                        mChartContentConstraint.setVisibility(View.VISIBLE);

//                        check(month, year);
                        mShowPaidOffChart(month, year);
                        mShowNotPaidOffChart(month, year);

                        mTotalDipinjamNotPaidOffTextView.setText(getString(R.string.total_dipinjam) + " " +  methodeFunction.currencyIndonesian(mNotPaidOffDipinjam(month, year)));
                        mTotalMeminjamNotPaidOffTextView.setText(getString(R.string.total_meminjam) + " " +  methodeFunction.currencyIndonesian(mNotPaidOffMeminjam(month, year)));
                        mTotalDipinjamPaidOffTextView.setText(getString(R.string.total_dipinjam) + " " +  methodeFunction.currencyIndonesian(mPaidOffDipinjam(month, year)));
                        mTotalMeminjamPaidOffTextView.setText(getString(R.string.total_meminjam) + " " +  methodeFunction.currencyIndonesian(mPaidOffMeminjam(month, year)));
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog alertDialog) {
                        alertDialog.dismiss();
                    }
                })
                .setColorTheme(R.color.colorPrimaryDarkForPicker)
                .show();
    }

    private void check(int month, int year) {
//        int fillter;
//        String yearString = CURRENT_MONTH.substring(CURRENT_MONTH.length() - 4);
//        String monthString = CURRENT_MONTH.replace(" " + yearString, "");
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter = String.valueOf(year) + monthString + "01";

        methodeFunction.toastMessage(this, filter);
    }

    private void mShowPaidOffChart(int month, int year) {
        int dipinjam = mPaidOffDipinjam(month, year);
        int meminjam = mPaidOffMeminjam(month, year);

        ArrayList entries = new ArrayList();
        entries.add(new PieEntry(dipinjam, "Dipinjam"));
        entries.add(new PieEntry(meminjam, "Meminjam"));

        int[] colors = {Color.argb(255, 36, 100, 201), Color.argb(255, 240, 169, 36)};

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(pieDataSet);

        mPaidOffChart.setData(pieData);
        mPaidOffChart.animateXY(500, 500);
        mPaidOffChart.invalidate();
        mPaidOffChart.getDescription().setText("Utang Apps");
    }

    private void mShowNotPaidOffChart(int month, int year) {
        int dipinjam = mNotPaidOffDipinjam(month, year);
        int meminjam = mNotPaidOffMeminjam(month, year);

        ArrayList entries = new ArrayList();
        entries.add(new PieEntry(dipinjam, "Dipinjam"));
        entries.add(new PieEntry(meminjam, "Meminjam"));

        int[] colors = {Color.argb(255, 240, 169, 36), Color.argb(255, 36, 100, 201)};

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(pieDataSet);

        mNotPaidOffChart.setData(pieData);
        mNotPaidOffChart.animateXY(500, 500);
        mNotPaidOffChart.invalidate();
        mNotPaidOffChart.getDescription().setText("Utang Apps");
    }

    private int mNotPaidOffDipinjam(int month, int year) {
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter1 = String.valueOf(year) + monthString + "01";
        String filter2 = String.valueOf(year) + monthString + "31";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 1" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_DIPINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter1 +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter2, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        cursor.close();
        return total;
    }

    private int mNotPaidOffMeminjam(int month, int year) {
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter1 = String.valueOf(year) + monthString + "01";
        String filter2 = String.valueOf(year) + monthString + "31";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 1" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_MEMINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter1 +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter2, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return total;
    }

    private int mPaidOffDipinjam(int month, int year) {
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter1 = String.valueOf(year) + monthString + "01";
        String filter2 = String.valueOf(year) + monthString + "31";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 2" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_DIPINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter1 +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter2, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return total;
    }

    private int mPaidOffMeminjam(int month, int year) {
        String monthString;
        if (month <= 9) {
            monthString = "0" + String.valueOf(month);
        } else {
            monthString = String.valueOf(month);
        }
        String filter1 = String.valueOf(year) + monthString + "01";
        String filter2 = String.valueOf(year) + monthString + "31";

        database = new Helper(this).getReadableDatabase();
        cursor = database.rawQuery(
                "SELECT SUM(" +
                        Contract.UtangEntry.COLUMN_UTANG_JUMLAH +
                        ") FROM " + Contract.UtangEntry.TABLE_NAME +
                        " WHERE " + Contract.UtangEntry.COLUMN_UTANG_STATUS +
                        " = 2" + " AND " +
                        Contract.UtangEntry.COLUMN_UTANG_KATEGORI + " = " + Contract.UtangEntry.UTANG_KATEGORI_MEMINJAM +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " >= " + filter1 +
                        " AND " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " <= " + filter2, null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return total;
    }
}

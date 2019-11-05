package org.d3ifcool.utang.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.R;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MethodeFunction {

    public static final int ACCESS_READ_CONTACT = 10;
    public static final int ACCESS_READ_PHONE_NUMBERS = 11;
    public static final int ACCESS_CALL_PHONE = 12;
    public static final int ACCESS_READ_EXTERNAL_STORAGE = 13;
    public static final int ACCESS_WRITE_EXTERNAL_STORAGE = 14;
    public static final int ACCESS_MULTIPLE_STORAGE = 14;

    public String subStringTanggal(String rawTanggal) {
        String year = rawTanggal.substring(0,4);
        String month = rawTanggal.substring(4,6);
        String day = rawTanggal.substring(6);

        String subString = year + "/" + month + "/" + day;

        return subString;
    }

    public String currencyIndonesian(int total) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);

        return format.format(total);
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = simpleDateFormat.format(c.getTime());

        return formattedDate;
    }

    public TextWatcher jumlahChangeListener(final EditText jumlahEditText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                jumlahEditText.removeTextChangedListener(this);

                try {
                    String originalJumlahString = editable.toString();

                    Long longval;
                    if (originalJumlahString.contains(",")) {
                        originalJumlahString = originalJumlahString.replace("," ,"");
                    }
                    longval = Long.parseLong(originalJumlahString);

                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    decimalFormat.applyPattern("#,###,###,###");
                    String formatedString = decimalFormat.format(longval);

                    jumlahEditText.setText(formatedString);
                    jumlahEditText.setSelection(jumlahEditText.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                jumlahEditText.addTextChangedListener(this);
            }
        };
    }

    public void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void readContactPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_CONTACTS}, ACCESS_READ_CONTACT);
    }

    public void callPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CALL_PHONE}, ACCESS_CALL_PHONE);
    }

    public void readPhoneNumberPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_PHONE_NUMBERS}, ACCESS_READ_PHONE_NUMBERS);
    }

    public void readStoragePermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, ACCESS_READ_EXTERNAL_STORAGE);
    }

    public String checkFilePath(String filename) {
        String result;
        if (filename.contains(".")) {
            int startIndex = filename.indexOf(0);
            int endIndex = filename.indexOf(".");
            String replacement = "";
            String toBeReplace = filename.substring(startIndex + 1, endIndex);

            String newFilename = filename.replace(toBeReplace, replacement);

            if (newFilename.contains(".csv")) {
                return result = "true";
            } else {
                return result = "false";
            }
        } else {
            return result = "false";
        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void deleteAll(Context context) {
        SQLiteDatabase database = new Helper(context).getWritableDatabase();
        database.execSQL("DELETE FROM " + Contract.UtangEntry.TABLE_NAME);
        database.close();
    }

    public void deleteSelectedHistory(final Context context, final Uri uri, Cursor cursor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String text;

        String nama = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_NAMA));
        int jumlah = cursor.getInt(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_JUMLAH));

        text = "Nama : " + nama + "\n"
                + "Jumlah : " + currencyIndonesian(jumlah);

        builder.setTitle(R.string.delete_data);

        builder.setMessage(text);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.getContentResolver().delete(uri, null, null);
                toastMessage(context, context.getString(R.string.form_activity_delete_success));
            }
        });

        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    public boolean checkAndRequestStoragePermission(Context context) {
        int permissionReadStorage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    ACCESS_MULTIPLE_STORAGE);
            return false;
        }
        return true;
    }
}

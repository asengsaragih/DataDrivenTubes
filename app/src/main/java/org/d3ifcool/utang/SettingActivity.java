package org.d3ifcool.utang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.base.CsvWritter;
import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static org.d3ifcool.utang.base.MethodeFunction.ACCESS_READ_EXTERNAL_STORAGE;
import static org.d3ifcool.utang.base.MethodeFunction.ACCESS_WRITE_EXTERNAL_STORAGE;

public class SettingActivity extends AppCompatActivity {

    MethodeFunction methodeFunction = new MethodeFunction();

    private TextView mCloseWarningTextView;
    private TextView mExportCsvTextView;
    private TextView mImportTextView;
    private TextView mAboutAplicationTextView;
    private TextView mCriticismSuggestionTextView;

    private ConstraintLayout mWarningTextConstrainLayout;

    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mCloseWarningTextView = findViewById(R.id.textView_detele_button_warning_text);
        mExportCsvTextView = findViewById(R.id.textView_export_button_csv);
        mImportTextView = findViewById(R.id.textView_import_button);
        mAboutAplicationTextView = findViewById(R.id.textView_about_aplication);
        mCriticismSuggestionTextView = findViewById(R.id.textView_about_criticism_suggestions);
        mWarningTextConstrainLayout = findViewById(R.id.constraitlayout_setting_warning);

        mButtonSettingClicked();
    }

    private void mButtonSettingClicked() {
        mCloseWarningTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWarningTextConstrainLayout.setVisibility(View.GONE);
            }
        });

        mExportCsvTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExportToCsv(getApplicationContext());
            }
        });

        mImportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpenFilePicker();
            }
        });

        mAboutAplicationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TentangActivity.class);
                startActivity(intent);
            }
        });

        mCriticismSuggestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: " + "aldiwahyu.saragih@gmail.com, devianarahmadhani@gmail.com, nimaderiarolina@gmail.com, "));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Masukkan Dan Saran Utang Apps");
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n\nDikirim Melalui " + methodeFunction.getDeviceName());

                startActivity(Intent.createChooser(intent, "Send Feedback"));
            }
        });
    }


    private void mOpenFilePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.setting_activity_import_data);
        builder.setMessage(R.string.setting_activity_import_procedure);
        builder.setPositiveButton(R.string.setting_activity_pick_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                mPickerFile();
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

    private void mPickerFile() {
        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //jika permisi belum disetujui
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                methodeFunction.readStoragePermission(this);
            } else {
                methodeFunction.readStoragePermission(this);
            }
        } else {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");

            Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            sIntent.addCategory(Intent.CATEGORY_DEFAULT);

            Intent chooserIntent;
            if (getPackageManager().resolveActivity(sIntent, 0) != null) {
                chooserIntent = Intent.createChooser(sIntent, "Open File");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intent});
            } else {
                chooserIntent = Intent.createChooser(intent, "Open file");
            }

            try {
                startActivityForResult(chooserIntent, FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException e) {
                methodeFunction.toastMessage(SettingActivity.this, getString(R.string.warning_open_file_chooser));
            }
        }
    }

    private void mExportToCsv(Context context) {
        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //jika permisi belum disetujui
            if (ActivityCompat.shouldShowRequestPermissionRationale(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                methodeFunction.checkAndRequestStoragePermission(this);
            } else {
                methodeFunction.checkAndRequestStoragePermission(this);
            }
        } else {
            Helper helper = new Helper(context);

            File exportFolder = new File(Environment.getExternalStorageDirectory() + "/" + "Utang");

            if (!exportFolder.exists()) {
                exportFolder.mkdir();
            }

            String fileName = "data.csv";
            File file = new File(exportFolder, fileName);

            try {
                file.createNewFile();
                CsvWritter csvWritter = new CsvWritter(new FileWriter(file));
                SQLiteDatabase database = helper.getReadableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM utang", null);
                csvWritter.writeNext(cursor.getColumnNames());

                while (cursor.moveToNext()) {
                    String arrStr[] = {
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                    };

                    csvWritter.writeNext(arrStr);
                }
                methodeFunction.toastMessage(context, getString(R.string.export_csv_success));
                csvWritter.close();
                cursor.close();

            } catch (Exception e) {
                methodeFunction.toastMessage(context, e.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri path = data.getData();
                    BufferedReader bufferedReader = null;
                    String line = "";
                    ContentValues values = new ContentValues();

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(path);
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String checkPath = methodeFunction.checkFilePath(path.getPath().toString());

                        if (checkPath == "true") {
                            int iteration = 0;

                            methodeFunction.deleteAll(this);

                            while ((line = bufferedReader.readLine()) != null) {
                                String[] str = line.split(",");

                                if (str.length != 10) {
                                    methodeFunction.toastMessage(this, getString(R.string.wrong_file));
                                    continue;
                                }

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

                                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, Integer.parseInt(create));
                                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, deadline);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN, pembayaran);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, nama);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_PHONE, phone);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, Integer.parseInt(jumlah));
                                values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, keterangan);
                                values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, Integer.parseInt(kategori));
                                values.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, Integer.parseInt(status));

                                getContentResolver().insert(Contract.UtangEntry.CONTENT_URI, values);

                            }
                            methodeFunction.toastMessage(this, getString(R.string.setting_activity_success_import));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            Toast.makeText(this, getString(R.string.wrong_extension), Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_READ_EXTERNAL_STORAGE:
            case ACCESS_WRITE_EXTERNAL_STORAGE:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                methodeFunction.toastMessage(getApplicationContext(), getString(R.string.storage_permission_granted));
            }
        }
    }


}

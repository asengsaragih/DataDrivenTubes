package org.d3ifcool.utang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;

public class CicilActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    MethodeFunction methodeFunction = new MethodeFunction();

    private static final int EXISTING_CICIL_LOADER = 0;

    private String mTanggalPeminjamanTmp;
    private String mNamaTmp;
    private String mJumlahTmp;
    private String mPhoneTmp;
    private String mKeteranganTmp;
    private String mJatuhTempoTmp;
    private String mKategoriTmp;
    private String mStatusTmp;

    private String mKategoriString;
    private String mStatusString;

    private Uri mCurrentUtangUri;

    private TextView mTanggalPeminjamanTextView;
    private TextView mNamaTextView;
    private TextView mJumlahTextView;
    private TextView mPhoneTextView;
    private TextView mKeteranganTextView;
    private TextView mJatuhTempoTextView;
    private ImageView mShowMoreImageView;
    private EditText mCicilEditText;
    private Button mCicilButton;

    private boolean mCicilHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mCicilHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicil);

        mInitialCicilComponent();
        mGetDataCicil();
        mShowMoreButton();
        mButtonCicilClick();
    }

    private void mInitialCicilComponent() {
        mTanggalPeminjamanTextView = findViewById(R.id.textView_cicil_tanggal_peminjaman);
        mNamaTextView = findViewById(R.id.textView_cicil_nama);
        mJumlahTextView = findViewById(R.id.textView_cicil_jumlah);
        mPhoneTextView = findViewById(R.id.textView_cicil_phone);
        mKeteranganTextView = findViewById(R.id.textView_cicil_keterangan);
        mJatuhTempoTextView = findViewById(R.id.textView_cicil_jatuh_tempo);

        mShowMoreImageView = findViewById(R.id.imageView5);

        mCicilEditText = findViewById(R.id.editText_cicil_total);
        mCicilEditText.addTextChangedListener(methodeFunction.jumlahChangeListener(mCicilEditText));

        mCicilButton = findViewById(R.id.button);
    }

    private void mGetDataCicil() {
        Intent intent = getIntent();
        mCurrentUtangUri = intent.getData();

        LoaderManager.getInstance(this).initLoader(EXISTING_CICIL_LOADER, null, this);

        mCicilEditText.setOnTouchListener(mTouchListener);
    }

    private void mShowMoreButton() {
        final TextView titlePhoneTextView = (TextView) findViewById(R.id.textView15);
        final TextView titleKeteranganTextView = (TextView) findViewById(R.id.textView17);
        final TextView titleJatuhTempoTextView = (TextView) findViewById(R.id.textView19);

        mShowMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleJatuhTempoTextView.getVisibility() == View.GONE) {
                    titleJatuhTempoTextView.setVisibility(View.VISIBLE);
                    titleKeteranganTextView.setVisibility(View.VISIBLE);
                    titlePhoneTextView.setVisibility(View.VISIBLE);
                    mJatuhTempoTextView.setVisibility(View.VISIBLE);
                    mKeteranganTextView.setVisibility(View.VISIBLE);
                    mPhoneTextView.setVisibility(View.VISIBLE);
                    mShowMoreImageView.setImageResource(R.drawable.ic_arrow_drop_up_24dp);
                } else {
                    titleJatuhTempoTextView.setVisibility(View.GONE);
                    titleKeteranganTextView.setVisibility(View.GONE);
                    titlePhoneTextView.setVisibility(View.GONE);
                    mJatuhTempoTextView.setVisibility(View.GONE);
                    mKeteranganTextView.setVisibility(View.GONE);
                    mPhoneTextView.setVisibility(View.GONE);
                    mShowMoreImageView.setImageResource(R.drawable.ic_arrow_drop_down_24dp);
                }
            }
        });
    }

    private void mButtonCicilClick() {
        mCicilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaveUtang();
            }
        });
    }

    private void mSaveUtang() {
        String cicilTotalString = mCicilEditText.getText().toString().trim().replace(",", "");

        if (TextUtils.isEmpty(cicilTotalString)) {
            methodeFunction.toastMessage(getApplicationContext(), getString(R.string.cicil_activity_empty_total));
            return;
        }

        if (Integer.parseInt(cicilTotalString) <= 999) {
            methodeFunction.toastMessage(getApplicationContext(), getString(R.string.form_data_empty_total));
            return;
        }

        if (Integer.parseInt(cicilTotalString) >= Integer.parseInt(mJumlahTmp)) {
            methodeFunction.toastMessage(getApplicationContext(), getString(R.string.cicil_activity_bigger_cicil_then_total));
            return;
        }

        int substractionTotal = Integer.parseInt(mJumlahTmp) - Integer.parseInt(cicilTotalString);

        ContentValues values = new ContentValues();
        ContentValues cicilValues = new ContentValues();

        values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, mTanggalPeminjamanTmp);
        values.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, mNamaTmp);
        values.put(Contract.UtangEntry.COLUMN_UTANG_PHONE, mPhoneTmp);
        values.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, substractionTotal);
        values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, mJatuhTempoTmp);
        values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, mKeteranganTmp);
        values.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, 1);
        values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, mKategoriTmp);

        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, mTanggalPeminjamanTmp);
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, mNamaTmp);
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_PHONE, mPhoneTmp);
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN, Integer.parseInt(methodeFunction.getCurrentDate()));
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, Integer.parseInt(cicilTotalString));
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, mJatuhTempoTmp);
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, mKeteranganTmp);
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, 2);
        cicilValues.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, mKategoriTmp);

//         update dulu baru tambah data
        int update = getContentResolver().update(mCurrentUtangUri, values, null, null);
        Uri add = getContentResolver().insert(Contract.UtangEntry.CONTENT_URI, cicilValues);

        if (update == 0) {
            Toast.makeText(this, getString(R.string.main_activity_update_failed), Toast.LENGTH_SHORT).show();
        } else {
            if (add == null) {
                Toast.makeText(this, getString(R.string.form_activity_insert_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.form_activity_insert_success), Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                Contract.UtangEntry._ID,
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE,
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE,
                Contract.UtangEntry.COLUMN_UTANG_NAMA,
                Contract.UtangEntry.COLUMN_UTANG_PHONE,
                Contract.UtangEntry.COLUMN_UTANG_KETERANGAN,
                Contract.UtangEntry.COLUMN_UTANG_KATEGORI,
                Contract.UtangEntry.COLUMN_UTANG_STATUS,
                Contract.UtangEntry.COLUMN_UTANG_JUMLAH

        };

        return new CursorLoader(this,
                mCurrentUtangUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1){
            return;
        }

        if (cursor.moveToFirst()) {
            int tanggalPeminjamanIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE);
            int jatuhTempoIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE);
            int namaIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_NAMA);
            int phoneIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_PHONE);
            int jumlahIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_JUMLAH);
            int keteranganIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN);
            int statusIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_STATUS);
            int kategoriIndex = cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_KATEGORI);

            String stringTanggalPeminjaman = cursor.getString(tanggalPeminjamanIndex);
            String stringJatuhTempo = cursor.getString(jatuhTempoIndex);
            String stringNama = cursor.getString(namaIndex);
            String stringKeterangan = cursor.getString(keteranganIndex);
            String stringPhone = cursor.getString(phoneIndex);
            int intJumlah = cursor.getInt(jumlahIndex);
            int intKategori = cursor.getInt(kategoriIndex);
            int intStatus = cursor.getInt(statusIndex);

            mTanggalPeminjamanTmp = stringTanggalPeminjaman;
            mNamaTmp = stringNama;
            mJumlahTmp = String.valueOf(intJumlah);
            mPhoneTmp = stringPhone;
            mKeteranganTmp = stringKeterangan;
            mJatuhTempoTmp = stringJatuhTempo;
            mKategoriTmp = String.valueOf(intKategori);
            mStatusTmp = String.valueOf(intStatus);

            mKategoriString = String.valueOf(intKategori);
            mStatusString = String.valueOf(intStatus);

            mTanggalPeminjamanTextView.setText(": " + methodeFunction.subStringTanggal(stringTanggalPeminjaman));
            mNamaTextView.setText(": " + stringNama);
            mJumlahTextView.setText(": " + methodeFunction.currencyIndonesian(intJumlah));

            if (!TextUtils.isEmpty(stringJatuhTempo)) {
                mJatuhTempoTextView.setText(": " + methodeFunction.subStringTanggal(stringJatuhTempo));
            } else {
                mJatuhTempoTextView.setText(": -");
            }

            if (!TextUtils.isEmpty(stringPhone)) {
                mPhoneTextView.setText(": " + stringPhone);
            } else {
                mPhoneTextView.setText(": -");
            }

            if (!TextUtils.isEmpty(stringKeterangan)) {
                mKeteranganTextView.setText(": " + stringKeterangan);
            } else {
                mKeteranganTextView.setText(": -");
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onBackPressed() {
        if (!mCicilHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };

        mShowUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mCicilHasChanged) {
                    NavUtils.navigateUpFromSameTask(CicilActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(CicilActivity.this);
                    }
                };

                mShowUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mShowUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.form_activity_unsaved_dialog_message);
        builder.setPositiveButton(R.string.leave, discardButtonClickListener);
        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

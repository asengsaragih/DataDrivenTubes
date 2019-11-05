package org.d3ifcool.utang;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;
import org.d3ifcool.utang.database.Helper;

import java.text.NumberFormat;
import java.util.Locale;

import static org.d3ifcool.utang.base.MethodeFunction.ACCESS_CALL_PHONE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DipinjamMainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    MethodeFunction methodeFunction = new MethodeFunction();

    public DipinjamMainFragment() {
        // Required empty public constructor
    }

    private static final int UTANG_DIPINJAM_LOADER = 0;

    CursorMainUtangAdapter mCursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_dipinjam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDisplayDipinjamListview();
        LoaderManager.getInstance(getActivity()).initLoader(UTANG_DIPINJAM_LOADER, null, DipinjamMainFragment.this);
    }

    private void mDisplayDipinjamListview() {
        final ListView listView = getView().findViewById(R.id.listview_dipinjam);
        View emptyView = getView().findViewById(R.id.emptyview_dipinjam);

        mCursorAdapter = new CursorMainUtangAdapter(getActivity(), null);

        listView.setEmptyView(emptyView);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, final long id) {
                final Cursor cursor = (Cursor) listView.getItemAtPosition(i);
                detailUtang(getContext(), cursor, id);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(getContext(), FormActivity.class);
                Uri currentDipinjamUri = ContentUris.withAppendedId(Contract.UtangEntry.CONTENT_URI, id);
                intent.setData(currentDipinjamUri);
                startActivity(intent);

                return true;
            }
        });
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

        //pengkondisian status dan kategori menggunakan clausa where
        String clausaWhere = Contract.UtangEntry.COLUMN_UTANG_STATUS+" =? "+" AND "+ Contract.UtangEntry.COLUMN_UTANG_KATEGORI+" =? ";
        String statusWhere = "1";
        String kategoriWhere = "1";
        String[] whereArgs = {
                statusWhere.toString(),
                kategoriWhere.toString()
        };

        return new CursorLoader(getActivity(),
                Contract.UtangEntry.CONTENT_URI,
                projection,
                clausaWhere,
                whereArgs,
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE + " ASC, " + Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void detailUtang(Context context, Cursor cursor, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View detailView = getActivity().getLayoutInflater().inflate(R.layout.dialog_detail_main, null);

        builder.setView(detailView);
        final AlertDialog alertDialog = builder.create();

        TextView tanggalPeminjamanDetailTextView = detailView.findViewById(R.id.textView_detail_tanggal_peminjaman);
        TextView namaDetailTextView = detailView.findViewById(R.id.textView_detail_nama);
        TextView jumlahDetailTextView = detailView.findViewById(R.id.textView_detail_jumlah);
        TextView keteranganDetailTextView = detailView.findViewById(R.id.textView_detail_keterangan);
        TextView jatuhTempoDetailTextView = detailView.findViewById(R.id.textView_detail_jatuh_tempo);
        TextView phoneDetailTextView = detailView.findViewById(R.id.textView_detail_phone);

        final EditText jumlahCicilEditText = detailView.findViewById(R.id.editText_detail_cicilan);
        jumlahCicilEditText.addTextChangedListener(methodeFunction.jumlahChangeListener(jumlahCicilEditText));

        Button markAsDoneDetailButton = detailView.findViewById(R.id.button_detail_mark_as_done);
        Button cicilDetailButton = detailView.findViewById(R.id.button_detail_cicil);
        Button editDetailButton = detailView.findViewById(R.id.button_detail_edit);
        Button yesCicilDetailButton = detailView.findViewById(R.id.button_detail_yes);
        Button cancleCicilDetailButton = detailView.findViewById(R.id.button_detail_cancle);
        Button messageDetailButton = detailView.findViewById(R.id.button_detail_message);
        Button callDetailButton = detailView.findViewById(R.id.button_detail_call);

        final ConstraintLayout detailUtangView = detailView.findViewById(R.id.constraitLayout_detail);
        final ConstraintLayout cicilUtangView = detailView.findViewById(R.id.constraitLayout_detail_payment_cicil);

        final String tanggalPeminjamanString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE));
        final String namaString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_NAMA));
        final String jumlahString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_JUMLAH));
        final String jatuhTempoString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE));
        final String keteranganString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN));
        final String phoneString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_PHONE));

        tanggalPeminjamanDetailTextView.setText(": " + methodeFunction.subStringTanggal(tanggalPeminjamanString));
        namaDetailTextView.setText(": " + namaString);
        jumlahDetailTextView.setText(": " + methodeFunction.currencyIndonesian(Integer.parseInt(jumlahString)));

        if (!TextUtils.isEmpty(jatuhTempoString)) {
            jatuhTempoDetailTextView.setText(": " + methodeFunction.subStringTanggal(jatuhTempoString));
        } else  {
            jatuhTempoDetailTextView.setText(": -");
        }

        if (!TextUtils.isEmpty(keteranganString)) {
            keteranganDetailTextView.setText(": " + keteranganString);
        } else  {
            keteranganDetailTextView.setText(": -");
        }

        if (!TextUtils.isEmpty(phoneString)) {
            phoneDetailTextView.setText(": " + phoneString);
        } else  {
            phoneDetailTextView.setText(": -");
        }

        //untuk view gone
        cicilUtangView.setVisibility(View.GONE);
        if (cursor.getInt(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_KATEGORI)) == 1) {
            if (!TextUtils.isEmpty(phoneString)) {
                callDetailButton.setVisibility(View.VISIBLE);
                messageDetailButton.setVisibility(View.VISIBLE);
            } else {
                callDetailButton.setVisibility(View.GONE);
                messageDetailButton.setVisibility(View.GONE);
            }
        } else {
            callDetailButton.setVisibility(View.GONE);
            messageDetailButton.setVisibility(View.GONE);
        }
        //end view gone

        final Uri currentUtangUri = ContentUris.withAppendedId(Contract.UtangEntry.CONTENT_URI, id);
        final ContentValues values = new ContentValues();
        final ContentValues cicilValues = new ContentValues();

        markAsDoneDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE, tanggalPeminjamanString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_NAMA, namaString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN, Integer.parseInt(methodeFunction.getCurrentDate()));
                values.put(Contract.UtangEntry.COLUMN_UTANG_JUMLAH, Integer.parseInt(jumlahString));
                values.put(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE, jatuhTempoString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN, keteranganString);
                values.put(Contract.UtangEntry.COLUMN_UTANG_STATUS, 2);
                values.put(Contract.UtangEntry.COLUMN_UTANG_KATEGORI, 1);

                int rowAffected = getActivity().getContentResolver().update(currentUtangUri, values, null, null);

                if (rowAffected == 0) {
                    Toast.makeText(getActivity(), getString(R.string.main_activity_update_failed), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.main_activity_update_success), Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
                getActivity().recreate();

//                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                startActivity(intent);
            }
        });

        cicilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CicilActivity.class);
                final Uri currentDipinjamUri = ContentUris.withAppendedId(Contract.UtangEntry.CONTENT_URI, id);
                intent.setData(currentDipinjamUri);
                startActivity(intent);
                alertDialog.cancel();
            }
        });

        editDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FormActivity.class);
                final Uri currentDipinjamUri = ContentUris.withAppendedId(Contract.UtangEntry.CONTENT_URI, id);
                intent.setData(currentDipinjamUri);
                startActivity(intent);
                alertDialog.cancel();
            }
        });

        yesCicilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // telah diganti dengan cicil activity
            }
        });

        cancleCicilDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // telah diganti dengan cicil activity
            }
        });

        callDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckPermissionCall(phoneString);
            }
        });

        messageDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:"+phoneString);
                String messageFirstParagraph = "Kepada Yth. saudara/i " + namaString + ". Anda memiliki utang kepada saya sebesar " + methodeFunction.currencyIndonesian(Integer.parseInt(jumlahString)) + ". Pada tanggal " + methodeFunction.subStringTanggal(tanggalPeminjamanString);
                String messageMiddleParagraph = "";
                String messageLastParagraph = ".\n\nDownload Utang Apps di PlayStore \nbit.ly/utangapps";

                if (!TextUtils.isEmpty(jatuhTempoString)) {
                    messageMiddleParagraph = ". Mohon untuk segera dilunaskan sebelum tanggal " + methodeFunction.subStringTanggal(jatuhTempoString) + ". Terima Kasih";
                } else  {
                    messageMiddleParagraph = ". Mohon untuk segera dilunaskan. Terima Kasih";
                }


                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body",messageFirstParagraph + messageMiddleParagraph + messageLastParagraph);
                startActivity(intent);
            }
        });


        alertDialog.show();
//        builder.show();
    }

    private void mCheckPermissionCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                methodeFunction.callPermission(getContext());
            } else {
                methodeFunction.callPermission(getContext());
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    methodeFunction.toastMessage(getContext(), getString(R.string.call_permission_granted));
                }
                break;
        }
    }
}

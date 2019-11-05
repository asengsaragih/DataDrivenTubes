package org.d3ifcool.utang;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.d3ifcool.utang.base.MethodeFunction;
import org.d3ifcool.utang.database.Contract;


/**
 * A simple {@link Fragment} subclass.
 */
public class DipinjamHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    MethodeFunction methodeFunction = new MethodeFunction();


    public DipinjamHistoryFragment() {
        // Required empty public constructor
    }

    private static final int UTANG_HISTORY_DIPINJAM_LOADER = 3;

    private CursorHistoryUtangAdapter mCursorAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_dipinjam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDisplayDipinjamListview();

        LoaderManager.getInstance(getActivity()).initLoader(UTANG_HISTORY_DIPINJAM_LOADER, null, DipinjamHistoryFragment.this);
    }

    private void mDisplayDipinjamListview() {
        final ListView listView = getView().findViewById(R.id.listview_history_dipinjam);
        View emptyView = getView().findViewById(R.id.emptyview_history_dipinjam);

        mCursorAdapter = new CursorHistoryUtangAdapter(getActivity(), null);

        listView.setEmptyView(emptyView);
        listView.setAdapter(mCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //ntar disini ditambah kodingan untuk dialog box
                final Cursor cursor = (Cursor) listView.getItemAtPosition(i);
                detailHistoryUtang(getContext(), cursor, id);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(i);
                Uri uri = ContentUris.withAppendedId(Contract.UtangEntry.CONTENT_URI, id);
                methodeFunction.deleteSelectedHistory(getContext(), uri, cursor);
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
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN,
                Contract.UtangEntry.COLUMN_UTANG_KETERANGAN,
                Contract.UtangEntry.COLUMN_UTANG_KATEGORI,
                Contract.UtangEntry.COLUMN_UTANG_STATUS,
                Contract.UtangEntry.COLUMN_UTANG_JUMLAH
        };

        //pengkondisian status dan kategori menggunakan clausa where

        String clausaWhere = Contract.UtangEntry.COLUMN_UTANG_STATUS+"=?"+" AND "+ Contract.UtangEntry.COLUMN_UTANG_KATEGORI+"=?";
        String statusWhere = "2";
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
                Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN + " ASC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void detailHistoryUtang(Context context, Cursor cursor, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View detailHistoryView = getActivity().getLayoutInflater().inflate(R.layout.dialog_detail_history, null);

        TextView tanggalPeminjamanDetailTextView = detailHistoryView.findViewById(R.id.textView_detail_history_tanggal_peminjaman);
        TextView namaDetailTextView = detailHistoryView.findViewById(R.id.textView_detail_history_nama);
        TextView jumlahDetailTextView = detailHistoryView.findViewById(R.id.textView_detail_history_jumlah);
        TextView keteranganDetailTextView = detailHistoryView.findViewById(R.id.textView_detail_history_keterangan);
        TextView jatuhTempoDetailTextView = detailHistoryView.findViewById(R.id.textView_detail_history_jatuh_tempo);
        TextView phoneDetailTextView = detailHistoryView.findViewById(R.id.textView_detail_history_phone);
        TextView tanggalPembayaranDetailTextView = detailHistoryView.findViewById(R.id.textView_detail_history_tanggal_pembayaran);

        String tanggalPeminjamanString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_CREATE));
        String namaString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_NAMA));
        String jumlahString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_JUMLAH));
        String jatuhTempoString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_DEADLINE));
        String keteranganString = cursor.getString(cursor.getColumnIndexOrThrow(Contract.UtangEntry.COLUMN_UTANG_KETERANGAN));
        String phoneString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_PHONE));
        String tanggalPembayaranString = cursor.getString(cursor.getColumnIndex(Contract.UtangEntry.COLUMN_UTANG_TANGGAL_PEMBAYARAN));

        tanggalPeminjamanDetailTextView.setText(": " + methodeFunction.subStringTanggal(tanggalPeminjamanString));
        namaDetailTextView.setText(": " + namaString);
        jumlahDetailTextView.setText(": " + methodeFunction.currencyIndonesian(Integer.parseInt(jumlahString)));
        tanggalPembayaranDetailTextView.setText(": " + methodeFunction.subStringTanggal(tanggalPembayaranString));

        if (!TextUtils.isEmpty(jatuhTempoString)) {
            jatuhTempoDetailTextView.setText(": " + methodeFunction.subStringTanggal(jatuhTempoString));
        } else {
            jatuhTempoDetailTextView.setText(": -");
        }

        if (!TextUtils.isEmpty(phoneString)) {
            phoneDetailTextView.setText(": " + phoneString);
        } else {
            phoneDetailTextView.setText(": -");
        }

        if (!TextUtils.isEmpty(keteranganString)) {
            keteranganDetailTextView.setText(": " + keteranganString);
        } else {
            keteranganDetailTextView.setText(": -");
        }

        builder.setView(detailHistoryView);
        builder.show();
    }
}

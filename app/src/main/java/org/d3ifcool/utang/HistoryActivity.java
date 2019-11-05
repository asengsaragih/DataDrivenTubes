package org.d3ifcool.utang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class HistoryActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FragmentManager fragmentManager;
    private TextView mDeleteHintTextView;
    private ConstraintLayout mHintConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mDeleteHintTextView = findViewById(R.id.textView_history_hint_delete);
        mHintConstraintLayout = findViewById(R.id.constraint_history_hint);

        //kodingan untuk two pane
        isTwoPane();

        //kodingan untuk delete hint
        mDeleteHintView();
    }

    private void mDeleteHintView() {
        mDeleteHintTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHintConstraintLayout.setVisibility(View.GONE);
            }
        });
    }

    private void isTwoPane() {
        fragmentManager = this.getSupportFragmentManager();

        if (findViewById(R.id.dipinjam_history_container) != null) {
            fragmentManager.beginTransaction()
                    .add(R.id.meminjam_history_container, new MeminjamHistoryFragment())
                    .commit();

            fragmentManager.beginTransaction()
                    .add(R.id.dipinjam_history_container, new DipinjamHistoryFragment())
                    .commit();
        } else {
            //kodingan untuk viewpager dan tablayout
            mHistoryViewPager();
        }
    }

    private void mHistoryViewPager() {
        //kodingan untuk view pager
        mViewPager = findViewById(R.id.viewpager_history_utang);
        KategoriHistoryUtangAdapter kategoriHistoryUtangAdapter = new KategoriHistoryUtangAdapter(HistoryActivity.this, getSupportFragmentManager());
        mViewPager.setAdapter(kategoriHistoryUtangAdapter);

        //kodingan untuk tab layout
        mTabLayout = findViewById(R.id.tablayout_history_utang);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}

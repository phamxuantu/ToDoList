package com.toanutc.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int TAB_POSITION = 0;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static String arrCategory[] = {"Bạn bè", "Gia đình", "Công việc", "Học tập", "Khác"};
    static boolean active = false;


    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        final Button btnSearch = findViewById(R.id.search);
        final Button btnClose = findViewById(R.id.close);
        final ImageView imgSearch = findViewById(R.id.imgsearch);
        final TextView lblToolbar = findViewById(R.id.lblToolbar);
        final EditText edtSearch = findViewById(R.id.edtSearch);
        Button btnAdd = findViewById(R.id.add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddListActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lblToolbar.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                imgSearch.setVisibility(View.VISIBLE);
                edtSearch.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.VISIBLE);
                edtSearch.setText("");
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblToolbar.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.GONE);
                edtSearch.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                if (TAB_POSITION == 0) {
                    TabAll tabAll = new TabAll();
                    tabAll.updateList();
                } else if (TAB_POSITION == 1) {
                    TabDone tabDone = new TabDone();
                    tabDone.updateList();
                } else {
                    TabNotDone tabNotDone = new TabNotDone();
                    tabNotDone.updateList();
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TAB_POSITION == 0) {
                    TabAll tabAll = new TabAll();
                    tabAll.searchAll(s);
                } else if (TAB_POSITION == 1) {
                    TabDone tabDone = new TabDone();
                    tabDone.searchDone(s);
                } else {
                    TabNotDone tabNotDone = new TabNotDone();
                    tabNotDone.searchNotDone(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TAB_POSITION = tab.getPosition();
//                Log.e("test tab", String.valueOf(position));

                if (TAB_POSITION == 0) {
                    TabAll tabAll = new TabAll();
                    tabAll.updateList();
                } else if (TAB_POSITION == 1) {
                    TabDone tabDone = new TabDone();
                    tabDone.updateList();
                } else {
                    TabNotDone tabNotDone = new TabNotDone();
                    tabNotDone.updateList();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabAll(), "All");
        adapter.addFragment(new TabDone(), "Done");
        adapter.addFragment(new TabNotDone(), "Not Done");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }
}

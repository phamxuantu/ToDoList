package com.toanutc.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static int TAB_POSITION = 0;
    public static Context context;
    public static String arrCategory[] = {"Bạn bè", "Gia đình", "Công việc", "Học tập", "Khác"};
    public static SQLiteHelper db;
    static boolean active = false;


    TabLayout tabLayout;
    ViewPager viewPager;
    int selectedTab = 0;

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

        db = new SQLiteHelper(this, 1);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        final Button btnSearch = (Button) findViewById(R.id.search);
        final Button btnClose = (Button) findViewById(R.id.close);
        final ImageView imgSearch = (ImageView) findViewById(R.id.imgsearch);
        final TextView lblToolbar = (TextView) findViewById(R.id.lblToolbar);
        final EditText edtSearch = (EditText) findViewById(R.id.edtSearch);
        Button btnAdd = (Button) findViewById(R.id.add);

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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                if (selectedTab == 0) {
                    TabAll tabAll = new TabAll();
                    tabAll.updateList();
                } else if (selectedTab == 1) {
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
                if(selectedTab == 0)
                {
                    TabAll tabAll = new TabAll();
                    tabAll.searchAll(s);
                } else if (selectedTab == 1)
                {
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
                selectedTab = tab.getPosition();
                TAB_POSITION = tab.getPosition();
//                Log.e("test tab", String.valueOf(position));

                if (selectedTab == 0) {
                    TabAll tabAll = new TabAll();
                    tabAll.updateList();
                } else if (selectedTab == 1) {
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

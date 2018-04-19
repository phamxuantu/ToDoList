package com.toanutc.todolist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TabNotDone extends Fragment {

    static SQLiteHelper db;
    @SuppressLint("StaticFieldLeak")
    static ToDoAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    static RecyclerView recyclerView;
    static ArrayList<ToDo> arrayList;
    static LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.activity_tab_notdone, null);
        db = new SQLiteHelper(MainActivity.context, 1);
        initView(view);
        return view;
    }

    public void initView(View view) {
        recyclerView = view.findViewById(R.id.listNotDone);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MainActivity.context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.context, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        arrayList = new ArrayList<>();

        List<ToDo> list = db.getToDosNotDone();
        arrayList.addAll(list);

        adapter = new ToDoAdapter(arrayList, MainActivity.context, R.layout.row_layout_tab_all, db);
        recyclerView.setAdapter(adapter);
    }

    public void searchNotDone(CharSequence s) {
        String query = s.toString().toLowerCase().trim();
        ArrayList<ToDo> filterList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            String text = arrayList.get(i).getName().toLowerCase();
            if (text.contains(query)) {
                filterList.add(arrayList.get(i));
            }
        }

        recyclerView.setLayoutManager(layoutManager);
        adapter = new ToDoAdapter(filterList, MainActivity.context, R.layout.row_layout_tab_all, db);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateList() {

        arrayList = new ArrayList<>();
        List<ToDo> list = db.getToDosNotDone();
        arrayList.addAll(list);

        adapter = new ToDoAdapter(arrayList, MainActivity.context, R.layout.row_layout_tab_all, db);
        recyclerView.setAdapter(adapter);
    }

}

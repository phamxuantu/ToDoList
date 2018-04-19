package com.toanutc.todolist;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddListActivity extends AppCompatActivity {

    EditText edtName, edtDescription;
    Button btnSelectDateTime, btnSave, btnCancel;
    Spinner spinner;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int positionCategory = 0;
    public static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        edtName = findViewById(R.id.edtNameTodo);
        edtDescription = findViewById(R.id.edtDescription);
        btnSelectDateTime = findViewById(R.id.btnSelectDateTime);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btncancel);
        spinner = findViewById(R.id.spinnerCategoryAdd);
        calendar = Calendar.getInstance();

        btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddListActivity.this);
                builder.setTitle("Chọn ngày và giờ");

                LayoutInflater inflater = AddListActivity.this.getLayoutInflater();
                @SuppressLint("InflateParams") View itemView = inflater.inflate(R.layout.layout_select_time, null);

                final TextView txtDate = itemView.findViewById(R.id.txtDateSelected);
                final TextView txtTime = itemView.findViewById(R.id.txtTimeSelected);
                Button btnSelectDate = itemView.findViewById(R.id.btnSelectDate);
                Button btnSelectTime = itemView.findViewById(R.id.btnSelectTime);

                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);

//                Log.e("test cuurent", mDay + "/" + mMonth + "/" + mYear + " " + mHour + ":" + mMinute);

                txtDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                txtTime.setText(mHour + ":" + mMinute);

                btnSelectDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddListActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        calendar.set(year, monthOfYear, dayOfMonth);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                btnSelectTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddListActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        txtTime.setText(hourOfDay + ":" + minute);
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();
                    }
                });

                builder.setView(itemView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnSelectDateTime.setText(txtDate.getText() + " " + txtTime.getText());
//                        Log.e("test select time", calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                    }
                }).setNegativeButton("Tắt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnSelectDateTime.setText("Tắt");
                    }
                });

                builder.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddListActivity.this, android.R.layout.simple_spinner_item, MainActivity.arrCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionCategory = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                SQLiteHelper db = new SQLiteHelper(AddListActivity.this, 1);

                if ((!btnSelectDateTime.getText().equals("Tắt")) && (calendar.getTimeInMillis() > System.currentTimeMillis())) {

                    ToDo toDo = new ToDo(1, false, edtName.getText().toString(), btnSelectDateTime.getText().equals("Tắt") ? "" : btnSelectDateTime.getText().toString(), MainActivity.arrCategory[positionCategory], edtDescription.getText().toString(), btnSelectDateTime.getText().equals("Tắt") ? 0 : R.drawable.clock);
                    int id = db.addToDo(toDo);

                    Intent intent = new Intent(AddListActivity.this, MyService.class);
                    intent.putExtra("status", "addJob");
                    AddListActivity.this.startService(intent);
                    Util.scheduleJob(MainActivity.context, id, calendar);
                }else {
                    ToDo toDo = new ToDo(1, true, edtName.getText().toString(), btnSelectDateTime.getText().equals("Tắt") ? "" : btnSelectDateTime.getText().toString(), MainActivity.arrCategory[positionCategory], edtDescription.getText().toString(), btnSelectDateTime.getText().equals("Tắt") ? 0 : R.drawable.clock);
                    db.addToDo(toDo);
                }

                if (MainActivity.TAB_POSITION == 0) {
                    TabAll tabAll = new TabAll();
                    tabAll.updateList();
                } else if (MainActivity.TAB_POSITION == 1) {
                    TabDone tabDone = new TabDone();
                    tabDone.updateList();
                } else {
                    TabNotDone tabNotDone = new TabNotDone();
                    tabNotDone.updateList();
                }

                finish();

            }
        });
    }
}

package com.toanutc.todolist;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Date;

public class AddListActivity extends AppCompatActivity {

    EditText edtName, edtDescription;
    Button btnSelectDateTime, btnSave, btnCancel;
    Spinner spinner;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int positionCategory = 0;
    public static Calendar calendar = Calendar.getInstance();
    public static int idTodo = 0;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        edtName = (EditText) findViewById(R.id.edtNameTodo);
        edtDescription = (EditText) findViewById(R.id.edtDescription);
        btnSelectDateTime = (Button) findViewById(R.id.btnSelectDateTime);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btncancel);
        spinner = (Spinner) findViewById(R.id.spinnerCategoryAdd);
        sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);

        btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddListActivity.this);
                builder.setTitle("Chọn ngày và giờ");

                LayoutInflater inflater = AddListActivity.this.getLayoutInflater();
                View itemView = inflater.inflate(R.layout.layout_select_time, null);

                final TextView txtDate = (TextView) itemView.findViewById(R.id.txtDateSelected);
                final TextView txtTime = (TextView) itemView.findViewById(R.id.txtTimeSelected);
                Button btnSelectDate = (Button) itemView.findViewById(R.id.btnSelectDate);
                Button btnSelectTime = (Button) itemView.findViewById(R.id.btnSelectTime);

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                Log.e("test cuurent", mDay + "/" + mMonth + "/" + mYear + " " + mHour + ":" + mMinute);

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
                        Log.e("test select time", calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddListActivity.this, android.R.layout.simple_spinner_item, MainActivity.arrCategory);
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
                Intent intent = new Intent(AddListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                SQLiteHelper db = new SQLiteHelper(AddListActivity.this, 1);
                ToDo toDo = new ToDo(1, false, edtName.getText().toString(), btnSelectDateTime.getText().equals("Tắt") ? "" : btnSelectDateTime.getText().toString(), MainActivity.arrCategory[positionCategory], edtDescription.getText().toString(), btnSelectDateTime.getText().equals("Tắt") ? 0 : R.drawable.clock);
                idTodo = db.addToDo(toDo);
                sharedPreferences.edit().putInt("ID", idTodo).commit();


                if (!btnSelectDateTime.getText().equals("Tắt")) {
                    Intent intent = new Intent(AddListActivity.this, MyService.class);
                    AddListActivity.this.startService(intent);
                    Util.scheduleJob(MainActivity.context, idTodo);
                }

                Intent intent = new Intent(AddListActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}

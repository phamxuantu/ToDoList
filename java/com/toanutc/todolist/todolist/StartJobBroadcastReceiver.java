package com.toanutc.todolist;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StartJobBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<ToDo> arrayList = new ArrayList<>();
        List<ToDo> list = MyService.db.getToDosNotDone();

        arrayList.addAll(list);

//        Log.e("arr job after reboot", arrayList.size() + "");

        for (int i = 0; i < arrayList.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                calendar.setTime(simpleDateFormat.parse(arrayList.get(i).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Util.scheduleJob(context, arrayList.get(i).getId(), calendar);
        }
    }
}

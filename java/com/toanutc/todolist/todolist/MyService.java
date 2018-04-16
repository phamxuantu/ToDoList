package com.toanutc.todolist;

import android.app.IntentService;
import android.app.Service;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {

    public static SQLiteHelper db;
    public static ToDo toDo;
    public static int ID = 0;



    public MyService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("test service", "create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences("my_data", MODE_PRIVATE);
        Log.e("test service", "start");
        db = new SQLiteHelper(this, 1);
        Log.e("test db", db.toString());

        ID = sharedPreferences.getInt("ID",0);
        Log.e("test id service", String.valueOf(ID));
        toDo = db.getToDo(ID);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

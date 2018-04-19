package com.toanutc.todolist;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    public static SQLiteHelper db;

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
//        Log.e("test service", "create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        Log.e("test service", "start");
        db = new SQLiteHelper(this, 1);
        Log.e("test db", db.toString());
//
//        Log.e("test intent", intent.getExtras().getString("status"));
        if (intent != null)
        {
            Log.e("test intent", intent.getExtras().getString("status"));
            if (intent.getExtras().getString("status").equals("reboot"))
            {
                Intent intent2 = new Intent(this,StartJobBroadcastReceiver.class);
                intent2.setAction("SERVICE_DONE");
                this.sendBroadcast(intent2);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        db.close();

        super.onDestroy();
    }


}

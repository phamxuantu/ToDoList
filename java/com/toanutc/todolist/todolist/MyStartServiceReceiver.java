package com.toanutc.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sev_user on 13-Apr-18.
 */

public class MyStartServiceReceiver extends BroadcastReceiver {

    public static SQLiteHelper db;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        db = new SQLiteHelper(context, 1);
//        Util.scheduleJob(context, );
}
}

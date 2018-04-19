package com.toanutc.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyStartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent intentService = new Intent(context, MyService.class);
            intentService.putExtra("status", "reboot");
            context.startService(intentService);
        }
    }
}

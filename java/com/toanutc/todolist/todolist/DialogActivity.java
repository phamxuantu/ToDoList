package com.toanutc.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
//        Log.e("screen on", ""+isScreenOn);
        if(isScreenOn==false)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);

            wl.release();
            wl_cpu.release();
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1;
        params.flags = WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().setAttributes(params);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        Intent intent = getIntent();
        final ToDo toDo = MyService.db.getToDo(intent.getExtras().getInt("idToDo"));
        Intent intentNot = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNot, 0);
        Notification.Builder notBuilder = new Notification.Builder(this).setContentTitle(toDo.getName())
                .setContentText(toDo.getDescription())
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(intent.getExtras().getInt("idToDo"), notBuilder.build());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(toDo.getName());
        builder.setMessage(toDo.getDescription());
        builder.setPositiveButton("Tắt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyService.db.updateTodo(new ToDo(toDo.getId(), true, toDo.getName(), toDo.getTime(), toDo.getCategories(), toDo.getDescription(), toDo.getImageAlert()));
                if (MainActivity.active) {
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
                }
                mediaPlayer.release();
                finish();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}

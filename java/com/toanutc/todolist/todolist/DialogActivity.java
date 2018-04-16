package com.toanutc.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final ToDo toDo = MyService.db.getToDo(intent.getExtras().getInt("idToDo"));
        Intent intentNot = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intentNot,0);
        Notification.Builder notBuilder = new Notification.Builder(this).setContentTitle(toDo.getName())
                .setContentText(toDo.getDescription())
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true).setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notBuilder.build());

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
                finish();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}

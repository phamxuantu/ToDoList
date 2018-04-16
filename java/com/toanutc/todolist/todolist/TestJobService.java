package com.toanutc.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by sev_user on 13-Apr-18.
 */

public class TestJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {

        Log.e("test db job", MyService.db.toString());

        Intent intent = new Intent(this, DialogActivity.class);
        intent.putExtra("idToDo", params.getJobId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        jobFinished(params, false);
//        Intent service = new Intent(getApplicationContext(), MainActivity.class);
//        getApplicationContext().startService(service);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}


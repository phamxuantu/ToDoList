package com.toanutc.todolist;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

public class TestJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {

//        Log.e("test db job", MyService.db.toString());

        Intent intent = new Intent(this, DialogActivity.class);
        intent.putExtra("idToDo", params.getJobId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}


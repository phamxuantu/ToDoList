package com.toanutc.todolist;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.RequiresApi;

import java.util.Calendar;

class Util {
    private static JobScheduler jobScheduler;

    @RequiresApi(api = 21)
    static void scheduleJob(Context context, int id, Calendar calendar) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(id, serviceComponent);
//        Log.e("scheduler id", id + "");
        builder.setMinimumLatency(calendar.getTimeInMillis() - System.currentTimeMillis());
        builder.setOverrideDeadline(calendar.getTimeInMillis() - System.currentTimeMillis() + 5 * 1000);
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        jobScheduler.schedule(builder.build());
    }

    static void stopJob(int idJob, Context context) {
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(idJob);
//        Log.e("canceled job", "done");
    }
}

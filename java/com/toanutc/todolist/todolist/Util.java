package com.toanutc.todolist;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by sev_user on 13-Apr-18.
 */

public class Util {
    static JobScheduler jobScheduler;



    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context, int id) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(id, serviceComponent);
        Log.e("scheduler id", id + "");
//        builder.se
        builder.setMinimumLatency(AddListActivity.calendar.getTimeInMillis() - System.currentTimeMillis()); // wait at least
        builder.setOverrideDeadline(AddListActivity.calendar.getTimeInMillis() - System.currentTimeMillis() + 5*1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        jobScheduler = (JobScheduler)
                context.getSystemService(JobScheduler.class);

        jobScheduler.schedule(builder.build());
    }

    public static void stopJob(int idJob) {
        jobScheduler.cancel(idJob);
    }
}

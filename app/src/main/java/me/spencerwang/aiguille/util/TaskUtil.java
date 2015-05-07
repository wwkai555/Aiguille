package me.spencerwang.aiguille.util;

import android.os.AsyncTask;
import android.os.Build;


/**
 * Created by SpencerWang on 2015/4/28.
 */
public class TaskUtil {
    public static <Params, Progress, Result> void executeAsyncTask(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}

package com.app.vv_voronov.weatherforecast.utilities;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class FirebaseJobService extends JobService{
    private AsyncTask<Void,Void,Void> mAsyncTask;
    @Override
    public boolean onStartJob( final JobParameters job) {
        mAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SyncUtils.startSync(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                jobFinished(job,false);
            }
        }.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mAsyncTask!=null)
        mAsyncTask.cancel(true);
        return true;
    }
}

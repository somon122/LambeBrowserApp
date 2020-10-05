package com.world_tech_point.lambebrowser.serviceFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class AsynckT extends AsyncTask<Integer,Integer,String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private WeakReference<DownloadActivity> activityWeakReference;

    AsynckT(DownloadActivity activity) {
        activityWeakReference = new WeakReference<DownloadActivity>(activity);
    }

    public AsynckT() {}

    @Override
    protected String doInBackground(Integer... integers) {

        for (int i = 0; i < integers[0]; i++) {
            publishProgress((i * 100) / integers[0]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "Download Complete";
    }

    @Override
    protected void onPreExecute() {

        DownloadActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.progressBar.setVisibility(View.VISIBLE);


    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        DownloadActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.progressBar.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(String s) {
        DownloadActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
        activity.progressBar.setProgress(0);
        activity.progressBar.setVisibility(View.INVISIBLE);
    }


}

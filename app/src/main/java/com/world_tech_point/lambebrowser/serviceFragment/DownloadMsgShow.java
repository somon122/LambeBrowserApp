package com.world_tech_point.lambebrowser.serviceFragment;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadMsgShow extends AsyncTask<String,Integer,String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    public DownloadMsgShow() {}

    @Override
    protected String doInBackground(String... params) {

        @SuppressLint
                ("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String dateTime = sdf.format(new Date());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(params[0]));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dateTime + "."+params[1]);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);

        return "Download Complete";
    }

    @Override
    protected void onPreExecute() {


    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        Toast.makeText(context, ""+values[0], Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, ""+s, Toast.LENGTH_SHORT).show();

    }


}

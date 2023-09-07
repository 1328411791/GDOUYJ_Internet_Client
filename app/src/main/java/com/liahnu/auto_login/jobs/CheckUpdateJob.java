package com.liahnu.auto_login.jobs;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.liahnu.auto_login.domain.DownloadConfig;
import com.liahnu.auto_login.execption.VersionException;
import com.liahnu.auto_login.utilliiy.CheckUpdate;

import java.io.IOException;

public class CheckUpdateJob implements Runnable {

    private final static String TAG = "CheckUpdateJob";
    private final Activity activity;

    public CheckUpdateJob(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        CheckUpdate checkUpdate = new CheckUpdate(activity.getBaseContext());
        try {
            DownloadConfig config = checkUpdate.checkUpdate4Github();
            startBrowser(config);
        } catch (PackageManager.NameNotFoundException | IOException e) {
            Log.e(TAG, "检查更新失败");
            showResponse("检查更新失败,请检查网络连接");
        } catch (VersionException e) {
            Log.e(TAG, "已经是最新版本");
            showResponse("已经是最新版本，目前版本为" + e.getNowVersion() + "，最新版本为" + e.getNewVersion());
        }
    }

    public void start() {
        new Thread(this).start();
    }

    private void startBrowser(final DownloadConfig config) {
        activity.runOnUiThread(() -> {
            Log.i(TAG, config.getDownloadUrl());
            Toast.makeText(activity, "检测到有新版本，正在跳转到浏览器", Toast.LENGTH_LONG).show();
            // 将url加入下载队列
            Uri uri = Uri.parse(config.getDownloadUrl());
            DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(config.getFileName());
            request.setDescription(config.getFileName());
            request.setVisibleInDownloadsUi(true);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            long id = downloadManager.enqueue(request);
            Log.i(TAG, "download id: " + id);
        });
    }

    private void showResponse(String message) {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        });
    }
}

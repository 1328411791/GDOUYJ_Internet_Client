package com.liahnu.auto_login.jobs;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

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
            String downloadURL = checkUpdate.checkUpdate4Github();
            startBrowser(downloadURL);
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

    private void startBrowser(final String url) {
        activity.runOnUiThread(() -> {
            Log.i(TAG, url);
            Toast.makeText(activity, "跳转到浏览器", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            activity.startActivity(intent);
        });
    }

    private void showResponse(String message) {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        });
    }
}

package com.liahnu.auto_login.utilliiy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.liahnu.auto_login.client.ApisGithubReleaseClient;
import com.liahnu.auto_login.domain.DownloadConfig;
import com.liahnu.auto_login.execption.VersionException;

import java.io.IOException;


public class CheckUpdate {

    private final static String TAG = "checkUpdate";

    private final Context context;

    public CheckUpdate(Context context){
        this.context = context;
    }


    public String isUpdate() throws IOException, PackageManager.NameNotFoundException, VersionException {
        String version = ApisGithubReleaseClient.getReleaseVersion();

        if (version == null){
            throw new IOException("获取版本失败");
        }

        // 获取当前版本
        String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;

        if (versionName.equals(version.replace("v",""))){
            throw new VersionException(version.replace("v",""),versionName);
        }
        return version;
    }

    public DownloadConfig checkUpdate4Github() throws PackageManager.NameNotFoundException, IOException, VersionException {

        String version = isUpdate();

        String DownloadUrl = "https://github.com/1328411791/GDOUYJ_Internet_Client/releases/download/"
                + version
                + "/GDOUYJ_Internet_Client_" + version + ".apk";

        Log.i(TAG, "DownloadURL: " + DownloadUrl);

        DownloadConfig config = new DownloadConfig();
        config.setVersion(version);
        config.setDownloadUrl(DownloadUrl);
        config.setFileName("GDOUYJ_Internet_Client_" + version + ".apk");

        return config;
    }



}






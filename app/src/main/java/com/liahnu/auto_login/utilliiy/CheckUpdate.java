package com.liahnu.auto_login.utilliiy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.liahnu.auto_login.client.ApisGithubReleaseClient;

import java.io.IOException;

public class CheckUpdate {

    private final static String TAG = "checkUpdate";

    private Context context;

    public CheckUpdate(Context context){
        this.context = context;
    }


    public String checkUpdate4Github() throws PackageManager.NameNotFoundException, IOException {

        String version = ApisGithubReleaseClient.getReleaseVersion();

        if (version == null){
            return "获取版本失败";
        }

        // 获取当前版本
        String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;

        if (versionName.equals(version.replace("v",""))){
            return "当前版本为最新版本";
        }

        String DownloadUrl = "https://github.com/1328411791/GDOUYJ_Internet_Client/releases/download/"
                + version
                + "/GDOUYJ_Internet_Client_"+ version +".apk";

        // 跳转浏览器下载
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(android.net.Uri.parse(DownloadUrl));
        context.startActivity(intent);

        return "更新完成";
    }

}






package com.liahnu.auto_login.utilliiy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.liahnu.auto_login.view.WebView;

public class PermissionRequest {

    private Activity activity;
    private String[] permissions;

    private final static int REQUEST_CODE_CONTACT = 101;

    private final static String TAG = "PermissionRequest";


    public PermissionRequest(Activity activity, String[] permissions) {
        this.activity = activity;
        this.permissions = permissions;
    }

    public void requestPermission(){
        //验证是否许可权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (String str : permissions) {
                Log.i(TAG, "request permission:" + str );
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    activity.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }
}

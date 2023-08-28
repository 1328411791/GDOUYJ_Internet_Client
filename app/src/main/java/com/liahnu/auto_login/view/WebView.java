package com.liahnu.auto_login.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liahnu.auto_login.R;
import com.liahnu.auto_login.utilliiy.PermissionRequest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebView extends AppCompatActivity {

    private static final String TAG = "WebView";
    TextView responseText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        Button sendRRequest = (Button) findViewById(R.id.sent_requert);
        responseText =(TextView) findViewById(R.id.response_test);
       sendRRequest.setOnClickListener(view -> {
           sendRequestWithHttpURL();
       });
    }

    private void sendRequestWithHttpURL(){
        PermissionRequest permissionRequest = new PermissionRequest(this,
                new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
        permissionRequest.requestPermission();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

}

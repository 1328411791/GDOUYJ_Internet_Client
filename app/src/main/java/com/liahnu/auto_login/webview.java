package com.liahnu.auto_login;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class webview extends AppCompatActivity {
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
        new  Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client =new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.200.132.20:801/eportal/portal/login?user_account=202144011308&user_password=05261x")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

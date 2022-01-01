package com.liahnu.auto_login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;
    private String responseText="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit= (EditText) findViewById(R.id.User);
        passwordEdit =(EditText) findViewById(R.id.Password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        Switch autologin = (Switch) findViewById(R.id.Auto_Login);
        Button button1 = (Button) findViewById(R.id.button_login);
        Button button_logout =(Button) findViewById(R.id.button_logout);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember){
            String account =pref.getString("account","");
            String password =pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        button1.setOnClickListener(view -> {
            //判断按钮代码
            //Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_SHORT).show();
            Log.d("MainActivity","Click Save");
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            sendLogin();
            editor = pref.edit();
            if(rememberPass.isChecked()){
                editor.putBoolean("remember_password",true);
                editor.putString("account",account);
                editor.putString("password",password);
            }else{
                editor.clear();
            }
            editor.apply();

        });

        button_logout.setOnClickListener(view -> {
            Log.d("MainActivity","Click Logout");
            sendLogout();
        });


    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Setting_item:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.WebView:
                Intent intent_web =new Intent(MainActivity.this,webview.class);
                startActivity(intent_web);
                break;
            case R.id.internet:
                Toast.makeText(this, "internet", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://10.200.132.20/"));
                startActivity(intent);
                break;
            case R.id.Exit_item:
                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
        }
        return true;
    }

    private void sendLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String account = accountEdit.getText().toString();
                    String password = passwordEdit.getText().toString();
                    OkHttpClient client =new OkHttpClient();
                    String url = "http://10.200.132.20:801/eportal/portal/login?" + "user_account=" + account +
                            "&user_password=" + password;
                    Request request = new Request.Builder()
                            .url(url)
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

    private void sendLogout(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client =new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.200.132.20:801/eportal/portal/logout?user_account=drcom&user_password=123")
                            .build();
                    Response response = client.newCall(request).execute();
                    assert response.body() != null;
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
                responseText =response;
                Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
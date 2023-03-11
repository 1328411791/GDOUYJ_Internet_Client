package com.liahnu.auto_login.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.liahnu.auto_login.R;
import com.liahnu.auto_login.client.GetChallenge;
import com.liahnu.auto_login.client.domain.GetChallengeResponse;
import com.liahnu.auto_login.utilliiy.GetWifiInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch autologin;
    private CheckBox rememberPass;
    private String responseText;
    private CheckBox loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = findViewById(R.id.User);
        passwordEdit = findViewById(R.id.Password);
        rememberPass = findViewById(R.id.remember_pass);
        autologin = findViewById(R.id.Auto_Login);
        loginPass = findViewById(R.id.auto_login);
        Button button1 = findViewById(R.id.button_login);
        Button button_logout = findViewById(R.id.button_logout);
        boolean isAutoLogin = pref.getBoolean("auto_login", false);
        boolean isRemember = pref.getBoolean("remember_password", false);
        Toast.makeText(MainActivity.this, GetWifiInfo.getSsid(getApplicationContext()),
                Toast.LENGTH_SHORT).show();

        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        if (isAutoLogin) {
            loginPass.setChecked(true);
            sendLogin();
        }
        button1.setOnClickListener(view -> {
            //判断按钮代码
            //Toast.makeText(MainActivity.this,"Login",Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "Click Save");
            String account = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            sendLogin();
            editor = pref.edit();
            if (loginPass.isChecked()) {
                editor.putBoolean("auto_login", true);
            } else {
                editor.putBoolean("auto_login", false);
            }
            if (rememberPass.isChecked()) {
                editor.putBoolean("remember_password", true);
                editor.putString("account", account);
                editor.putString("password", password);
            } else {
                editor.clear();
            }
            editor.apply();

        });

        button_logout.setOnClickListener(view -> {
            Log.d("MainActivity", "Click Logout");
            sendLogout();
        });

        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Setting_item:
                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_setting);
                break;
            case R.id.WebView:
                Intent intent_web = new Intent(MainActivity.this, WebView.class);
                startActivity(intent_web);
                break;
            case R.id.internet:
                Toast.makeText(this, "internet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
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

    private void sendLogin() {
        new Thread(() -> {
            try {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String response = GetChallenge.getChallenge();
                showResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendLogout() {
        new Thread(() -> {
            try {
                String response = GetChallenge.getChallenge();
                showResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    private void showResponse(final String response) {
        runOnUiThread(() -> {
            responseText = response;
            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
        });
    }


}
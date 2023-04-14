package com.liahnu.auto_login.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.liahnu.auto_login.R;
import com.liahnu.auto_login.client.QueryAcidClient;
import com.liahnu.auto_login.domain.Config;
import com.liahnu.auto_login.domain.User;
import com.liahnu.auto_login.utilliiy.copyElfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;

    private TextView infoTextView;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch autologin;
    private CheckBox rememberPass;
    private String responseText;
    private CheckBox loginPass;

    private copyElfs ce;

    private Config config;

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
        infoTextView = findViewById(R.id.info);
        Button button1 = findViewById(R.id.button_login);
        Button button_logout = findViewById(R.id.button_logout);
        boolean isAutoLogin = pref.getBoolean("auto_login", false);
        boolean isRemember = pref.getBoolean("remember_password", false);

        // 初始化文件
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            ce = new copyElfs(getBaseContext());
        }

        if(ce!=null) {
            ce.copyAll2Data();
            config = ce.readConfig();
            accountEdit.setText(config.getUsers().get(0).getUsername());
            passwordEdit.setText(config.getUsers().get(0).getPassword());
        }else{
            showMessage("初始化失败");
            Log.e(TAG, "获取文件失败");
        }

        if (isRemember) {
            rememberPass.setChecked(true);
        }

        if (isAutoLogin) {
            loginPass.setChecked(true);
            callAccount(true);
            Toast.makeText(MainActivity.this, "开始自动登录...", Toast.LENGTH_SHORT).show();
        }
        button1.setOnClickListener(view -> {
            //判断按钮代码
            Log.d(TAG ,"Click Login");

            String username = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            editor = pref.edit();
            editor.putBoolean("auto_login", loginPass.isChecked());

            // 保存用户信息
            editor.putBoolean("remember_password", true);
            // 保存用户信息
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            config.getUsers().set(0,user);
            ce.updateConfig(config);

            editor.apply();

            if(config.getUsers().get(0).getUsername()!=null||config.getUsers().get(0).getPassword()!=null) {
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                callAccount(true);
            } else{
                Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }

        });

        button_logout.setOnClickListener(view -> {
            Log.d(TAG, "Click Logout");
            callAccount(false);
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

    private void showMessage(String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,message);
                infoTextView.setText(message);
            }
        });
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
            case R.id.Exit_item:
                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
        }
        return true;
    }

    // 1登录 0注销
    private void callAccount(boolean status) {
        new  Thread(new Runnable() {
            @Override
            public void run() {
                Process p;
                String tmptext;
                String cmd = "srun";
                String ConfigPath = "config.json";
                String statusStr = status ? "login" : "logout";
                StringBuilder execreSult = new StringBuilder();
                String path = "";

                if (statusStr.equals("login")) {
                    path = "/system/bin/linker64 " + ce.getExecutableFilePath() + "/" + cmd + " login"
                            + " -c " + ce.getExecutableFilePath() + "/" + ConfigPath;
                } else {
                    path = "/system/bin/linker64 " + ce.getExecutableFilePath() + "/" + cmd + " logout -u "
                            + config.getUsers().get(0).getUsername() + " -d -s " + config.getServer();
                }

                Log.i(TAG, path);

                if (statusStr.equals("login")) {
                    Integer acidresult;
                    acidresult = QueryAcidClient.queryAcid();
                    if (acidresult != -1){
                        config.setAcid(acidresult);
                        ce.updateConfig(config);
                        Log.i(TAG,"Set acid " + config.getAcid());
                    }
                }

                try {
                    p = Runtime.getRuntime().exec(path);
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    while ((tmptext = br.readLine()) != null) {
                        execreSult.append(tmptext).append("\n");
                    }
                } catch (IOException e) {
                    Log.i(TAG, e.toString());
                }
                Log.i(TAG, execreSult.toString());
                showMessage(execreSult.toString());
            }


        }).start();
    }

}
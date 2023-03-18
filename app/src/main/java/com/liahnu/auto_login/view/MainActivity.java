package com.liahnu.auto_login.view;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.liahnu.auto_login.R;
import com.liahnu.auto_login.domain.Config;
import com.liahnu.auto_login.domain.User;
import com.liahnu.auto_login.utilliiy.copyElfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.*;
import java.util.Map;
import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
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
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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
            String s = callAccount(true);
            showMessage(s);
        }
        button1.setOnClickListener(view -> {
            //判断按钮代码
            Log.d(TAG ,"Click Login");

            String username = accountEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            editor = pref.edit();
            editor.putBoolean("auto_login", loginPass.isChecked());

            if (rememberPass.isChecked()) {
                editor.putBoolean("remember_password", true);
                // 保存用户信息
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                config.getUsers().set(0,user);
                ce.updateConfig(config);
            } else {
                editor.clear();
            }
            editor.apply();

            if(config.getUsers().get(0).getUsername()!=null||config.getUsers().get(0).getPassword()!=null) {
                showMessage("保存成功");
                String s = callAccount(true);
                showMessage(s);
            } else{
                showMessage("保存失败");
            }

        });

        button_logout.setOnClickListener(view -> {
            Log.d(TAG, "Click Logout");
            String s = callAccount(false);
            showMessage(s);
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
        TextView infoText = findViewById(R.id.info);
        infoText.setText(message);
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
    public String callAccount(boolean status){
        Process p;
        String tmptext;
        String cmd = "srun";
        String ConfigPath = "config.json";
        String statusStr = status?"login":"logout";
        StringBuilder execresult = new StringBuilder();
        String path = "";

        if (statusStr.equals("login")) {
            path = "/system/bin/linker64 " + ce.getExecutableFilePath() + "/" + cmd + " login"
                    + " -c " + ce.getExecutableFilePath() + "/" + ConfigPath;
        }
        else{
            path = "/system/bin/linker64 "+ce.getExecutableFilePath() + "/"+ cmd +" logout -u "
                    + config.getUsers().get(0).getUsername() + " -d -s http://10.129.1.1";
        }

        Log.i(TAG,path);
        try {
            getAcid();
            p = Runtime.getRuntime().exec(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((tmptext = br.readLine()) != null) {
                execresult.append(tmptext).append("\n");
            }
        }catch (IOException e){
            Log.i(TAG,e.toString());
        }
        Log.i(TAG,execresult.toString());
        return execresult.toString();
    }

    public String getAcid(){
        String acid;
        String execresult;
        String jsonpath = ce.getExecutableFilePath() + "/config.json";
        ObjectMapper objectMapper = new ObjectMapper();

        // 读取JSON文件并转换为Map对象
        Map<String, Object> data = null;
        try {
            data = objectMapper.readValue(new File(jsonpath), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://connect.rom.miui.com/generate_204")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.5563.65 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Connection", "close")
                .build();

        try{
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                String meta_refresh_url = response.body().string().split("URL=")[1].split("\"")[0];
                Request redirectRequest = new Request.Builder().url(meta_refresh_url).build();
                Response redirectResponse = client.newCall(redirectRequest).execute();
                String redirected_url = redirectResponse.request().url().toString();
                Pattern pattern = Pattern.compile("ac_id=\\d+");
                Matcher matcher = pattern.matcher(redirected_url);
                if (matcher.find()) {
                    acid = matcher.group().split("=")[1];
                    data.put("acid", Integer.parseInt(acid));
                    execresult = "Refresh! Ac_id is " + acid;

                }else{
                    execresult = "Could not find ac_id in the url, you may be redirected into a wrong page";
                }
            } else {
                execresult = "Connected to the network with the status code " + response.code();
            }
        }catch (IOException e) {
            e.printStackTrace();
            execresult = "Failed to connected to network.";
        }

        // 将修改后的Map对象转换回JSON格式并写入文件
        try {
            objectMapper.writeValue(new File(jsonpath), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return execresult; //需要放入运行结果
    }
}
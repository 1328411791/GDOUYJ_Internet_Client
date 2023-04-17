package com.liahnu.auto_login.view;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.liahnu.auto_login.R;
import com.liahnu.auto_login.domain.Config;
import com.liahnu.auto_login.execption.VersionException;
import com.liahnu.auto_login.utilliiy.CheckUpdate;
import com.liahnu.auto_login.utilliiy.copyElfs;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch strictBind ;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch  doubleStack;

    private Button checkUpdate;

    private Button saveConfig;

    private TextView server;

    private TextView acId;

    private copyElfs ce;
    private Config config;


    @SuppressLint("SetTextI18n")
    private void setTextView(){
        if(config==null){
            showResponse("获取配置文件失败");
            return;
        }
        server.setText(config.getServer());
        acId.setText(config.getAcid().toString());
    }

    private void initConfig(){
        doubleStack =findViewById(R.id.double_stack);
        strictBind =findViewById(R.id.strict_bind);
        checkUpdate = findViewById(R.id.check_update);
        server = findViewById(R.id.server);
        acId = findViewById(R.id.ac_id);
        saveConfig = findViewById(R.id.save_config);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ce = new copyElfs(getBaseContext());
        }

        if(ce!=null) {
            config = ce.readConfig();
        }else{
            Log.e(TAG, "获取文件失败");
        }
        setTextView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initConfig();


        checkUpdate.setOnClickListener(view -> {
            Log.i(TAG, "检查更新");
            checkUpdate();
        });

        saveConfig.setOnClickListener(view -> {
            Log.i(TAG, "保存配置");
            saveConfig();
        });



    }

    private void saveConfig() {
        config.setAcid(Integer.parseInt(acId.getText().toString()));
        config.setServer(server.getText().toString());
        Log.i(TAG, config.toString());
        ce.updateConfig(config);
    }

    private void checkUpdate(){
        new  Thread(new Runnable() {
            @Override
            public void run() {
                CheckUpdate checkUpdate = new CheckUpdate(getBaseContext());
                try {
                    String downloadURL = checkUpdate.checkUpdate4Github();
                    startBrowser(downloadURL);
                } catch (PackageManager.NameNotFoundException | IOException e) {
                    Log.e(TAG, "检查更新失败");
                    showResponse("检查更新失败,请检查网络连接");
                } catch (VersionException e) {
                    Log.e(TAG, "已经是最新版本");
                    showResponse("已经是最新版本，目前版本为"+e.getNowVersion()+"，最新版本为"+e.getNewVersion());
                }
            }
        }).start();
    }


    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SettingActivity.this, response, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startBrowser(final String url){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, url);
                Toast.makeText(SettingActivity.this, "跳转到浏览器", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }


}

package com.liahnu.auto_login.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.liahnu.auto_login.R;
import com.liahnu.auto_login.domain.Config;
import com.liahnu.auto_login.jobs.CheckUpdateJob;
import com.liahnu.auto_login.utilliiy.copyElfs;

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
    private TextView tryTimes;
    private TextView tryTimeout;
    private copyElfs ce;
    private Config config;


    @SuppressLint("SetTextI18n")
    private void setTextView(){
        if(config==null){
            Log.e(TAG, "config is null");
            Toast.makeText(this, "获取配置失败", Toast.LENGTH_SHORT).show();
            return;
        }
        server.setText(config.getServer());
        acId.setText(config.getAcid().toString());
        tryTimes.setText(config.getRetry_times().toString());
        tryTimeout.setText(config.getRetry_delay().toString());
    }

    private void initConfig(){
        doubleStack =findViewById(R.id.double_stack);
        strictBind =findViewById(R.id.strict_bind);
        checkUpdate = findViewById(R.id.check_update);
        server = findViewById(R.id.server);
        acId = findViewById(R.id.ac_id);
        saveConfig = findViewById(R.id.save_config);
        tryTimeout = findViewById(R.id.login_timeout);
        tryTimes = findViewById(R.id.try_times);
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
        config.setRetry_times(Integer.parseInt(tryTimes.getText().toString()));
        config.setRetry_delay(Integer.parseInt(tryTimeout.getText().toString()));
        Log.i(TAG, config.toString());
        ce.updateConfig(config);
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    private void checkUpdate() {
        CheckUpdateJob job = new CheckUpdateJob(this);
        job.start();
    }


}

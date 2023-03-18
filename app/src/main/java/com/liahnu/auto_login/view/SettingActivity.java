package com.liahnu.auto_login.view;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.liahnu.auto_login.R;
import com.liahnu.auto_login.domain.Config;
import com.liahnu.auto_login.utilliiy.CheckUpdate;
import com.liahnu.auto_login.utilliiy.copyElfs;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch strictBind =findViewById(R.id.double_stack);

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch  double_stack =findViewById(R.id.strict_bind);;

    private Button checkUpdate = findViewById(R.id.check_update);

    private copyElfs ce;
    private Config config;

    private void initConfig(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ce = new copyElfs(getBaseContext());
        }

        if(ce!=null) {
            config = ce.readConfig();
        }else{
            Log.e(TAG, "获取文件失败");
        }
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




    }


    public void checkUpdate(){
        // 检查更新
        CheckUpdate checkUpdate = new CheckUpdate(getBaseContext());
        try {
            String s = checkUpdate.checkUpdate4Github();
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
        } catch (PackageManager.NameNotFoundException | IOException e) {
            Log.e(TAG,"检查更新失败");
            throw new RuntimeException(e);
        }
    }

}

package com.liahnu.auto_login.utilliiy;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.liahnu.auto_login.domain.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONParser;
import cn.hutool.json.JSONUtil;

public class copyElfs {
    String tag="StackOF:";
    Context ct;
    String appFileDirectory, executableFilePath;
    AssetManager assetManager;
    List resList;
    String cpuType;
    String[] assetsFiles ={
            "srun","config.json"
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    public copyElfs(Context c){
        ct=c;

        //        appFileDirectory = ct.getFilesDir().getPath();
        appFileDirectory = "/data/data/"+ct.getPackageName();

        executableFilePath = appFileDirectory + "/executable";
        Log.e(tag, "cpu type:"+ cpuType);
        //        cputype = Build.SUPPORTED_ABIS[0];
        cpuType = Build.CPU_ABI;
        Log.e(tag, "cpu type:"+ cpuType);
        assetManager = ct.getAssets();
        try {
            resList = Arrays.asList(ct.getAssets().list(cpuType +"/"));
            Log.d(tag,"get assets list:"+ resList.toString());
        } catch (IOException e){
            Log.e(tag, "error list assets folder:", e);
        }
    }
    boolean resFileExist(String filename){
        File f=new File(executableFilePath +"/"+filename);
        if (f.exists())
            return true;
        return false;
    }
    public void copyFile(InputStream in, OutputStream out){
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e){
            Log.e(tag, "failed to read/write asset file: ", e);
        }
    };
    private void copyAssets(String filename) {
        InputStream in = null;
        OutputStream out = null;
        Log.d(tag, "attempting to copy this file: " + filename);

        try {
            in = assetManager.open(cpuType +"/"+filename);
            File outfile = new File(executableFilePath, filename);
            out = new FileOutputStream(outfile);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(IOException e) {
            Log.e(tag, "failed to copy asset file: " + filename, e);
        }
        Log.d(tag, "copy success: " + filename);
    }
    public void copyAll2Data(){

        Log.d(tag,"copyAll2Data() called");
        int i;

        File folder=new File(executableFilePath);
        if (!folder.exists()){
            folder.mkdir();
        }
        // 复制文件
        for(i=0; i< assetsFiles.length; i++){
            if (!resFileExist(assetsFiles[i])){
                copyAssets(assetsFiles[i]);
                File execfile = new File(executableFilePath +"/"+ assetsFiles[i]);
                execfile.setExecutable(true);
            }
        }
    }

    public String getExecutableFilePath(){
        return executableFilePath;
    }

    public Config readConfig(){
        String configPath = executableFilePath + "/config.json";
        File configFile = new File(configPath);
        if (configFile.exists()){
            JSON json = JSONUtil.readJSON(configFile, StandardCharsets.UTF_8);
            Log.d(tag,json.toStringPretty());
            // 讲json转换为对象
            return new Gson().fromJson(json.toString(), Config.class);
        }
        return null;
    }


    // 写入config文件
    public void updateConfig(Config config){
        String configPath = executableFilePath + "/config.json";
        File configFile = new File(configPath);
        // 按字符串覆盖写入
        String json = new Gson().toJson(config);
        Log.d(tag, "json string: " + json);

        FileUtil.writeString(json, configPath, "UTF-8");

    }
}

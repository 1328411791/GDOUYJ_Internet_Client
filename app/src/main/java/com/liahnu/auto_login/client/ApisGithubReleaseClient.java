package com.liahnu.auto_login.client;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApisGithubReleaseClient {

    public static String getReleaseVersion() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/repos/1328411791/GDOUYJ_Internet_Client/releases/latest")
                .build();

        Response resp = client.newCall(request).execute();

        if (resp.body() ==null){
            return null;
        }

        String respBody = resp.body().string();

        JSONObject entries = JSONUtil.parseObj(respBody);

        return Objects.requireNonNull(entries.get("tag_name")).toString();
    }
}

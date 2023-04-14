package com.liahnu.auto_login.client;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import cn.hutool.core.util.StrUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QueryAcidClient {
    public static Integer queryAcid() {
        String id = null;
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://8.8.8.8").get().build();
            Response response = client.newCall(request).execute();
            String url = response.request().url().toString();
            Uri uri = Uri.parse(url);
            id = uri.getQueryParameter("ac_id");
            if (StrUtil.isBlank(id)) {
                String body = response.body().string();
                int begin = body.indexOf("/index_") + 7;
                int end = body.indexOf(".html", begin);
                id = body.substring(begin, end);
            }
            //if (!StrUtil.isBlank(id)) {
            //    config.setAcid(Integer.parseInt(id));
            //}
        } catch (IOException | NullPointerException e) {
            Log.i("QueryAcidClient", e.toString());
        } catch (NumberFormatException e) {}

        return (!StrUtil.isBlank(id))?Integer.parseInt(id):-1;
    }
}

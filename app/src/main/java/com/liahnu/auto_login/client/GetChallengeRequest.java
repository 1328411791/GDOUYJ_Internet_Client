package com.liahnu.auto_login.client;

import android.util.Log;

import com.liahnu.auto_login.client.domain.GetChallengeResponse;
import com.liahnu.auto_login.client.util.jQuerySerialization;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetChallengeRequest {

    private static final String TAG = "GetChallengeRequest";

    /*
    GET /cgi-bin/get_challenge?callback=jQuery1124038630846526307305_1678337618278&username=2021440113**&ip=10.202.47.26&_=1678337618280
     */

    public static GetChallengeResponse getChallenge(String username,String ip){
        OkHttpClient client = new OkHttpClient();
        String nowTime = String.valueOf(System.currentTimeMillis());

        // 构建HttpUrl
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("10.129.1.1")
                .addPathSegment("cgi-bin")
                .addPathSegment("get_challenge")
                .addQueryParameter("callback", "jQuery1124038630846526307305_"+nowTime)
                .addQueryParameter("username", username)
                .addQueryParameter("ip", ip)
                .addQueryParameter("_", nowTime )
                .build();

        Log.d("GetChallenge",url.toString());

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        // 发送请求
        try {
            Response response = client.newCall(request).execute();

            if(response.body()==null){
                return null;
            }

            String json = jQuerySerialization.getQueryString(response.body().string());
            GetChallengeResponse challengeResponse = JSONUtil.toBean(json, GetChallengeResponse.class);
            Log.i(TAG, "GetChallengeRequest: " + challengeResponse.toString());
            return challengeResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

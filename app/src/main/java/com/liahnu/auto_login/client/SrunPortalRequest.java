package com.liahnu.auto_login.client;

import android.util.Log;

import com.liahnu.auto_login.client.domain.Config;
import com.liahnu.auto_login.client.domain.SurnPortalRespond;
import com.liahnu.auto_login.client.domain.User;
import com.liahnu.auto_login.client.util.jQuerySerialization;
import com.liahnu.auto_login.client.util.md5Util;
import com.liahnu.auto_login.client.util.sha1Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.hutool.json.JSONUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SrunPortalRequest {

    private static final String TAG = "SrunPortalRequest";

    /*
    * GET /cgi-bin/srun_portal?callback=jQuery1124038630846526307305_1678337618278&
    * action=login&username=2021440113**&password=%7BMD5%7D01d38ef19265ae9ff199cbafc98e6797&
    * os=Windows+95&name=Windows&double_stack=0&
    * chksum=ffbaed27a7cbf48bcf7c227beae802def6f96901&
    * info=%7BSRBX1%7DQJl6uFy9IkM3%2FI3HaaNP%2FgPkfZWn%2FLUH3ktAZuPhtrF%2BailDbmoRB7oaw4jfeE%2FKR9hTkSoyJ5de6TXw6IpyX8VGiLpiWy6MLBEfu4RdCyg2WSim%2FmIssMlh9cfzJygmNCSiFDNbWiE6uBnn
    * &ac_id=1&ip=10.202.47.26&n=200&type=1&_=1678337618281
    * */

    public static SurnPortalRespond SurnPortalRespond(Config config, User user,String token) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = buildUrl(config,user,token);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() == null) {
            return null;
        }

        String json = jQuerySerialization.getQueryString(response.body().string());

        SurnPortalRespond surnPortalRespond = JSONUtil.toBean(json, SurnPortalRespond.class);

        Log.i(TAG, "getCSrunPortalRequest: " + surnPortalRespond.toString());

        return surnPortalRespond;

    }

    private static HttpUrl buildUrl(Config config, User user,String token) {

        // 获取基础信息
        String nowTime = String.valueOf(System.currentTimeMillis());
        String md5 = md5Util.getMD5(user.getPassword(),token);
        String password = "%7BMD5%7D" + md5Util.getMD5(user.getPassword(),token);
        String chksum = sha1Util.getShkSum(config, user,token,md5);

        return new HttpUrl.Builder()
                .scheme("http")
                .host("10.129.1.1")
                .addPathSegment("cgi-bin")
                .addPathSegment("srun_portal")
                .addQueryParameter("callback", "jQuery1124038630846526307305_" + nowTime)
                .addQueryParameter("action", "login")
                .addQueryParameter("username", user.getUsername())
                .addQueryParameter("password", password)
                .addQueryParameter("os", config.getOs())
                .addQueryParameter("name", config.getName())
                .addQueryParameter("double_stack", String.valueOf(config.getDouble_stack()))
                .addQueryParameter("chksum", chksum)
                .addQueryParameter("info", "%7BSRBX1%7DQJl6uFy9IkM3%2FI3HaaNP%2FgPkfZWn%2FLUH3ktAZuPhtrF%2BailDbmoRB7oaw4jfeE%2FKR9hTkSoyJ5de6TXw6IpyX8VGiLpiWy6MLBEfu4RdCyg2WSim%2FmIssMlh9cfzJygmNCSiFDNbWiE6uBnn")
                .addQueryParameter("ac_id", "1")
                .addQueryParameter("ip", "")
                .addQueryParameter("n", "200")
                .addQueryParameter("type", "1")
                .addQueryParameter("_", nowTime)
                .build();
    }
}

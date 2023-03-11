package com.liahnu.auto_login.client.util;

import android.util.Log;

import com.liahnu.auto_login.client.domain.Config;
import com.liahnu.auto_login.client.domain.User;

import cn.hutool.crypto.digest.DigestUtil;

public class sha1Util {

    private static final String TAG = "sha1Util";

    public static String getShkSum(Config config, User user,String token,
                             String md5) {
        String str = token + user.getUsername();
        str += token + md5;
        str += token + config.getAcid();
        str += token + config.getIp();
        str += token + config.getN();
        str += token + config.getType();
        str += token + config.getI();

        String s = sha1(str);
        Log.i(TAG, "getShkSum: " + s);
        return s;
    }

    public static String sha1(String check_sum) {
        return DigestUtil.sha1Hex(check_sum);
    }
}

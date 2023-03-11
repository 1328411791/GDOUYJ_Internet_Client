package com.liahnu.auto_login.client.util;

import java.security.MessageDigest;
import java.util.Base64;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

public class md5Util {

    public static String getMD5(String password,String token){

        HMac hmac = new HMac(HmacAlgorithm.HmacMD5, token.getBytes());
        byte[] result = hmac.digest(password.getBytes());

        return  HexUtil.encodeHexStr(result);
    }
}

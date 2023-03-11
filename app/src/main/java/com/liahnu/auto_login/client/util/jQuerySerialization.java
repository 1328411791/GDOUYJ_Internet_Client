package com.liahnu.auto_login.client.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class jQuerySerialization {

    private static final String TAG = "jQuerySerialization";

    //jQuery1124038630846526307305_1678526127654({"challenge":"b0887b05028aede5f9b1d6e2ae1b4ce4b8fa289ff4133120094ffaf21d3db558","client_ip":"10.200.149.60","ecode":0,"error":"ok","error_msg":"","expire":"60","online_ip":"10.202.47.26","res":"ok","srun_ver":"SRunCGIAuthIntfSvr V1.18 B20220304","st":1678526126})

    public static String getQueryString(String jQuery) {
        int start = jQuery.indexOf("(");
        int end = jQuery.lastIndexOf(")");

        String json = jQuery.substring(start + 1, end);

        Log.d(TAG, "getQueryString: " + json);

        return json;
    }
}

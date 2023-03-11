package com.liahnu.auto_login.client.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class SurnPortalRespond {

    //jQuery112405957177920927834_1678520694892({"client_ip":"10.200.149.60","ecode":"","error":"login_error","error_msg":"INFO failed, BAS respond timeout.","online_ip":"10.202.46.12","res":"login_error","srun_ver":"SRunCGIAuthIntfSvr V1.18 B20220304","st":1678520791})

    private String client_ip;
    private String ecode;
    private String error;
    private String error_msg;
    private String online_ip;
    private String res;
    private String srun_ver;
    private int st;



    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getOnline_ip() {
        return online_ip;
    }

    public void setOnline_ip(String online_ip) {
        this.online_ip = online_ip;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getSrun_ver() {
        return srun_ver;
    }

    public void setSrun_ver(String srun_ver) {
        this.srun_ver = srun_ver;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }
}

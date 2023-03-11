package com.liahnu.auto_login.client.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class GetChallengeResponse {

    // jQuery112405957177920927834_1678520694892({"challenge":"19c6a00d0d6b719d4169967a5869b92778b8c97d520fff758d6f086148f1be73","client_ip":"10.200.149.60","ecode":0,"error":"ok","error_msg":"","expire":"60","online_ip":"10.202.46.12","res":"ok","srun_ver":"SRunCGIAuthIntfSvr V1.18 B20220304","st":1678520788})

    private String challenge;
    private String client_ip;
    private int ecode;
    private String error;
    private String error_msg;
    private String expire;
    private String online_ip;
    private String res;
    private String srun_ver;
    private int st;

    public GetChallengeResponse(){

    }

    public GetChallengeResponse(JSONObject jsonObject) throws JSONException {
        this.challenge = jsonObject.getString("challenge");
        this.client_ip = jsonObject.getString("client_ip");
        this.ecode = jsonObject.getInt("ecode");
        this.error = jsonObject.getString("error");
        this.error_msg = jsonObject.getString("error_msg");
        this.expire = jsonObject.getString("expire");
        this.online_ip = jsonObject.getString("online_ip");
        this.res = jsonObject.getString("res");
        this.srun_ver = jsonObject.getString("srun_ver");
        this.st = jsonObject.getInt("st");
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public int getEcode() {
        return ecode;
    }

    public void setEcode(int ecode) {
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

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
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

package com.example.administrator.quantaproject.net;

import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/18.
 */

public class Login {
    public Login(String phoneNum, String password,final SuccessCallback successCallback,final FailCallback failCallback) {
        new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    switch (jsonObject.getInt(PingTai_Config.KEY_STATUS)){
                        case PingTai_Config.RESULT_STATUS_SUCCESS:
                            if(successCallback!=null){
                                successCallback.onSuccess(jsonObject.getString(PingTai_Config.KEY_TOKEN));
                            }
                            break;
                        default:
                            if(failCallback!=null){
                                failCallback.onFail();
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(failCallback!=null){
                        failCallback.onFail();
                    }
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        }, PingTai_Config.KEY_ACTION,PingTai_Config.ACTION_LOGIN,
                PingTai_Config.KEY_PHONE_NUM,phoneNum,PingTai_Config.KEY_PASSWORD,password);
    }

    public static interface SuccessCallback{
        void onSuccess(String token);
    }

    public static interface FailCallback{
        void onFail();
    }
}

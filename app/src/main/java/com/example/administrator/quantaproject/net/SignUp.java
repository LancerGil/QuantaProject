package com.example.administrator.quantaproject.net;

import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/20.
 */

public class SignUp {
    public SignUp(String code, String phoneNum,String city,String password,String studNum,String realName, final SuccessCallback successCallback, final FailCallback failCallback){
        new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.GET,new NetConnection.SuccessCallback() {
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
        }, PingTai_Config.ACTION_SIGNUP,
                PingTai_Config.KEY_PHONE_NUM,phoneNum,
                PingTai_Config.KEY_CODE,code,
                PingTai_Config.KEY_STUDNUM,studNum,
                PingTai_Config.KEY_PASSWORD,password,
                PingTai_Config.KEY_REALNAME,realName,
                PingTai_Config.KEY_CITY,city);
    }

    public static interface SuccessCallback{
        void onSuccess(String token);
    }

    public static interface FailCallback{
        void onFail();
    }
}

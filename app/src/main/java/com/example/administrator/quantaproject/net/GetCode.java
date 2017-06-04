package com.example.administrator.quantaproject.net;

import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/17.
 */

public class GetCode {
    public GetCode(String phoneNum, final SuccessCallback successCallback, final FailCallback failCallback){

        NetConnection netConnection = new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    switch (jsonObject.getInt(PingTai_Config.KEY_STATUS)){
                        case PingTai_Config.RESULT_STATUS_SUCCESS:
                            if(successCallback!=null){
                                successCallback.onSuccess();
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
                if(failCallback!=null){
                    failCallback.onFail();
                }
            }
        },PingTai_Config.KEY_PHONE_NUM,phoneNum);

    }

    public static interface SuccessCallback{
        void onSuccess();
    }

    public static interface FailCallback{
        void onFail();
    }
}

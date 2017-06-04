package com.example.administrator.quantaproject.net;

import android.content.Context;

import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class PersonalInfo {
    private List<String> personalInfo = new ArrayList<>();

    public PersonalInfo(Context context, final String phoneNum, final SuccessCallback successCallback, final FailCallback failCallback){
        new NetConnection(PingTai_Config.SERVER_URL_LOCAL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectResult = new JSONObject(result);

                    int status = jsonObjectResult.getInt(PingTai_Config.KEY_STATUS);
                    switch (status){
                        case PingTai_Config.RESULT_STATUS_SUCCESS:
                            if (successCallback != null){
                                personalInfo.add(jsonObjectResult.getString(PingTai_Config.KEY_HEAD_IMAGEURL));
                                personalInfo.add(jsonObjectResult.getString(PingTai_Config.KEY_PHONE_NUM));
                                personalInfo.add(jsonObjectResult.getString(PingTai_Config.KEY_SEX));
                                personalInfo.add(jsonObjectResult.getString(PingTai_Config.KEY_BIRTHDAY));
                                personalInfo.add(jsonObjectResult.getString(PingTai_Config.KEY_NICKNAME));
                                successCallback.onSuccess(personalInfo);
                            }
                            break;
                        default:
                            if (failCallback!=null){
                                failCallback.onFail();
                            }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {

            }
        },PingTai_Config.KEY_ACTION,PingTai_Config.KEY_PERSONALINFO,
                PingTai_Config.KEY_PHONE_NUM,phoneNum);
    }

    public static interface SuccessCallback{
        void onSuccess(List<String> result);
    }

    public static interface FailCallback{
        void onFail();
    }
}

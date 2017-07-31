package com.example.administrator.quantaproject.net;

import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/4.
 */

public class PutComment {
    public PutComment(String phoneNum, String token, String comMovementId, String comContent,
                      String comTime, final SuccessCallback successcallback, final FailCallback failCallback){
        new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json_result = new JSONObject(result);
                    int status = json_result.getInt(PingTai_Config.KEY_STATUS);
                    switch (status) {
                        case PingTai_Config.RESULT_STATUS_SUCCESS:
                            if (successcallback!=null){
                                successcallback.onSuccess();
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
                if (failCallback!=null){
                    failCallback.onFail();
                }
            }
        },PingTai_Config.KEY_ACTION,PingTai_Config.ACTION_PUTCOMMENT,
                PingTai_Config.KEY_PHONE_NUM,phoneNum,
                PingTai_Config.KEY_TOKEN,token,
                PingTai_Config.KEY_COMMENT_MOVEMENTID,comMovementId,
                PingTai_Config.KEY_COMMENT_CONTENT,comContent);

    }

    public static interface SuccessCallback{
        void onSuccess();
    }

    public static interface FailCallback{
        void onFail();
    }
}

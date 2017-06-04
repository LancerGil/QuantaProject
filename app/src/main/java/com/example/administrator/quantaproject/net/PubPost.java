package com.example.administrator.quantaproject.net;

import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/5/29.
 */

public class PubPost {

    public PubPost(String action, final String phoneNum,String token ,String title, String content,String categorySelected, final SuccessCallback successCallback, final FailCallback failCallback){


        try {
            new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        switch (jsonObject.getInt(PingTai_Config.KEY_STATUS)){
                            case PingTai_Config.RESULT_STATUS_SUCCESS:
                                if(successCallback!=null){
//                                    for (int i = 0; i < pubImage.size() ; i++) {
//                                        UploadUtil.uploadFile(pubImage.get(i), PingTai_Config.SERVER_URL + "/addFace",phoneNum);
//                                    }
                                    successCallback.onSuccess(jsonObject.getInt(PingTai_Config.KEY_MOVEMENT_ID));
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
            },action,
                    PingTai_Config.KEY_PHONE_NUM,phoneNum,
                    PingTai_Config.KEY_TOKEN,token,
                    PingTai_Config.KEY_PUBTITLE,title,
                    PingTai_Config.KEY_CATEGORY,categorySelected,
                    PingTai_Config.KEY_PUBCONTENT, URLEncoder.encode(content,"utf-8")
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static interface SuccessCallback{
        void onSuccess(int movementId);
    }

    public static interface FailCallback{
        void onFail();
    }


}

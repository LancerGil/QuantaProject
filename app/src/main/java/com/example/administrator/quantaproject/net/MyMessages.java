package com.example.administrator.quantaproject.net;

import android.content.Context;

import com.example.administrator.quantaproject.data.MyMessage;
import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class MyMessages {
    private List<MyMessage> myMessageList = new ArrayList<>();

    public MyMessages(Context context, String phoneNum, String token, final SuccessCallback successCallback, final FailCallback failCallback){
        new NetConnection(PingTai_Config.SERVER_URL, HttpMethod.GET, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectResult = new JSONObject(result);

                    int status = jsonObjectResult.getInt(PingTai_Config.KEY_STATUS);
                    switch (status){
                        case PingTai_Config.RESULT_STATUS_SUCCESS:
                            if(successCallback!=null){
                                JSONArray messages = jsonObjectResult.getJSONArray(PingTai_Config.KEY_MESSAGES);

                                for (int i = 0 ; i < messages.length();i++){
                                    JSONObject message = messages.getJSONObject(i);
                                    myMessageList.add(new MyMessage(
                                            message.getString(PingTai_Config.KEY_MESSAGE_CHATWIN_IMAGEURL),
                                            message.getString(PingTai_Config.KEY_MESSAGE_CHATWIN),
                                            message.getString(PingTai_Config.KEY_MESSAGE_LASTSENDER),
                                            message.getString(PingTai_Config.KEY_MESSAGE_MESSAGECON)));
                                }
                                successCallback.onSuccess(myMessageList);
                            }
                            break;
                        default:
                            if (failCallback!=null) {
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
                if(failCallback!=null){
                    failCallback.onFail();
                }
            }
        },PingTai_Config.KEY_ACTION,PingTai_Config.ACTION_MYMESSAGE,
                PingTai_Config.KEY_PHONE_NUM,phoneNum,
                PingTai_Config.KEY_TOKEN,token);
    }

    public static interface SuccessCallback{
        void onSuccess(List<MyMessage> myMessageList);
    }

    public static interface FailCallback{
        void onFail();
    }
}

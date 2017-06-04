package com.example.administrator.quantaproject.net;

import android.content.Context;
import android.util.Log;

import com.example.administrator.quantaproject.data.Movements;
import com.example.administrator.quantaproject.data.PingTai_Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movement {

    private Context context = null;

    public Movement(final Context context, String typeOfMovement , final String phoneNum, int page, int perpage, final SuccessCallback successCallback, final FailCallback failCallback){

        Log.i("Movement","running");

        this.context = context;
        String  token = PingTai_Config.getCachedToken(context);

        new NetConnection(PingTai_Config.SERVER_URL,HttpMethod.GET, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    Log.e("JSON:",result);
                    JSONObject result_json = new JSONObject(result);

                    switch (result_json.getInt(PingTai_Config.KEY_STATUS)){
                        case PingTai_Config.RESULT_STATUS_SUCCESS:
                            if (successCallback!=null){
                                List<Movements> movements = new ArrayList<>();
                                JSONArray movementJSONArray = result_json.getJSONArray(PingTai_Config.KEY_MOVEMENT);

                                for (int i = 0;i < result_json.getInt(PingTai_Config.KEY_PERPAGE);i++){
                                    JSONObject movementObj = movementJSONArray.getJSONObject(i);
                                    Log.i("IMAGEurl",movementObj.getString(PingTai_Config.KEY_IMAGEURL));
                                    movements.add(new Movements(
                                            movementObj.getString(PingTai_Config.KEY_MOVEMENT_ID),
                                            movementObj.getString(PingTai_Config.KEY_PUBTITLE),
                                            movementObj.getString(PingTai_Config.KEY_CONTENT),
                                            movementObj.getString(PingTai_Config.KEY_PHONE_NUM),
                                            movementObj.getJSONArray(PingTai_Config.KEY_IMAGEURL),
                                            movementObj.getString(PingTai_Config.KEY_PUBUSER),
                                            movementObj.getString(PingTai_Config.KEY_VIEWTIMES),
                                            movementObj.getString(PingTai_Config.KEY_COMMENTTIMES),
                                            movementObj.getString(PingTai_Config.KEY_CATEGORY),
                                            movementObj.getString(PingTai_Config.KEY_PUBTIME),
//                                            movementObj.getString(PingTai_Config.KEY_GROUPURLS),
                                               movementObj.getInt(PingTai_Config.KEY_LIKETIMES)));
                                }

                                successCallback.onSuccess(result_json.getInt(PingTai_Config.KEY_PAGE),result_json.getInt(PingTai_Config.KEY_PERPAGE),movements);
                            }
                            break;
                        default:
                            if (failCallback!=null){
                                failCallback.onFail();
                            }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (failCallback != null) {
                        failCallback.onFail();

                    }
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                failCallback.onFail();
            }
        },
                PingTai_Config.KEY_MOVEMENT,
                PingTai_Config.KEY_CATEGORY,
                typeOfMovement
                ,PingTai_Config.KEY_PHONE_NUM,phoneNum
                ,PingTai_Config.KEY_TOKEN,token
                ,PingTai_Config.KEY_PAGE,page+""
                ,PingTai_Config.KEY_PERPAGE,perpage+"");
    }

    public static interface SuccessCallback{
        void onSuccess(int page, int perpage, List<Movements> movements);
    }

    public static interface FailCallback{
        void onFail();
    }
}

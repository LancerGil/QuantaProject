package com.example.administrator.quantaproject.net;

import android.os.AsyncTask;
import android.util.Log;

import com.example.administrator.quantaproject.data.PingTai_Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/4/16.
 */

public class NetConnection {

    public NetConnection(final String url, final HttpMethod method, final SuccessCallback successCallback, final FailCallback failCallback, final String... kvs){

        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... params) {

                Log.i("NetConnection","running");

                StringBuffer paramsStr = new StringBuffer();

                try {

                    Log.i("NetConnection","trying");

                    URL server_url = new URL(url);
                    URLConnection urlConnection = server_url.openConnection();

                    switch (method){
                        case POST:
                            Log.i("NetConnection","method_post");
                            for (int i = 0; i < kvs.length; i+=2) {
                                paramsStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
                            }
                            urlConnection.setDoOutput(true);
                            Log.i("NetConnection",paramsStr.toString());
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),PingTai_Config.CHARSET));
                            bw.write(paramsStr.toString());
                            bw.flush();
                            bw.close();
                            URL request_url = new URL(url+"?"+paramsStr.toString());
                            urlConnection = request_url.openConnection();
                            break;
                        case GET:
                            Log.i("NetConnection","method_get");
                            for (int i = 0; i < kvs.length; i+=1) {
                                paramsStr.append(kvs[i]).append("/");
                            }
                            urlConnection.setDoOutput(true);
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),PingTai_Config.CHARSET));
                            bufferedWriter.write(paramsStr.toString());
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            request_url = new URL(url+"/"+paramsStr.toString());
                            urlConnection = request_url.openConnection();
                            break;
                        default:
                            break;
                    }

                    Log.i("Request url:",urlConnection.getURL().toString());
                    Log.i("Request data:" , paramsStr.toString());

                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),PingTai_Config.CHARSET));
                    Log.i("BufferedReader:" ,"defined");

                    String line = null;
                    StringBuffer result = new StringBuffer();
                    while((line = br.readLine())!=null){
                        result.append(line);
                    }
                    Log.i("Result:" , result.toString());
                    br.close();
                    return result.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    String result = "{\"status\":0}";
                    return result;
                }
            }

            @Override
            protected void onPostExecute(String result) {

                if(result != null){
                    if(successCallback!=null){
                        successCallback.onSuccess(result);
                    }
                }else {
                    failCallback.onFail();
                }

                super.onPostExecute(result);
            }
        }.execute();
    }

    public  static  interface SuccessCallback{
        void onSuccess(String result);
    }

    public static interface FailCallback{
        void onFail();
    }
}

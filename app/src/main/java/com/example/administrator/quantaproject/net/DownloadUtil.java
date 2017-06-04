package com.example.administrator.quantaproject.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.administrator.quantaproject.data.PingTai_Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Administrator on 2017/5/24.
 */

public class DownloadUtil {

    private static final String CHARSET = PingTai_Config.CHARSET; // 设置编码

    private static final String TAG = "downloadFile";


    public static Bitmap getHttpBitmap(String url) {

        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        URL myFileUrl = null;

        Bitmap bitmap = null;

        try {

            myFileUrl = new URL(url);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        }

        try {

            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setReadTimeout(10*1000);
            conn.setConnectTimeout(10*1000);
            conn.setDoInput(true); // 允许输入流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("GET"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            conn.connect();

            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            is.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return bitmap;

    }
}

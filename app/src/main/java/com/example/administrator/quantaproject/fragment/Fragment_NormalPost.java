package com.example.administrator.quantaproject.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.DownloadUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/30.
 */

public class Fragment_NormalPost extends Fragment {

    private View normalPost;
    private String movementJsonStr,title,pubUser,category,content,groupUrl;
    private JSONObject movementDetail;
    private int movementId;
    private long pubTime;
    private Bitmap bitmap;
    private String[] imageUrl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View normal_post = inflater.inflate(R.layout.fragment_normal_post,container,false);

        movementJsonStr = getActivity().getIntent().getStringExtra(PingTai_Config.KEY_MOVEMENT);
        try {
            movementDetail = new JSONObject(movementJsonStr);
            movementId = movementDetail.getInt(PingTai_Config.KEY_MOVEMENT_ID);
            title = movementDetail.getString(PingTai_Config.KEY_PUBTITLE);
            pubUser = movementDetail.getString(PingTai_Config.KEY_PUBUSER);
            pubTime = movementDetail.getLong(PingTai_Config.KEY_PUBTIME);
            category = movementDetail.getString(PingTai_Config.KEY_CATEGORY);
            imageUrl = (String[]) movementDetail.get(PingTai_Config.KEY_IMAGEURL);
            content = movementDetail.getString(PingTai_Config.KEY_PUBCONTENT);
            groupUrl = movementDetail.getString(PingTai_Config.KEY_GROUPURLS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tvTitle = (TextView) normal_post.findViewById(R.id.tv_normalPostTitle);
        tvTitle.setText(title);

        TextView tvPubUser_up = (TextView) normal_post.findViewById(R.id.tv_normalPostPubUser_upon);
        TextView tvPubUser_down = (TextView) normal_post.findViewById(R.id.tv_normalPostPubUser_blow);
        tvPubUser_up.setText(pubUser);
        tvPubUser_down.setText(pubUser);

        TextView tvPubTime = (TextView) normal_post.findViewById(R.id.tv_normalPostPubTime);

        tvPubTime.setText(pubTime+"");

        if(imageUrl!=null) {
            for (int i = 0; i < imageUrl.length; i++) {
                bitmap = DownloadUtil.getHttpBitmap(imageUrl[i]);
            }
        }
        return normal_post;
    }
}

package com.example.administrator.quantaproject.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.DownloadUtil;
import com.example.administrator.quantaproject.net.PersonalInfo;
import com.example.administrator.quantaproject.tools.BitmapUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/6/3.
 */

public class PagerFragment_Mine_PersonalInfo extends Fragment {
    private String phoneNum;
    private View PersonalInfoContainer;
    private Handler headHandler ;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PersonalInfoContainer = inflater.inflate(R.layout.fragment_mine_persona_info,container,false);

        phoneNum = PingTai_Config.getCachedPhoneNum(getContext());
        loadPersonalInfo();

        return PersonalInfoContainer;
    }

    private void loadPersonalInfo(){
        new PersonalInfo(getActivity(), phoneNum, new PersonalInfo.SuccessCallback() {
            @Override
            public void onSuccess(final Map<String,String> result) {
                final ImageView ivPersonalInfoHead = (ImageView) PersonalInfoContainer.findViewById(R.id.iv_item_personal_info_head);
                headHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        ivPersonalInfoHead.setImageBitmap((Bitmap) msg.obj);
                        Log.e("HEAD","Set");
                        super.handleMessage(msg);
                    }
                };
                if(!BitmapUtils.readImage(ivPersonalInfoHead, getActivity(), "head")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap head = DownloadUtil.getHttpBitmap(result.get(PingTai_Config.KEY_HEAD_IMAGEURL));
                            Bitmap finalHead = BitmapUtils.circleBitmap(BitmapUtils.imageCrop(head,1,1,false));
                            Message msg = new Message();
                            msg.obj = finalHead;
                            headHandler.sendMessage(msg);
                        }
                    }).start();
                }

                TextView tvAccount = (TextView) PersonalInfoContainer.findViewById(R.id.tv_item_personal_info_account);
                tvAccount.setText(result.get(PingTai_Config.KEY_PHONE_NUM));

                TextView tvSex = (TextView) PersonalInfoContainer.findViewById(R.id.tv_item_personal_info_sex);
                tvSex.setText(result.get(PingTai_Config.KEY_SEX));

                TextView tvBir = (TextView) PersonalInfoContainer.findViewById(R.id.tv_item_personal_info_birthday);
                tvBir.setText(result.get(PingTai_Config.KEY_BIRTHDAY));

                TextView tvNick = (TextView) PersonalInfoContainer.findViewById(R.id.tv_item_personal_info_nickname);
                tvNick.setText(result.get(PingTai_Config.KEY_NICKNAME));
            }
        }, new PersonalInfo.FailCallback() {
            @Override
            public void onFail() {
                Toast.makeText(getActivity(),"加载失败，请稍后重试",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
